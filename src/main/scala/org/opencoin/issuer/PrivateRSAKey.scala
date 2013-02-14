package org.opencoin.issuer

case class PrivateRSAKey(
  reference: BigInt, //Reference to Mint Key or Issuer Key
  cipher_suite: String,
  modulus: BigInt,
  private_exponent: BigInt)