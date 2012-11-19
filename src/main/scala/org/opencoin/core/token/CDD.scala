package org.opencoin.core.token

import java.net.URL
import java.util.Date
import org.opencoin.core.util.Base64
import org.opencoin.issuer.FlatCDD
import org.opencoin.core.util.BencodeEncoder
import org.opencoin.core.util.CanonicalJsonEncoder
import scala.collection.immutable.TreeSet

case class CDD(
    `type`: String = "cdd",
    protocol_version: URL,
    cdd_location: URL,
    issuer_cipher_suite: String,
    issuer_public_master_key: PublicRSAKey,
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
	//extends BencodeEncoder
  {

  require(`type` == "cdd", "Illegal parameter 'type': " + `type`)
  require(protocol_version != null, "Empty  parameter 'protocol_version'.")
  require(cdd_location != null, "Empty  parameter 'cdd_location'.")
  require(issuer_public_master_key != null, "Empty  parameter 'issuer_public_master_key'.")
  require(issuer_cipher_suite != null, "Empty  parameter 'issuer_cipher_suite'.")
  require(cdd_serial >= 0, "Empty  parameter 'cdd_serial'.")
  require(cdd_signing_date != null, "Empty  parameter 'cdd_signing_date'.")
  require(cdd_expiry_date != null, "Empty  parameter 'cdd_expiry_date'.")
  require(currency_name != null, "Empty  parameter 'currency_name'.")
  require(info_service != null, "Empty  parameter 'info_service'.")
  require(validation_service != null, "Empty  parameter 'validation_service'.")
  require(renewal_service != null, "Empty  parameter 'renewal_service'.")
  require(invalidation_service != null, "Empty  parameter 'invalidation_service'.")
  require(denominations != null, "Empty  parameter 'denominations'.")
	
  def canonical = CanonicalJsonEncoder.encode(this)

  def getFlatCDD(latest: Boolean, signature: Base64): FlatCDD = {
	FlatCDD(
		latest,
		this.protocol_version, 
		this.cdd_location,  
		this.issuer_cipher_suite,
		this.issuer_public_master_key.modulus,
		this.issuer_public_master_key.public_exponent,
		this.cdd_serial,
		this.cdd_signing_date,
		this.cdd_expiry_date,
		this.currency_name,
		this.currency_divisor,
		this.info_service,
		this.validation_service,
		this.renewal_service,
		this.invalidation_service,
		this.denominations,
		this.additional_info,
		signature)
	}

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