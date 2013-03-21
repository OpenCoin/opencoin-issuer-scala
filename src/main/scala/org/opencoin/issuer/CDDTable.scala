package org.opencoin.issuer

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import java.util.Date
import java.net.URL
import org.opencoin.core.token.CDD
import org.opencoin.issuer.TypeMappers._

object CDDTable extends Table[FlatCDD]("CDD") {
  
  def latest = column[Boolean]("latest", O NotNull) //TODO how to set "unique"?
//  def `type` = column[String]("type", O NotNull)
  def protocol_version = column[URL]("protocol_version", O NotNull)
  def cdd_location = column[URL]("cdd_location", O NotNull)
  def issuer_cipher_suite = column[String]("issuer_cipher_suite", O NotNull)
  def issuer_key_modulus = column[BigInt]("issuer_key_modulus", O NotNull)
  def issuer_key_public_exponent = column[BigInt]("issuer_key_public_exponent", O NotNull)
  def cdd_serial = column[Int]("cdd_serial", O NotNull)
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
  
  def * = latest ~ protocol_version ~ cdd_location ~ issuer_cipher_suite ~ issuer_key_modulus ~ issuer_key_public_exponent ~ cdd_serial ~ cdd_signing_date ~ cdd_expiry_date ~ currency_name ~ currency_divisor ~ info_service_prio ~ info_service_url ~ validation_service_prio ~ validation_service_url ~ renewal_service_prio ~ renewal_service_url ~ invalidation_service_prio ~ invalidation_service_url ~ denominations ~ additional_info ~ signature <> (FlatCDD, FlatCDD.unapply _)
  
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
  def getCdd(db: Database, serial: Int): CDD = db withSession { //s: Session =>
    (for { b <- CDDTable if b.cdd_serial === serial} yield b).first.getCDD
  }

  def getLatestCdd(db: Database): CDD = db withSession { //s: Session =>
    (for { b <- CDDTable if b.latest === true} yield b).first.getCDD
  }

}
