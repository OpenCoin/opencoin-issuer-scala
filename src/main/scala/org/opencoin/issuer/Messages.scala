package org.opencoin.issuer

import org.eintr.loglady.Logging
import com.codahale.jerkson.Json._
import org.opencoin.core.util.CustomJson._
import org.opencoin.core.messages._
import org.opencoin.core.util.Base64
import org.opencoin.core.token.CDD
//import org.opencoin.core.token.BlindSignature
//import scala.collection.JavaConversions._ ?

//TODO Catch error cases and respond with appropriate error code
//TODO Clean up and just return abstract type Response, do JSON serialization in Respond object
object Messages extends Logging {
  def process(methods: Methods, content: String): String = {
	val json = parse[Map[String,Any]](content)
	log.debug("Request type: " + json("type").asInstanceOf[String])
	json("type").asInstanceOf[String] match {
	  case "request cdd" => {
		val request = parse[RequestCdd](content)
		val cdd = methods.getCdd(request.cdd_serial)
		CustomJson.generate(ResponseCdd("response cdd", request.message_reference, 200, "OK", cdd))
	  }
	  case "request cdd serial" => {
	    log.debug("Request cdd serial detected.")
		val request = parse[RequestCddSerial](content)
		log.debug("Get latest CDD...")
		val cdd = methods.getLatestCdd()
		log.debug("Message reference: "+ request.message_reference + " Serial: "+cdd.cdd.cdd_serial)
		log.debug("Creating response...")
		CustomJson.generate(ResponseCddSerial("response cdd serial", request.message_reference, 200, "OK", cdd.cdd.cdd_serial))
	  }
	  case "request invalidation" => {
		val request = parse[RequestInvalidation](content)
		if (methods.invalidate(request.authorization_info, request.coins) == true) {
			CustomJson.generate(ResponseInvalidation("response invalidation", request.message_reference, 200, "OK"))
		}
		else CustomJson.generate(ResponseInvalidation("response invalidation", request.message_reference, 400, "Error"))
	  }
	  case "request mint keys" => {
		//TODO implement in Methods class
	    val request = parse[RequestMintKeys](content)
		if (request.mint_key_ids.isEmpty == false) {
		  val mintkeys = request.mint_key_ids.map(x => methods.getMintKeyCertificate(Base64(x)))
		  CustomJson.generate(ResponseMintKeys("response mint keys", request.message_reference, 200, "OK", mintkeys))
		}
		else if (request.denominations.isEmpty == false) {
		  val mintkeys = request.denominations.map(x => methods.getMintKeyCertificate(Base64(x.toString)))
		  CustomJson.generate(ResponseMintKeys("response mint keys", request.message_reference, 200, "OK", mintkeys))
		}
		//TODO what should happen if both, IDs and Denominations, are provided?
		else CustomJson.generate(ResponseMintKeys("response mint keys", request.message_reference, 400, "Error", null))
	  }
	  case "request renewal" => {
		val request = parse[RequestRenewal](content)
		val blindSignatures = methods.renew(request.coins, request.blinds)
		blindSignatures match {
			case Some(blindSig) =>
				CustomJson.generate(ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig))
			case None =>
				CustomJson.generate(ResponseMinting("response minting", request.message_reference, 400, "Error", null, null))
		}
	  }
	  case "request resume" => {
		val request = parse[RequestResume](content)
		val blindSignatures = methods.resume(request.transaction_reference.toString)
		blindSignatures match {
			case Some(blindSig) =>
				CustomJson.generate(ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig))
			case None =>
				CustomJson.generate(ResponseMinting("response minting", request.message_reference, 400, "Error", null, null))
		}
	  }
	  case "request validation" => {
		val request = parse[RequestValidation](content)
		val blindSignatures = methods.validate(request.authorization_info, request.blinds)
		blindSignatures match {
			case Some(blindSig) =>
				CustomJson.generate(ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig))
			case None =>
				CustomJson.generate(ResponseMinting("response minting", request.message_reference, 400, "Error", null, null))
		}
	  }
	}
  }
}
