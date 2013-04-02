package org.opencoin.core.token

//import org.opencoin.core.token.Bencode
//import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder

case class Blind(
    `type`: String = "blinded payload hash",
    reference: Int,
    blinded_payload_hash: BigInt,
    mint_key_id: BigInt) extends Bencode {
	
  require(`type` == "blinded payload hash", "Parameter 'type' is invalid.")
  require(reference > 0, "Parameter 'reference' is invalid.")
  require(blinded_payload_hash > 0, "Parameter 'blinded_payload_hash' is invalid.")
  require(mint_key_id > 0, "Parameter 'mint_key_id' is invalid.")
  
  def keyValues = Map(
    "type" -> `type`,
    "reference" -> reference,
    "blinded_payload_hash" -> blinded_payload_hash,
    "mint_key_id" -> mint_key_id)
}
