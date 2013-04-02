package org.opencoin.core.token

import java.util.Date
//import org.opencoin.core.token.Bencode
//import org.opencoin.core.util.BencodeEncoder
//import org.opencoin.core.util.CanonicalJsonEncoder
//import org.opencoin.issuer.FlatMintKey

case class MintKeyCore (
    `type`: String = "mint key", 
	id: BigInt, 
    issuer_id: BigInt, 
    cdd_serial: Int, 
    public_mint_key: PublicRSAKey, 
    denomination: Int, 
    sign_coins_not_before: Date, 
    sign_coins_not_after: Date, 
    coins_expiry_date: Date) 
  extends Bencode {
  //TODO This requirement causes Squeryl exception
  require(`type` == "mint key", "Parameter 'type' must contain 'mint key'.")
  require(id > 0, "Parameter 'id' is invalid.")
  require(issuer_id > 0, "Parameter 'issuer_id' is invalid.")
  require(cdd_serial > 0, "Parameter 'cdd_serial' is invalid.")
  require(public_mint_key != null, "Parameter 'public_mint_key' is invalid.")
  require(denomination > 0, "Parameter 'denomination' is invalid.")
  require(sign_coins_not_before != null, "Parameter 'sign_coins_not_before' is invalid.")
  require(sign_coins_not_after != null, "Parameter 'sign_coins_not_after' is invalid.")
  require(coins_expiry_date != null, "Parameter 'coins_expiry_date' is invalid.")

  def keyValues = Map(
    "type" -> `type`,
	"id" -> id, 
    "issuer_id" -> issuer_id, 
    "cdd_serial" -> cdd_serial, 
    "public_mint_key" -> public_mint_key, 
    "denomination" -> denomination, 
    "sign_coins_not_before" -> sign_coins_not_before, 
    "sign_coins_not_after" -> sign_coins_not_after, 
    "coins_expiry_date" -> coins_expiry_date)
  
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
