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
//import org.opencoin.issuer.PrivateRSAKey
import org.opencoin.core.util.Base64
import org.scalaquery.session._
import org.scalaquery.ql.extended.H2Driver.Implicit._
import org.scalaquery.ql.basic.{BasicTable => Table}
import org.scalaquery.ql._
import org.scalaquery.session.Database.threadLocalSession
   
class Methods(db: Database) extends Logging {

  //################ Public methods: ##################
  
  def init: Boolean = {
    try {
      db withSession { session: Session => // passes the session
        CDDTable.ddl.create(session)
	    log.debug("CDD Table created. Inserting example CDD...")
		CDDTable.insert(exampleFlatCDD)(session)
	    log.debug("Example CDD inserted. Creating MintKeyTable...")

		MintKeyTable.ddl.create(session)
	    log.debug("MintKeyTable created. Inserting example MintKey...")
		exampleMintKeys.foreach(MintKeyTable.insert(_)(session))
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
  
  def getCdd(serial: Int) = CDDTable.getCdd(db, serial) 

  def getLatestCdd() = CDDTable.getLatestCdd(db) 

  def getMintKeyCertificate(id: Base64) = MintKeyTable.getMintKeyCertificate(db, id)

  def getMintKeyCertificates(denomination: Int) = MintKeyTable.getMintKeyCertificates(db, denomination)

  def getAllMintKeyCertificates = MintKeyTable.getAllMintKeyCertificates(db)

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
    import com.github.tototoshi.base64.{Base64 => Tototoshi}
	val mintkey: PrivateRSAKey = PrivateKeyTable.get(db, blind.mint_key_id)
	//val hash = crypto.hash(blind.canonical, mintkey.hashAlg) Hashing should be part of the sign method. Test it.
	val signature: Base64 = crypto.sign(blind.canonical, mintkey, mintkey.cipher_suite)
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
	
	// To prevent side channel attacks, all evaluations are calculated and stored in values before the final result is calculated and returned:
    val denomination = mintkeycert.mint_key.denomination == coin.token.denomination
	val mintKeyCert = isValid(mintkeycert)
	val spent = notSpent(coin)
    val signature = isValidSignature(coin, mintkeycert.mint_key.public_mint_key)
	
	denomination && mintKeyCert && spent && signature
  }

  private def isValidSignature(token: Array[Byte], signature: Array[Byte], key: PublicRSAKey): Boolean = {
    import java.security.Signature
    import java.security.spec.RSAPublicKeySpec
    import java.security.KeyFactory
	
    //Convert public key into java.security.PublicKey format
    //See this tutorial for details: http://www.java2s.com/Tutorial/Java/0490__Security/BasicRSAexample.htm
    val spec = new RSAPublicKeySpec(new BigInteger(key.modulus.toString), new BigInteger(key.public_exponent.toString))
    val kf = KeyFactory.getInstance("RSA")
    val publicKey = kf.generatePublic(spec)
	
    //Sign
    val sig: Signature = Signature.getInstance("SHA256withRSA") //If it fails, try Bouncycastle provider
    sig.initVerify(publicKey)
    sig.update(token)
    sig.verify(signature)
  }
  
  private def isValidSignature(coin: Coin, key: PublicRSAKey): Boolean = 
    isValidSignature(coin.canonical.getBytes, coin.signature.decode, key)

  private def isValidSignature(cert: MintKeyCertificate): Boolean = 
    isValidSignature(cert.mint_key.canonical.getBytes, cert.signature.decode, cert.mint_key.public_mint_key)

  private def isValid(cert: MintKeyCertificate): Boolean = {
	val today = new Date
	val cddcert = CDDTable.getCdd(db, cert.mint_key.cdd_serial)
	
	// To prevent side channel attacks, all evaluations are calculated and stored in values before the final result is calculated and returned:
	val sign_coins_not_before = cert.mint_key.sign_coins_not_before.before(today)
	val coins_expiry_date = cert.mint_key.coins_expiry_date.after(today)
	val cdd_signing_date = cddcert.cdd.cdd_signing_date.before(today)
    val cdd_expiry_date = cddcert.cdd.cdd_expiry_date.after(today)
	val signature = isValidSignature(cert)
	
	sign_coins_not_before && coins_expiry_date && cdd_signing_date && cdd_expiry_date && signature
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
	keyGen.initialize(2048, random)
	val keyPair: KeyPair = keyGen.genKeyPair()
	val privateKey: RSAPrivateKey = keyPair.getPrivate().asInstanceOf[RSAPrivateKey]
	//This may help: keyPair.getPrivate.asInstanceOf[RSAPrivateKey].getPrivateExponent
	privateKey
  } */
  
}
