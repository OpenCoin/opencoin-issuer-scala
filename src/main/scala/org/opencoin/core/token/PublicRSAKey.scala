package org.opencoin.core.token

//import org.opencoin.core.token.Bencode
//import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder

//TODO make crypto algorithm flexible, not only RSA but also ECC for instance

case class PublicRSAKey(modulus: BigInt, public_exponent: BigInt) extends Bencode {
  require(modulus > 0, "Parameter 'modulus' is invalid.")
  require(public_exponent > 0, "Parameter 'public_exponent' is invalid.")
    
  def keyValues = Map(
      "modulus" -> modulus,
      "public_exponent" -> public_exponent)
 // def this(modulus: BigInteger, public_exponent: BigInteger) = this(Base64(modulus.toString), Base64(public_exponent.toString))
}
