package org.opencoin.core.token

import org.opencoin.core.token.Bencode
//import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder
import java.net.URL

case class Blank(
    `type`: String = "payload",
    protocol_version: URL, 
    issuer_id: BigInt, 
    cdd_location: URL,
    denomination: Int,
    mint_key_id: BigInt,
    serial: BigInt) extends Bencode {
	
  require(`type` == "payload")
  require(protocol_version != null)
  require(issuer_id != null)
  require(cdd_location != null)
  require(denomination >= 0) //TODO This assumption needs to be confirmed with the specification.
  require(mint_key_id != null)
  require(serial != null)

  //TODO This may become a trait, but first tests failed to execute.
  //def getBencode = BencodeEncoder.encode(getCCParams(this))
  
  //def getBencode = BencodeEncoder.encode(this)
  
  def keyValues = Map(
    "type" -> `type`,
    "protocol_version" -> protocol_version, 
    "issuer_id" -> issuer_id, 
    "cdd_location" -> cdd_location,
    "denomination" -> denomination,
    "mint_key_id" -> mint_key_id,
    "serial" -> serial)
  
/*  def getKeyValue = Map(
    `type` -> "payload",
    "protocol_version" -> protocol_version, 
    "issuer_id" -> issuer_id, 
    "cdd_location" -> cdd_location,
    "denomination" -> denomination,
    "mint_key_id" -> mint_key_id,
    "serial" -> serial)
    */
	/**
	 * The object is serialized according to Bencode dictionary format. This excludes the
	 * signature because the signature is build over all other serialized elements.
	 * See https://en.wikipedia.org/wiki/Bencode
	 */
/*  def serialization: String =
    "d4:type:5:token"+
    "16:protocol_version" + protocol_version.toString.length +":"+ protocol_version +
    "9:issuer_id" + issuer_id.length +":"+ issuer_id +
    "12:cdd_location" + cdd_location.toString.length +":"+ cdd_location +
    "12:denominationi" + denomination +"e"+
    "11:mint_key_id" + mint_key_id.length +":"+ mint_key_id +
    "6:serial" + serial.length +":"+ serial +"e"
*/    
}
