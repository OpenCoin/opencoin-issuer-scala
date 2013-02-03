package org.opencoin.core.token

import org.opencoin.core.util.Base64
import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder
import java.net.URL

case class Blank (
    `type`: String = "token",
    protocol_version: URL, 
    issuer_id: Base64, 
    cdd_location: URL,
    denomination: Int,
    mint_key_id: Base64,
    serial: Base64) {
	
  require(`type` == "token")
  require(protocol_version != null)
  require(issuer_id != null)
  require(cdd_location != null)
  require(denomination >= 0) //TODO This assumption needs to be confirmed with the specification.
  require(mint_key_id != null)
  require(serial != null)

  //TODO This may become a trait, but first tests failed to execute.
  def canonical = BencodeEncoder.encode(this)
  
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