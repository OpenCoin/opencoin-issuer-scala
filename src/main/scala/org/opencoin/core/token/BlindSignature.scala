package org.opencoin.core.token

case class BlindSignature(
    `type`: String = "blind signature",
    reference: Int,
    blind_signature: BigInt) {
	
  require(`type` == "blind signature", "Parameter 'type' is invalid.")
  require(reference > 0, "Parameter 'reference' is invalid.")
  require(blind_signature > 0, "Parameter 'blind_signature' is invalid.")
}