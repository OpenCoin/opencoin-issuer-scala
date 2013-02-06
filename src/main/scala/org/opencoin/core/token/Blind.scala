package org.opencoin.core.token

import org.opencoin.core.util.Base64
import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder

case class Blind(
    `type`: String = "blinded payload hash",
    reference: Int,
    blinded_payload_hash: Base64,
    mint_key_id: Base64) {
	
  require(`type` == "blinded payload hash")
  require(reference != 0)
  require(blinded_payload_hash != null)
  require(mint_key_id != null)
  
  def canonical: String = BencodeEncoder.encode(this)
}
