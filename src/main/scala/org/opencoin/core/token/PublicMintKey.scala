package org.opencoin.core.token

import java.math.BigInteger

//TODO make crypto algorithm flexible, not only RSA but also ECC for instance

case class PublicMintKey(
  modulus: BigInteger,
  publicExponent: BigInteger)