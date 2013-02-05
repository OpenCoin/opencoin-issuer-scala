package org.opencoin.issuer

import java.math.BigInteger
import org.eintr.loglady.Logging
import org.scalaquery._
import ql._
import basic.{ BasicTable => Table, _ }
import basic.BasicDriver.Implicit._
import session._
import java.util.Date
import org.opencoin.core.util.Base64
import org.opencoin.core.token.CDD
import org.opencoin.core.token.MintKeyCertificate
import org.opencoin.issuer.TypeMappers._
import org.scalaquery.session.Database.threadLocalSession

object MintKeyTable extends Table[FlatMintKey]("MINTKEY") with Logging {

  def id = column[Base64]("id", O NotNull)
  def issuer_id = column[Base64]("issuer_id", O NotNull)
  def cdd_serial = column[Int]("cdd_serial", O NotNull)
  def modulus = column[Base64]("modulus", O NotNull)
  def public_exponent = column[Base64]("public_exponent", O NotNull)
  //def secret_exponent = column[Base64]("secret_exponent", O NotNull)
  def denomination = column[Int]("denomination", O NotNull)
  def sign_coins_not_before = column[Date]("sign_coins_not_before", O NotNull)
  def sign_coins_not_after = column[Date]("sign_coins_not_after", O NotNull)
  def coins_expiry_date = column[Date]("coins_expiry_date", O NotNull)
  def signature = column[Base64]("signature", O NotNull)

  def * = id ~ issuer_id ~ cdd_serial ~ modulus ~ public_exponent ~ denomination ~ sign_coins_not_before ~ sign_coins_not_after ~ coins_expiry_date ~ signature <> (FlatMintKey, FlatMintKey.unapply _)

  def getMintKeyCertificate(db: Database, id: Base64): MintKeyCertificate = db withSession { //s: Session =>
	log.debug("getMintKeyCertificate called. ID: " + id)
	(for { b <- MintKeyTable if b.id is id} yield b).first().getMintKeyCertificate
  }

  def getMintKeyCertificates(db: Database, denomination: Int): List[MintKeyCertificate] = db withSession { //s: Session =>
    (for { b <- MintKeyTable if b.denomination === denomination} yield b).list.map(_.getMintKeyCertificate) 
  }

  def getAllMintKeyCertificates(db: Database): List[MintKeyCertificate] = db withSession { //s: Session =>
    (for { b <- MintKeyTable} yield b).list.map(_.getMintKeyCertificate)
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
