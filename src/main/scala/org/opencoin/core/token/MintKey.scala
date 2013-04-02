package org.opencoin.core.token

import java.util.Date

case class MintKey(
    `type`: String = "mint key certificate",
    mint_key: MintKeyCore,
    signature: BigInt) {

  require(`type` == "mint key certificate", "Parameter 'type' is invalid.")
  require(mint_key != null, "Parameter 'mint_key' is invalid.")
  require(signature > 0, "Parameter 'signature' is invalid.")
}

/**
 * This object is here to allow mapping a record from SLICK and an object to SLICK
**/	
object MintKey {
  def fromRow(
	id: BigInt,
    issuer_id: BigInt,
    cdd_serial: Int,
    modulus: BigInt,
	public_exponent: BigInt,
    denomination: Int,
    sign_coins_not_before: Date,
    sign_coins_not_after: Date,
    coins_expiry_date: Date,
	signature: BigInt): MintKey = MintKey(
	  "mint key certificate",
	  MintKeyCore(
	    "mint key", 
	    id, 
		issuer_id, 
		cdd_serial, 
		PublicRSAKey(modulus, public_exponent),
		denomination, 
		sign_coins_not_before, 
		sign_coins_not_after, 
		coins_expiry_date),
	  signature)
	  
  def toRow(x: MintKey) = Some((
      x.mint_key.id, 
      x.mint_key.issuer_id, 
      x.mint_key.cdd_serial, 
      x.mint_key.public_mint_key.modulus,
      x.mint_key.public_mint_key.public_exponent,
      x.mint_key.denomination,
      x.mint_key.sign_coins_not_before,
      x.mint_key.sign_coins_not_after,
      x.mint_key.coins_expiry_date,
      x.signature))
}