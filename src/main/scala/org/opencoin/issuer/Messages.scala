package org.opencoin.issuer

import org.eintr.loglady.Logging
import com.codahale.jerkson.Json._
import org.opencoin.core.messages._
import org.opencoin.core.util.Base64
import org.opencoin.core.token.CDDCertificate
//import org.opencoin.core.token.BlindSignature
//import scala.collection.JavaConversions._ ?

//TODO Catch error cases and respond with appropriate error code

object Messages extends Logging {
  def process(methods: Methods, content: String) {
	val json = parse[Map[String,Any]](content)
	json("type").asInstanceOf[String] match {
	  case "request cdd" => {
		val request = parse[RequestCdd](content)
		val cdd = methods.getCdd(request.cdd_serial)
		ResponseCdd("response cdd", request.message_reference, 200, "OK", cdd)
	  }
	  case "request cdd serial" => {
		val request = parse[RequestCddSerial](content)
		val cdd = methods.getLatestCdd()
		ResponseCddSerial("response cdd serial", request.message_reference, 200, "OK", cdd.cdd.cdd_serial)
	  }
	  case "request invalidation" => {
		val request = parse[RequestInvalidation](content)
		if (methods.invalidate(request.authorization_info, request.coins) == true) {
			ResponseInvalidation("response invalidation", request.message_reference, 200, "OK")
		}
		else ResponseInvalidation("response invalidation", request.message_reference, 400, "Error")
	  }
	  case "request mint keys" => {
		//TODO implement in Methods class
	    val request = parse[RequestMintKeys](content)
		if (request.mint_key_ids.isEmpty == false) {
		  val mintkeys = request.mint_key_ids.map(x => methods.getMintKeyCertificate(Base64(x)))
		  ResponseMintKeys("response mint keys", request.message_reference, 200, "OK", mintkeys)
		}
		else if (request.denominations.isEmpty == false) {
		  val mintkeys = request.denominations.map(x => methods.getMintKeyCertificate(Base64(x.toString)))
		  ResponseMintKeys("response mint keys", request.message_reference, 200, "OK", mintkeys)
		}
		//TODO what should happen if both, IDs and Denominations, are provided?
		else ResponseMintKeys("response mint keys", request.message_reference, 400, "Error", null)
	  }
	  case "request renewal" => {
		val request = parse[RequestRenewal](content)
		val blindSignatures = methods.renew(request.coins, request.blinds)
		blindSignatures match {
			case Some(blindSig) =>
				ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig)
			case None =>
				ResponseMinting("response minting", request.message_reference, 400, "Error", null, null)
		}
	  }
	  case "request resume" => {
		val request = parse[RequestResume](content)
		val blindSignatures = methods.resume(request.transaction_reference.toString)
		blindSignatures match {
			case Some(blindSig) =>
				ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig)
			case None =>
				ResponseMinting("response minting", request.message_reference, 400, "Error", null, null)
		}
	  }
	  case "request validation" => {
		val request = parse[RequestValidation](content)
		val blindSignatures = methods.validate(request.authorization_info, request.blinds)
		blindSignatures match {
			case Some(blindSig) =>
				ResponseMinting("response minting", request.message_reference, 200, "OK", null, blindSig)
			case None =>
				ResponseMinting("response minting", request.message_reference, 400, "Error", null, null)
		}
	  }
	}
  }
}
