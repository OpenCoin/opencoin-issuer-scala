package org.opencoin.core.token

import java.util.Date
import java.math.BigInteger
import org.opencoin.core.util.Base64
import org.opencoin.core.util.BencodeEncoder
import org.opencoin.core.util.CanonicalJsonEncoder
import org.opencoin.issuer.FlatMintKey

case class MintKey (
    `type`: String = "mint key",
	id: Base64,
    issuer_id: Base64,
    cdd_serial: Int,
    public_mint_key: PublicRSAKey,
    denomination: Int,
    sign_coins_not_before: Date,
    sign_coins_not_after: Date,
    coins_expiry_date: Date) {
  //TODO This requirement causes Squeryl exception
  require(`type` == "mint key")
  require(id != null)
  require(issuer_id != null)
  require(cdd_serial != 0)
  require(public_mint_key != null)
  require(denomination > 0)
  require(sign_coins_not_before != null)
  require(sign_coins_not_after != null)
  require(coins_expiry_date != null)

  def canonical = CanonicalJsonEncoder.encode(this)
  
  def getFlatMintKey(signature: Base64): FlatMintKey = {
	FlatMintKey (
		this.id,
		this.issuer_id,
		this.cdd_serial,
		this.public_mint_key.modulus,
		this.public_mint_key.public_exponent,
		this.denomination,
		this.sign_coins_not_before,
		this.sign_coins_not_after,
		this.coins_expiry_date,
		signature)
  }
}

/*    def serialization: String =
		"d10:currencyId" + currencyId.length + ":" + currencyId +
		"12:denominationi" + denomination + "e" +
		"6:issuer" + issuer.length + ":" + issuer +
		"13:key_not_after" + key_not_after.toString.length + ":" + key_not_after.toString +
		"5:keyId" + keyId.length + ":" + keyId +
		"10:not_before" + not_before.toString.length + ":" + not_before.toString +
		"9:publicKey" + publicKey.length + ":" + publicKey +
		"15:token_not_after" + token_not_after.toString.length + ":" + token_not_after.toString + "e"

	def isValid: Boolean = {
        //check mintkey.validationperiod
        true
    } */