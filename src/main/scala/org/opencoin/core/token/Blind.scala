package org.opencoin.core.token

import org.opencoin.core.token.Bencode
//import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder

case class Blind(
    `type`: String = "blinded payload hash",
    reference: Int,
    blinded_payload_hash: BigInt,
    mint_key_id: BigInt) extends Bencode {
	
  require(`type` == "blinded payload hash")
  require(reference != 0)
  require(blinded_payload_hash != null)
  require(mint_key_id != null)
  
  def getKeyValue = Map(
    "type" -> `type`,
    "reference" -> reference,
    "blinded_payload_hash" -> blinded_payload_hash,
    "mint_key_id" -> mint_key_id)
}
