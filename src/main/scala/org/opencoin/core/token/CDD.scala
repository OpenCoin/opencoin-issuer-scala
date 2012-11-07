package org.opencoin.core.token

import java.net.URL
import java.util.Date
import org.opencoin.core.util.Base64
import org.opencoin.core.util.BencodeEncoder
import scala.collection.immutable.TreeSet

case class CDD (
    `type`: String = "cdd",
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
    additional_info: String) //This field can be empty.
  {

  require(`type` == "cdd")
  require(protocol_version != null)
  require(cdd_location != null)
  require(issuer_public_master_key != null)
  require(issuer_cipher_suite != null)
  require(cdd_serial != 0)
  require(cdd_signing_date != null)
  require(cdd_expiry_date != null)
  require(currency_name != null)
  require(info_service != null)
  require(validation_service != null)
  require(renewal_service != null)
  require(invalidation_service != null)
  require(denominations != null)
	
  def serialization = BencodeEncoder.encode(this)

 // potentially requires for Squeryl: 
 // def this() = this(	"cdd",new URL("http://example.com"),new URL("http://example.com"),new Base64(""),"",0, new Date(0), new Date(0), "",0,Nil,Nil,Nil,Nil,Nil,"")

 /* 	def serialization: String =
		"d11:currency_Id" + currency_Id.length + ":" + currency_Id +
		"d13:denominationsli" + {denominations.mkString("ei")} + "ee" +
		"6:issuer" + issuer.length + ":" + issuer +
		"21:issuerPublicMasterKey" + issuerPublicMasterKey.length + ":" + issuerPublicMasterKey +
		"21:issuerServiceLocation" + issuerServiceLocation.length + ":" + issuerServiceLocation +
		"7:options" + options.length + ":" + options +
		"15:shortCurrencyId" + shortCurrencyId.length + ":" + shortCurrencyId +
		"11:standard_Id" + standard_Id.length + ":" + standard_Id + "e"
		*/
}