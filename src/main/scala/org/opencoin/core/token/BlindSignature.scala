package org.opencoin.core.token

import org.opencoin.core.util.Base64

case class BlindSignature(
    `type`: String = "blind signature",
    reference: Int,
    blind_signature: Base64) {
	
  require(`type` == "blind signature")
  require(reference != 0)
  require(blind_signature != null)
}