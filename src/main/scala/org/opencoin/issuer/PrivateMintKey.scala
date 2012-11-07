package org.opencoin.issuer

import org.opencoin.core.util.Base64
import java.math.BigInteger

case class PrivateMintKey(
  reference: Base64, //Reference to Mint Key or Issuer Key
  modulus: BigInteger,
  privateExponent: BigInteger)