package org.opencoin.issuer

import java.math.BigInteger
import org.eintr.loglady.Logging
import java.util.Date
import org.opencoin.core.token.PublicRSAKey
import org.opencoin.core.token.MintKeyCore
import org.opencoin.core.token.MintKey
import org.opencoin.issuer.TypeMappers._
import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession

object MintKeyTable extends Table[MintKey]("MINTKEY") with Logging {

  def id = column[BigInt]("id", O NotNull)
  def issuer_id = column[BigInt]("issuer_id", O NotNull)
  def cdd_serial = column[Int]("cdd_serial", O NotNull)
  def modulus = column[BigInt]("modulus", O NotNull)
  def public_exponent = column[BigInt]("public_exponent", O NotNull)
  //def secret_exponent = column[Base64]("secret_exponent", O NotNull)
  def denomination = column[Int]("denomination", O NotNull)
  def sign_coins_not_before = column[Date]("sign_coins_not_before", O NotNull)
  def sign_coins_not_after = column[Date]("sign_coins_not_after", O NotNull)
  def coins_expiry_date = column[Date]("coins_expiry_date", O NotNull)
  def signature = column[BigInt]("signature", O NotNull)

  def * = 
    id ~ 
    issuer_id ~ 
    cdd_serial ~ 
    modulus ~ 
    public_exponent ~ 
    denomination ~ 
    sign_coins_not_before ~ 
    sign_coins_not_after ~ 
    coins_expiry_date ~ 
    signature <> (MintKey.fromRow _, MintKey.toRow _)

/*
  def * = 
    id ~ 
    issuer_id ~ 
    cdd_serial ~ 
    modulus ~ 
    public_exponent ~ 
    denomination ~ 
    sign_coins_not_before ~ 
    sign_coins_not_after ~ 
    coins_expiry_date ~ 
    signature <> (
        //From a row to a CDD object (previously "apply"):
        (id,
	    issuer_id,
	    cdd_serial, 
	    modulus,
	    public_exponent,
	    denomination,
	    sign_coins_not_before,
	    sign_coins_not_after, 
	    coins_expiry_date,
	    signature)
	    => MintKey(
	        "mint key certificate",
	        MintKeyCore(
	            "mint key",
	            id,
			    issuer_id,
			    cdd_serial, 
			    PublicRSAKey(modulus,
			    		public_exponent),
			    denomination,
			    sign_coins_not_before,
			    sign_coins_not_after, 
			    coins_expiry_date),
	        signature),
	    //From a CDD object to a row (previously "unapply"):
	    (x:MintKey) => Some(
	        x.mint_key.id,
	        x.mint_key.issuer_id,
	        x.mint_key.cdd_serial,
	        x.mint_key.public_mint_key.modulus,
	        x.mint_key.public_mint_key.public_exponent,
	        x.mint_key.denomination,
	        x.mint_key.sign_coins_not_before,
	        x.mint_key.sign_coins_not_after,
	        x.mint_key.coins_expiry_date,
	        x.signature)
	    )
*/
  def getMintKey(db: Database, id: BigInt): Option[MintKey] = db withSession { //s: Session =>
	log.debug("getMintKey called. ID: " + id)
	(for { b <- MintKeyTable if b.id is id} yield b).firstOption
  }

  def getMintKeys(db: Database, denomination: Int): List[MintKey] = db withSession { //s: Session =>
    (for { b <- MintKeyTable if b.denomination === denomination} yield b).list //.list.map(_.getMintKey) 
  }

  def getAllMintKeys(db: Database): List[MintKey] = db withSession { //s: Session =>
    (for { b <- MintKeyTable} yield b).list //.list.map(_.getMintKey)
  }

/*  def store(db: Database, items: List[FlatMintKey]): Boolean = {
    db withSession { //s: Session => 
	  items.foreach(c => MintKeyTable.insert(c))
	  //Maybe "insertAll" is faster than "insert", but I can't get it working.
	}
	if(db==items.length) true
	else false
  }

  def getPrivateMintKey(db: Database, pubKeyId: Base64): PrivateRSAKey = {
    db withSession { //s: Session =>
	  (for { b <- MintKeyTable if b.id.is(pubKeyId)} yield b).first().getRSAPrivKey
	}
  }

  def getPrivateMintKey(id: Base64): PrivateMintKey = db withSession { implicit s: Session =>
    (for { b <- MintKeyTable if b.id.like(id.toString)} yield b).first()
  } */

}

