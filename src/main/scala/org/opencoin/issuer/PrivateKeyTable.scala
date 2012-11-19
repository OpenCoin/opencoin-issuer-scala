package org.opencoin.issuer

import java.math.BigInteger
import org.scalaquery._
import ql._
import basic.{ BasicTable => Table, _ }
import basic.BasicDriver.Implicit._
import session._
import org.opencoin.issuer.TypeMappers._
import org.opencoin.core.util.Base64
import org.scalaquery.session.Database.threadLocalSession

/**
 * This object stores private issuer and mint keys.
**/
object PrivateKeyTable extends Table[PrivateRSAKey]("PRIVATE_RSA_KEYS") {

  def reference = column[Base64]("reference", O NotNull)
  def cipher_suite = column[String]("cipher_suite", O NotNull)
  def modulus = column[BigInteger]("modulus", O NotNull)
  def private_exponent = column[BigInteger]("private_exponent", O NotNull)

  def * = reference ~ cipher_suite ~ modulus ~ private_exponent<> (PrivateRSAKey, PrivateRSAKey.unapply _)

  def get(db: Database, id: Base64): PrivateRSAKey = db withSession { //s: Session =>
    (for { b <- PrivateKeyTable if b.reference is id} yield b).first
  }
/*
  def getMintKeyCertificates(db: Database, denomination: Int): List[MintKeyCertificate] = db withSession { //s: Session =>
    (for { b <- MintKeyTable if b.denomination.is(denomination)} yield b).list.map(_.getMintKeyCertificate) //TODO Verify whether to use ".is" or "===".
  }

  def getPrivateMintKey(db: Database, pubKeyId: Base64): RSAPrivKey = {
    db withSession { //s: Session =>
	  (for { b <- MintKeyTable if b.id.is(pubKeyId)} yield b).first().getRSAPrivKey
	}
  }

  def getPrivateMintKey(id: Base64): PrivateMintKey = db withSession { implicit s: Session =>
    (for { b <- MintKeyTable if b.id.like(id.toString)} yield b).first()
  } */

}
