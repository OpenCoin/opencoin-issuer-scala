package org.opencoin.core.token

import org.opencoin.core.util.Base64
import org.opencoin.core.util.BencodeEncoder
import org.opencoin.core.util.CanonicalJsonEncoder

case class Blind(
    `type`: String = "blinded token hash",
    reference: Int,
    blinded_token_hash: Base64,
    mint_key_id: Base64) {
	
  require(`type` == "blinded token hash")
  require(reference != 0)
  require(blinded_token_hash != null)
  require(mint_key_id != null)
  
  def canonical: String = CanonicalJsonEncoder.encode(this)
}