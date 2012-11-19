package org.opencoin.issuer

import org.opencoin.core.util.Base64
import java.math.BigInteger

case class PrivateRSAKey(
  reference: Base64, //Reference to Mint Key or Issuer Key
  cipher_suite: String,
  modulus: BigInteger,
  private_exponent: BigInteger)