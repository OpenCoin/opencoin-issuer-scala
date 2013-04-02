package org.opencoin.issuer

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import java.util.Date
import java.net.URL
import org.opencoin.core.token.CDD
import org.opencoin.core.token.CDDCore
import org.opencoin.core.token.PublicRSAKey
import org.opencoin.issuer.TypeMappers._

object CDDTable extends Table[CDD]("CDD") {
  //TODO Currently all CDDs are "latest". Define a method to add latest CDD and make existing CDDs not-latest.
  //See https://groups.google.com/forum/?fromgroups=#!topic/scalaquery/qtsXlD_pytE
  //This might help: http://stackoverflow.com/questions/13906684/scala-slick-method-i-can-not-understand-so-far
  def latest = column[Boolean]("latest", O NotNull)
  def protocol_version = column[URL]("protocol_version", O NotNull)
  def cdd_location = column[URL]("cdd_location", O NotNull)
  def issuer_cipher_suite = column[String]("issuer_cipher_suite", O NotNull)
  def issuer_key_modulus = column[BigInt]("issuer_key_modulus", O NotNull)
  def issuer_key_public_exponent = column[BigInt]("issuer_key_public_exponent", O NotNull)
  def cdd_serial = column[Int]("cdd_serial", O PrimaryKey)
  def cdd_signing_date = column[Date]("cdd_signing_date", O NotNull)
  def cdd_expiry_date = column[Date]("cdd_expiry_date", O NotNull)
  def currency_name = column[String]("currency_name", O NotNull)
  def currency_divisor = column[Int]("currency_divisor", O NotNull)
  def info_service_prio = column[List[Int]]("info_service_prio", O NotNull)
  def info_service_url = column[List[URL]]("info_service_url", O NotNull)
  def validation_service_prio = column[List[Int]]("validation_service_prio", O NotNull)
  def validation_service_url = column[List[URL]]("validation_service_url", O NotNull)
  def renewal_service_prio = column[List[Int]]("renewal_service_prio", O NotNull)
  def renewal_service_url = column[List[URL]]("renewal_service_url", O NotNull)
  def invalidation_service_prio = column[List[Int]]("invalidation_service_prio", O NotNull)
  def invalidation_service_url = column[List[URL]]("invalidation_service_url", O NotNull)
  def denominations = column[List[Int]]("denominations", O NotNull)
  def additional_info = column[String]("additional_info")
  def signature = column[BigInt]("signature", O NotNull)
  
  //def * = latest ~ protocol_version ~ cdd_location ~ issuer_cipher_suite ~ issuer_key_modulus ~ issuer_key_public_exponent ~ cdd_serial ~ cdd_signing_date ~ cdd_expiry_date ~ currency_name ~ currency_divisor ~ info_service_prio ~ info_service_url ~ validation_service_prio ~ validation_service_url ~ renewal_service_prio ~ renewal_service_url ~ invalidation_service_prio ~ invalidation_service_url ~ denominations ~ additional_info ~ signature <> (FlatCDD, FlatCDD.unapply _)
  //With latest:
  /*
  def * = latest ~ protocol_version ~ cdd_location ~ issuer_cipher_suite ~ issuer_key_modulus ~ issuer_key_public_exponent ~ cdd_serial ~ cdd_signing_date ~ cdd_expiry_date ~ currency_name ~ currency_divisor ~ info_service_prio ~ info_service_url ~ validation_service_prio ~ validation_service_url ~ renewal_service_prio ~ renewal_service_url ~ invalidation_service_prio ~ invalidation_service_url ~ denominations ~ additional_info ~ signature <> 
    //apply:
    ((latest, 
        protocol_version, 
        cdd_location, 
        issuer_cipher_suite, 
        issuer_key_modulus, 
        issuer_key_public_exponent, 
        cdd_serial, 
        cdd_signing_date, 
        cdd_expiry_date, 
        currency_name, 
        currency_divisor, 
        info_service_prio,
        info_service_url, 
        validation_service_prio,
        validation_service_url, 
        renewal_service_prio,
        renewal_service_url,
		invalidation_service_prio,
		invalidation_service_url,
        denominations, 
        additional_info, 
        signature
      )=>
      (latest, 
          CDD("cdd certificate", 
              CDDCore(
                  "cdd", 
                  protocol_version, 
                  cdd_location, 
                  issuer_cipher_suite, 
                  PublicRSAKey(
                      issuer_key_modulus, 
                      issuer_key_public_exponent), 
                  cdd_serial, 
                  cdd_signing_date, 
                  cdd_expiry_date,
                  currency_name, 
                  currency_divisor, 
		          info_service_prio.zip(info_service_url), 
		          validation_service_prio.zip(validation_service_url), 
		          renewal_service_prio.zip(renewal_service_url),
				  invalidation_service_prio.zip(invalidation_service_url),
                  denominations, 
                  additional_info),
          signature)),
    //unapply:
    (f:(Boolean, CDD)) => Some((
        f._1, 
        f._2.cdd.protocol_version, 
        f._2.cdd.cdd_location, 
        f._2.cdd.issuer_cipher_suite, 
        f._2.cdd.issuer_public_master_key.modulus, 
        f._2.cdd.issuer_public_master_key.public_exponent, 
        f._2.cdd.cdd_serial, 
        f._2.cdd.cdd_signing_date, 
        f._2.cdd.cdd_expiry_date, 
        f._2.cdd.currency_name, 
        f._2.cdd.currency_divisor, 
        f._2.cdd.info_service.map(x => x._1), 
        f._2.cdd.info_service.map(x => x._2),
        f._2.cdd.validation_service.map(x => x._1),
        f._2.cdd.validation_service.map(x => x._2),
        f._2.cdd.renewal_service.map(x => x._1),
        f._2.cdd.renewal_service.map(x => x._2),
        f._2.cdd.invalidation_service.map(x => x._1),
        f._2.cdd.invalidation_service.map(x => x._2),
        f._2.cdd.denominations,
        f._2.cdd.additional_info,
        f._2.signature))
    ) */
  
  //Without latest:
/*    def * =
        protocol_version ~ 
    	cdd_location ~ 
    	issuer_cipher_suite ~ 
    	issuer_key_modulus ~ 
    	issuer_key_public_exponent ~ 
    	cdd_serial ~ cdd_signing_date ~ 
    	cdd_expiry_date ~ currency_name ~ 
    	currency_divisor ~ 
    	info_service_prio ~ 
    	info_service_url ~ 
    	validation_service_prio ~ 
    	validation_service_url ~ 
    	renewal_service_prio ~ 
    	renewal_service_url ~ 
    	invalidation_service_prio ~ 
    	invalidation_service_url ~ 
    	denominations ~ 
    	additional_info ~ 
    	signature <> (
    //From a row to a CDD object (previously "apply"):
    	(protocol_version, 
        cdd_location, 
        issuer_cipher_suite, 
        issuer_key_modulus, 
        issuer_key_public_exponent, 
        cdd_serial, 
        cdd_signing_date, 
        cdd_expiry_date, 
        currency_name, 
        currency_divisor, 
        info_service_prio,
        info_service_url, 
        validation_service_prio,
        validation_service_url, 
        renewal_service_prio,
        renewal_service_url,
		invalidation_service_prio,
		invalidation_service_url,
        denominations, 
        additional_info, 
        signature) 
        => CDD("cdd certificate", 
              CDDCore(
                  "cdd", 
                  protocol_version, 
                  cdd_location, 
                  issuer_cipher_suite, 
                  PublicRSAKey(
                      issuer_key_modulus, 
                      issuer_key_public_exponent), 
                  cdd_serial, 
                  cdd_signing_date, 
                  cdd_expiry_date,
                  currency_name, 
                  currency_divisor, 
		          info_service_prio.zip(info_service_url), 
		          validation_service_prio.zip(validation_service_url), 
		          renewal_service_prio.zip(renewal_service_url),
				  invalidation_service_prio.zip(invalidation_service_url),
                  denominations, 
                  additional_info),
          signature),
    //From a CDD object to a row (previously "unapply"):
    (x:CDD) => Some(
        x.cdd.protocol_version, 
        x.cdd.cdd_location, 
        x.cdd.issuer_cipher_suite, 
        x.cdd.issuer_public_master_key.modulus, 
        x.cdd.issuer_public_master_key.public_exponent, 
        x.cdd.cdd_serial, 
        x.cdd.cdd_signing_date, 
        x.cdd.cdd_expiry_date, 
        x.cdd.currency_name, 
        x.cdd.currency_divisor, 
        x.cdd.info_service.map(x => x._1), 
        x.cdd.info_service.map(x => x._2),
        x.cdd.validation_service.map(x => x._1),
        x.cdd.validation_service.map(x => x._2),
        x.cdd.renewal_service.map(x => x._1),
        x.cdd.renewal_service.map(x => x._2),
        x.cdd.invalidation_service.map(x => x._1),
        x.cdd.invalidation_service.map(x => x._2),
        x.cdd.denominations,
        x.cdd.additional_info,
        x.signature)
    )
  */
  
  def * =
        protocol_version ~ 
    	cdd_location ~ 
    	issuer_cipher_suite ~ 
    	issuer_key_modulus ~ 
    	issuer_key_public_exponent ~ 
    	cdd_serial ~ cdd_signing_date ~ 
    	cdd_expiry_date ~ currency_name ~ 
    	currency_divisor ~ 
    	info_service_prio ~ 
    	info_service_url ~ 
    	validation_service_prio ~ 
    	validation_service_url ~ 
    	renewal_service_prio ~ 
    	renewal_service_url ~ 
    	invalidation_service_prio ~ 
    	invalidation_service_url ~ 
    	denominations ~ 
    	additional_info ~ 
    	signature <> (CDD.fromRow _, CDD.toRow _)
    	
  //See https://groups.google.com/forum/?fromgroups=#!topic/scalaquery/x5ZmHrOaDKo
//  def insert = `type` ~ protocol_version ~ cdd_location ~ issuer_public_master_key ~ issuer_cipher_suite ~ cdd_serial ~ cdd_signing_date ~ cdd_expiry_date ~ currency_name ~ currency_divisor ~ info_service ~ validation_service ~ renewal_service ~ invalidation_service ~ denominations ~ additional_info <> (CDD, CDD.unapply _)
  
  //def forInsert = first ~ last <> ({ (f, l) => User(0, f, l) }, { u:
	//User => Some((u.first, u.last)) })

/*  def getCdd(db: Database, serial: Int): Option[CDD] = db withSession { //s: Session =>
    (for { b <- CDDTable if b.cdd_serial === serial} yield b).first.getCDD match {
      case x: CDD => Some(x)
	  case _ => None
    }
  }
  */
  def getCdd(db: Database, serial: Int): Option[CDD] = db withSession { //s: Session =>
    (for { b <- CDDTable if b.cdd_serial === serial} yield b).firstOption
  }

  //TODO This is a dummy method which returns the first record it retrieves.
  // Instead the latest CDD should be returned.
  def getLatestCdd(db: Database): CDD = db withSession { //s: Session =>
    (for { b <- CDDTable } yield b).first
    //(for { b <- CDDTable if b.latest === true} yield b).first
  }

}
