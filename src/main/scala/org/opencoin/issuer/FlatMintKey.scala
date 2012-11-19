package org.opencoin.issuer

import java.util.Date
import java.math.BigInteger
import org.opencoin.core.util.Base64
import org.opencoin.core.token.MintKey
import org.opencoin.core.token.MintKeyCertificate
//import org.opencoin.issuer.PrivateRSAKey
import org.opencoin.core.token.PublicRSAKey

/**
 * This class combines MintKey, MintKeyCertificate and PublicRSAKey into a flat structure to store it in the data base. It may be replaced by proper mapping in Scalaquery eventually.
 * Links:
 * https://groups.google.com/forum/?fromgroups=#!topic/scalaquery/x5ZmHrOaDKo
 * http://slick.typesafe.com/doc/0.11.2/gettingstarted.html
 * https://mackler.org/LearningSlick/
 * https://github.com/brianhsu/scalaquery-tut/wiki
 * https://github.com/szeiger/scala-query/wiki/Getting-Started
 * https://github.com/tototoshi/unfiltered-scalate-scalaquery-example-bookmarks/blob/master/src/main/scala/com/github/tototoshi/example/bookmarks/models.scala
**/
case class FlatMintKey (
	id: Base64,
    issuer_id: Base64,
    cdd_serial: Int,
	modulus: Base64,
    public_exponent: Base64,
//	private_exponent: BigInteger,
    denomination: Int,
    sign_coins_not_before: Date,
    sign_coins_not_after: Date,
    coins_expiry_date: Date,
	signature: Base64) {

	//def getRSAPrivKey: RSAPrivKey = RSAPrivKey(modulus, private_exponent, "SHA-256", "RSA-2048")
	
	def getPublicRSAKey: PublicRSAKey = PublicRSAKey(Base64(modulus.toString), Base64(public_exponent.toString))
	
	def getMintKey: MintKey = MintKey("mint key", id, issuer_id, cdd_serial, this.getPublicRSAKey, denomination, sign_coins_not_before, sign_coins_not_after, coins_expiry_date)
	
	def getMintKeyCertificate: MintKeyCertificate = MintKeyCertificate("mint key certificate", this.getMintKey, signature)
}