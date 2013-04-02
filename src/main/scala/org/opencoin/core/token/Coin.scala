package org.opencoin.core.token

import scala.collection.immutable.HashMap

case class Coin(
    `type`: String = "coin", 
    payload: Blank,
    signature: BigInt) {
	
  require(`type` == "coin", "Parameter 'type' is invalid.")
  require(payload != null, "Parameter 'payload' is invalid.")
  require(signature > 0, "Parameter 'signature' is invalid.")

  def bencode = payload.bencode
}

//import net.liftweb._
//import json._
//import util.Helpers
/*	if (standardId==null || currencyId==null || keyId==null || serial==null || signature==null) 
	  throw new Exception ("Missing or invalid parameter.")
	if (standardId=="" || currencyId=="" || denomination<=0 || keyId=="" || serial=="" || signature=="") 
	  throw new Exception ("Missing or invalid parameter.")
	if (Base64.isEncoded(keyId)==false || Base64.isEncoded(serial)==false || Base64.isEncoded(signature)==false) 
	  throw new Exception ("Parameter is not base64 encoded.")

	// override def toString = "" + denomination + currencyId + " coin"
	// override hashCode with signature? Cache hashCode?
    
	/**
	 * The object is serialized according to Bencode dictionary format. This excludes the
	 * signature because the signature is build over all other serialized elements.
	 * See https://en.wikipedia.org/wiki/Bencode
	 */
	def serialization: String =
		"d10:currencyId"       + currencyId.length + ":" + currencyId +
		"12:denominationi" + denomination + "e" +
		"5:keyId" + keyId.length + ":" + keyId +
		"6:serial" + serial.length + ":" + serial +
		"10:standardId" + standardId + ":" + standardId + "e"
	
	/**
	 * Verifies whether the coin is valid or not. This includes checks of denomination,
	 * validation period etc.
	 * It doesn't verify the coin in the DSDB, which is supposed to be done by the issuer
	 * after this verification is executed.
	 * This function requires accurate system time and references to collections of CDDs
	 * and MintKeys.
	 */
	def isValid(cdds: => HashMap[String,CDD], mintkeys: => HashMap[String,MintKey]): Boolean = {
	  if (standardId != "http://opencoin.org/OpenCoinProtocol/1.0") false
	  else if (cdds.contains(currencyId)==false) false
	  else if (denomination<=0) false
	  else if (mintkeys.contains(keyId)==false) false
	  else if (signature!=sign(mintkeys(keyId),cdds(currencyId).issuerCipherSuite)) false
	  else if (mintkeys(keyId).isValid==false) false
	  else if (cdds(currencyId).isValid==false) false
	  else if (mintkeys(keyId).denomination!=denomination) false
	  else if (mintkeys(keyId).currencyId==currencyId) false
	  else true
	}
}
*/