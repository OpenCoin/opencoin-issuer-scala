package org.opencoin.core.token

case class BlindSignature(
    `type`: String = "blind signature",
    reference: Int,
    blind_signature: BigInt) {
	
  require(`type` == "blind signature")
  require(reference != 0)
  require(blind_signature != null)
}