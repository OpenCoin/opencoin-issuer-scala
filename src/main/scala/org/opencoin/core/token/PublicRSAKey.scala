package org.opencoin.core.token

import java.math.BigInteger
import org.opencoin.core.util.Base64
import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder

//TODO make crypto algorithm flexible, not only RSA but also ECC for instance

case class PublicRSAKey(modulus: Base64, public_exponent: Base64) {
  
  def canonical = BencodeEncoder.encode(this)
 // def this(modulus: BigInteger, public_exponent: BigInteger) = this(Base64(modulus.toString), Base64(public_exponent.toString))
}
