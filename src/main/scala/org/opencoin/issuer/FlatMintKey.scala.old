package org.opencoin.issuer

import java.util.Date
import org.opencoin.core.token.MintKeyCore
import org.opencoin.core.token.MintKey
//import org.opencoin.issuer.PrivateRSAKey
import org.opencoin.core.token.PublicRSAKey

/**
 * This class combines MintKey, MintKeyCore and PublicRSAKey into a flat structure to store it in the data base. It may be replaced by proper mapping in Scalaquery eventually.
 * Links:
 * https://groups.google.com/forum/?fromgroups=#!topic/scalaquery/x5ZmHrOaDKo
 * http://slick.typesafe.com/doc/0.11.2/gettingstarted.html
 * https://mackler.org/LearningSlick/
 * https://github.com/brianhsu/scalaquery-tut/wiki
 * https://github.com/szeiger/scala-query/wiki/Getting-Started
 * https://github.com/tototoshi/unfiltered-scalate-scalaquery-example-bookmarks/blob/master/src/main/scala/com/github/tototoshi/example/bookmarks/models.scala
**/
case class FlatMintKey (
	id: BigInt,
    issuer_id: BigInt,
    cdd_serial: Int,
	modulus: BigInt,
    public_exponent: BigInt,
//	private_exponent: BigInteger,
    denomination: Int,
    sign_coins_not_before: Date,
    sign_coins_not_after: Date,
    coins_expiry_date: Date,
	signature: BigInt) {

	//def getRSAPrivKey: RSAPrivKey = RSAPrivKey(modulus, private_exponent, "SHA-256", "RSA-2048")
	
	def getPublicRSAKey: PublicRSAKey = PublicRSAKey(modulus, public_exponent)
	
	private def getMintKeyCore: MintKeyCore = MintKeyCore("mint key", id, issuer_id, cdd_serial, this.getPublicRSAKey, denomination, sign_coins_not_before, sign_coins_not_after, coins_expiry_date)
	
	def getMintKey: MintKey = MintKey("mint key certificate", this.getMintKeyCore, signature)
}