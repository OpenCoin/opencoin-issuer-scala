package org.opencoin.core.token

import org.opencoin.core.util.Base64
import java.math.BigInteger
import java.util.Date

case class MintKeyCertificate(
    `type`: String = "mint key certificate",
    mint_key: MintKey,
    signature: Base64) {

  require(`type` == "mint key certificate")
  require(mint_key != null)
  require(signature != null)

  /**
   * This constructor is here to allow creating an object from Scalaquery
  **/
/*  def this(
    `type`: String = "mint key certificate",
	type2: String = "mint key",
	id: Base64,
    issuer_id: Base64,
    cdd_serial: Int,
    modulus: BigInteger,
	publicExponent: BigInteger,
    denomination: Int,
    sign_coins_not_before: Date,
    sign_coins_not_after: Date,
    coins_expiry_date: Date,
	signature: Base64) = this(
	  `type`,
	  MintKey(
	    type2, 
	    id, 
		issuer_id, 
		cdd_serial, 
		PublicMintKey(modulus, publicExponent),
		denomination, 
		sign_coins_not_before, 
		sign_coins_not_after, 
		coins_expiry_date),
	  signature) */
  
  //def serialization = mint_key.serialization
}