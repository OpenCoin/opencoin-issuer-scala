package org.opencoin.issuer

import java.math.BigInteger
import java.net.URL
import java.sql.SQLException
import java.util.Date
import org.eintr.loglady.Logging
import org.opencoin.issuer.TypeMappers._
import org.opencoin.issuer.Testdata._
import org.opencoin.core.token._
import org.opencoin.core.util.crypto
import org.opencoin.core.util.crypto.RSAPrivKey
import org.opencoin.core.util.Base64
import org.scalaquery.session._
import org.scalaquery.ql.extended.H2Driver.Implicit._
import org.scalaquery.ql.basic.{BasicTable => Table}
import org.scalaquery.ql._
import org.scalaquery.session.Database.threadLocalSession
   
class Issuer(db: Database) extends Logging {

  //################ Public methods: ##################
  
  def init: Boolean = {
    try {
      db withSession { session: Session => // passes the session
        CDDTable.ddl.create(session)
	    log.debug("CDD Table created. Inserting example CDD...")
		CDDTable.insert(exampleCdd)(session)
	    log.debug("Example CDD inserted. Creating MintKeyTable...")

		MintKeyTable.ddl.create(session)
	    log.debug("MintKeyTable created. Inserting example MintKey...")
		MintKeyTable.insert(exampleMintKey)(session)
	    log.debug("Example MintKey inserted. Creating DSDBTable...")

		DSDBTable.ddl.create(session)
	    log.debug("DSDBTable created.")
      }
	  true
	}
	catch {
	  case e: Exception => {
        log.debug("Exception: %s" format e)
		false
	  }
	}
  }
  
  def getCdd(serial: Int): Option[CDD] = CDDTable.getCdd(db, serial) match {
    case x: CDD => Some(x)
	case _ => None
  }

  def getLatestCdd(): Option[CDD] = CDDTable.getLatestCdd(db) match {
    case x: CDD => Some(x)
	case _ => None
  }

  def getMintKeyCertificate(id: Base64): Option[MintKeyCertificate] = MintKeyTable.getMintKeyCertificate(db, id) match {
    case x: MintKeyCertificate => Some(x)
	case _ => None
  }

  def getMintKeyCertificates(denomination: Int): Option[List[MintKeyCertificate]] = MintKeyTable.getMintKeyCertificates(db, denomination) match {
    case x: List[MintKeyCertificate] => Some(x)
	case _ => None
  }
	
  def validate(token: String, blinds: List[Blind]): Option[List[BlindSignature]] = {
    val value = blinds.map(getDenomination).sum
  	log.debug("validate. Token: " + token + " Blinds: " + blinds.toString + " Value: " + value)
	if (authenticate(token, value))
	  Some(blinds.map(mint))
	else None
  }
  
  def renew(coins: List[Coin], blinds: List[Blind]): Option[List[BlindSignature]] = {
    if(sumCoins(coins) == sumBlinds(blinds) && areValid(coins)) {
	  if(storeInDSDB(coins)) {
	    Some(blinds.map(mint))
	  }
      else None
	}
	else None
  }
  
  def invalidate(account: String, coins: List[Coin]): Option[Boolean] = {
    if(areValid(coins)) {
	  val amount = sumCoins(coins)
	  if(storeInDSDB(coins)) {
	    //TODO Auf Konto 'account' gutschreiben.
		Some(true)
	  }
	  else Some(false)
	}
	else Some(false)
  }
	
	/**
	 * TODO implement cache
	 * Get BlindSignatures with transaction ID from cache.
	 */
  def resume(transaction: String): Option[List[BlindSignature]] = None

  //################ Private methods: ##################
  
  private def mint(blind: Blind): BlindSignature = {
	val mintkey: RSAPrivKey = MintKeyTable.getPrivateMintKey(db, blind.mint_key_id)
	val hash = crypto.hash(blind.serialization, mintkey.hashAlg)
	val signature: Base64 = crypto.sign(hash, mintkey, mintkey.signAlg)
	BlindSignature("blind signature", blind.reference, signature)
  }
  
  private def getDenomination(blind: Blind): Int = MintKeyTable.getMintKeyCertificate(db, blind.mint_key_id).mint_key.denomination

  //TODO implement
  private def authenticate(password: String, value: Int): Boolean = {
    if(password=="opencoin" && value < 1000) true
    else false
  }

  private def sumCoins(coins: List[Coin]): Int =
	coins.map(x => MintKeyTable.getMintKeyCertificate(db, x.token.mint_key_id).mint_key.denomination).sum

  private def sumBlinds(blinds: List[Blind]): Int =
	blinds.map(x => MintKeyTable.getMintKeyCertificate(db, x.mint_key_id).mint_key.denomination).sum
  
  private def areValid(coins: List[Coin]): Boolean = coins.forall(isValid)

  /**
   * A coin is valid when all of the following is true:
   *   the denomination is correct
   *   signature is correct
   *   the key used for signing is valid
   *   the coin's serial is not in the DSDB
  */
  private def isValid(coin: Coin): Boolean = {
    val mintkeycert = MintKeyTable.getMintKeyCertificate(db, coin.token.mint_key_id)
    mintkeycert.mint_key.denomination == coin.token.denomination &&
	  isValid(mintkeycert) &&
	    notSpent(coin) &&
          isValidSignature(coin, mintkeycert.mint_key.public_mint_key)
  }

  private def isValidSignature(token: Array[Byte], signature: Array[Byte], key: PublicMintKey): Boolean = {
    import java.security.Signature
    import java.security.spec.RSAPublicKeySpec
    import java.security.KeyFactory
	
    //Convert public key into java.security.PublicKey format
    //See this tutorial for details: http://www.java2s.com/Tutorial/Java/0490__Security/BasicRSAexample.htm
    val spec = new RSAPublicKeySpec(key.modulus, key.publicExponent)
    val kf = KeyFactory.getInstance("RSA")
    val publicKey = kf.generatePublic(spec)
	
    //Sign
    val sig: Signature = Signature.getInstance("SHA256withRSA") //If it fails, try Bouncycastle provider
    sig.initVerify(publicKey)
    sig.update(token)
    sig.verify(signature)
  }
  
  private def isValidSignature(coin: Coin, key: PublicMintKey): Boolean = 
    isValidSignature(coin.serialization.getBytes, coin.signature.decode, key)

  private def isValidSignature(cert: MintKeyCertificate): Boolean = 
    isValidSignature(cert.mint_key.serialization.getBytes, cert.signature.decode, cert.mint_key.public_mint_key)

  private def isValid(cert: MintKeyCertificate): Boolean = {
	val today = new Date
	val cdd = CDDTable.getCdd(db, cert.mint_key.cdd_serial)
	
	cert.mint_key.sign_coins_not_before.before(today) &&
	  cert.mint_key.coins_expiry_date.after(today) &&
	    cdd.cdd_signing_date.before(today) &&
          cdd.cdd_expiry_date.after(today) &&
			isValidSignature(cert)
  }
  
  private def notSpent(coin: Coin): Boolean = DSDBTable.notSpent(db, coin)

  private def storeInDSDB(coins: List[Coin]): Boolean = DSDBTable.storeInDSDB(db, coins)
    
/*  private def generatePrivRsaKey: RSAPrivateKey = {
	import java.security.KeyPairGenerator
	import java.security.SecureRandom
	import java.security.KeyPair
	import java.security.interfaces.RSAPrivateKey
	
	val keyGen: KeyPairGenerator  = KeyPairGenerator.getInstance("RSA")
	val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN")
	keyGen.initialize(512, random)
	val keyPair: KeyPair = keyGen.genKeyPair()
	val privateKey: RSAPrivateKey = keyPair.getPrivate().asInstanceOf[RSAPrivateKey]
	privateKey
  } */
  
}
