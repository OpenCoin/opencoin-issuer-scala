package org.opencoin.issuer

import java.util.Date
import java.net.URL
import org.opencoin.core.util.Base64
import org.opencoin.core.token.CDD

/**
 * This class enriches the CDD class with additional "latest" field to store it in the data base. It may be replaced by proper mapping (hiding field "latest") in Scalaquery eventually.
 * Links:
 * https://groups.google.com/forum/?fromgroups=#!topic/scalaquery/x5ZmHrOaDKo
 * http://slick.typesafe.com/doc/0.11.2/gettingstarted.html
 * https://mackler.org/LearningSlick/
 * https://github.com/brianhsu/scalaquery-tut/wiki
 * https://github.com/szeiger/scala-query/wiki/Getting-Started
 * https://github.com/tototoshi/unfiltered-scalate-scalaquery-example-bookmarks/blob/master/src/main/scala/com/github/tototoshi/example/bookmarks/models.scala
**/
case class FlatCDD (
	latest: Boolean,
  //  `type`: String = "cdd",
    protocol_version: URL,
    cdd_location: URL,
    issuer_public_master_key: Base64,
    issuer_cipher_suite: String,
    cdd_serial: Int,
    cdd_signing_date: Date,
    cdd_expiry_date: Date,
    currency_name: String,
    currency_divisor: Int,
    info_service: List[URL],
    validation_service: List[URL],
    renewal_service: List[URL],
    invalidation_service: List[URL],
    denominations: List[Int],
    additional_info: String) { //This field can be empty.

/*	def this(cdd: CDD) = this(
		false,
		"cdd", 
		cdd.protocol_version, 
		cdd.cdd_location, 
		cdd.issuer_public_master_key, 
		cdd.issuer_cipher_suite,
		cdd.cdd_serial,
		cdd.cdd_signing_date,
		cdd.cdd_expiry_date,
		cdd.currency_name,
		cdd.currency_divisor,
		cdd.info_service,
		cdd.validation_service,
		cdd.renewal_service,
		cdd.invalidation_service,
		cdd.denominations,
		cdd.additional_info)
		*/
	def getCDD: CDD = CDD(
		"cdd", 
		protocol_version, 
		cdd_location, 
		issuer_public_master_key, 
		issuer_cipher_suite,
		cdd_serial,
		cdd_signing_date,
		cdd_expiry_date,
		currency_name,
		currency_divisor,
		info_service,
		validation_service,
		renewal_service,
		invalidation_service,
		denominations,
		additional_info)
}