package org.opencoin.issuer

import org.eintr.loglady.Logging
import com.codahale.jerkson.Json._
import org.opencoin.core.util.CustomJson._
import org.opencoin.core.messages._
import org.opencoin.core.token.CDD
import org.opencoin.core.token.MintKey
//import org.opencoin.core.token.BlindSignature
//import scala.collection.JavaConversions._ ?

/**
 * The message-based API. It processes the requests, calls the Methods 
 * class and returns the response object.
 */
//TODO Catch error cases and respond with appropriate error code

object Messages extends Logging {
  def process(methods: Methods, content: String): Response = {
	val json = CustomJson.parse[Map[String,Any]](content)
	log.debug("Request type: " + json("type").asInstanceOf[String])
	json("type").asInstanceOf[String] match {
	  case "request cdd" => {
		val request = CustomJson.parse[RequestCdd](content)
		val cdd = methods.getCdd(request.cdd_serial)
		ResponseCdd("response cdd", request.message_reference, 200, "OK", cdd)
	  }
	  case "request cdd serial" => {
	    log.debug("Request cdd serial detected.")
		val request = CustomJson.parse[RequestCddSerial](content)
		log.debug("Get latest CDD...")
		val cdd = methods.getLatestCdd()
		log.debug("Message reference: "+ request.message_reference + " Serial: "+cdd.cdd.cdd_serial)
		log.debug("Creating response...")
		ResponseCddSerial("response cdd serial", request.message_reference, 200, "OK", cdd.cdd.cdd_serial)
	  }
	  case "request mint keys" => {
		//TODO What to do if both denomination and IDs are provided?
	    val request = CustomJson.parse[RequestMintKeys](content)
		if (request.mint_key_ids.isEmpty == false) {
		  val mintkeys = methods.getMintKeysById(request.mint_key_ids)
		  ResponseMintKeys("response mint keys", request.message_reference, 200, "OK", mintkeys)
		}
		else if (request.denominations.isEmpty == false) {
		  val mintkeys = methods.getMintKeys(request.denominations)
		  ResponseMintKeys("response mint keys", request.message_reference, 200, "OK", mintkeys)
		}
		else { //If both IDs and denomination are missing, respond with all available mint keys
		  val mintkeys = methods.getAllMintKeys
		  ResponseMintKeys("response mint keys", request.message_reference, 200, "OK", mintkeys)
		}
	  }
	  case "request renewal" => {
		val request = CustomJson.parse[RequestRenewal](content)
		val blindSignatures = methods.renew(request.coins, request.blinds)
		blindSignatures match {
			case Some(blindSig) =>
				ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig)
			case None =>
				ResponseMinting("response minting", request.message_reference, 400, "Error", null, null)
		}
	  }
	  case "request resume" => {
		val request = CustomJson.parse[RequestResume](content)
		val blindSignatures = methods.resume(request.transaction_reference.toString)
		blindSignatures match {
			case Some(blindSig) =>
				ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig)
			case None =>
				ResponseMinting("response minting", request.message_reference, 400, "Error", null, null)
		}
	  }
	  case "request validation" => {
		val request = CustomJson.parse[RequestValidation](content)
		val blindSignatures = methods.validate(request.authorization_info, request.blinds)
		blindSignatures match {
			case Some(blindSig) =>
				ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig)
			case None =>
				ResponseMinting("response minting", request.message_reference, 400, "Error", null, null)
		}
	  }
	  case "request invalidation" => {
		val request = CustomJson.parse[RequestInvalidation](content)
		if (methods.invalidate(request.authorization_info, request.coins) == true)
			ResponseInvalidation("response invalidation", request.message_reference, 200, "OK")
		else
		  ResponseInvalidation("response invalidation", request.message_reference, 400, "Error")
	  }
	}
  }
}
