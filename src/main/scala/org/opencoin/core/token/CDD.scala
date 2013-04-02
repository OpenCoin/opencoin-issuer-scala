package org.opencoin.core.token

//import org.opencoin.core.util.BencodeEncoder
//import com.fasterxml.jackson.annotation._
import java.util.Date
import java.net.URL

//@JsonPropertyOrder(alphabetic=true)
case class CDD (`type`: String, cdd: CDDCore, signature: BigInt) {
  require(`type` == "cdd certificate", "Parameter 'type' is invalid.")
  require(cdd != null, "Parameter 'cdd' is invalid.")
  require(signature > 0, "Parameter 'signature' is invalid.")
}

object CDD {
  def fromRow(
    protocol_version: URL,
    cdd_location: URL, 
    issuer_cipher_suite: String, 
    issuer_key_modulus: BigInt,
    issuer_key_public_exponent: BigInt, 
    cdd_serial: Int, 
    cdd_signing_date: Date, 
    cdd_expiry_date: Date, 
    currency_name: String, 
    currency_divisor: Int, 
    info_service_prio: List[Int],
    info_service_url: List[URL], 
    validation_service_prio: List[Int],
    validation_service_url: List[URL], 
    renewal_service_prio: List[Int],
    renewal_service_url: List[URL],
	invalidation_service_prio: List[Int],
	invalidation_service_url: List[URL],
    denominations: List[Int], 
    additional_info: String,
    signature: BigInt) = CDD(
      "cdd certificate",
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
      signature)
      
  def toRow(x: CDD) = Some((
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
    x.signature  
  ))
}