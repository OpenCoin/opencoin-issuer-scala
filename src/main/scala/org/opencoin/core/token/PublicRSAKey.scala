package org.opencoin.core.token

import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder

//TODO make crypto algorithm flexible, not only RSA but also ECC for instance

case class PublicRSAKey(modulus: BigInt, public_exponent: BigInt) {
  
  def canonical = BencodeEncoder.encode(this)
 // def this(modulus: BigInteger, public_exponent: BigInteger) = this(Base64(modulus.toString), Base64(public_exponent.toString))
}
