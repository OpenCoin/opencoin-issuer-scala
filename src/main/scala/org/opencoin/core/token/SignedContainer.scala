package org.opencoin.core.token

import scala.xml.Node
import scala.xml.Elem
//import net.liftweb._
import util._
//import Helpers._
//import common._
//import json._
import scala.xml._

trait SignedContainer extends Container{

	def serialization: String

	def isValid: Boolean
  	
  	def sign(key: MintKey, cipherSuite: String): String = {
	  	import java.security.interfaces.RSAPrivateKey
	//	val privateKey: RSAPrivateKey = key.publicKey
	//	val s:String = rsa(privateKey.getModulus(), privateKey.getPrivateExponent(), hash256base64(serialization)).toString()
		return "test"
	}
	
	import java.math.BigInteger
	def rsa(modulus: BigInteger, pubExp: BigInteger, message: String, cipherSuite: String): Array[Byte] = {
		import java.security.KeyFactory
		import java.security.spec.RSAPublicKeySpec
		import java.security.interfaces.RSAPublicKey
		import javax.crypto.Cipher

		val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
		val pubKeySpec: RSAPublicKeySpec = new RSAPublicKeySpec(modulus, pubExp)
		val key: RSAPublicKey = keyFactory.generatePublic(pubKeySpec).asInstanceOf[RSAPublicKey]
		val cipher: Cipher = Cipher.getInstance("RSA/ECB/NoPadding")
		cipher.init(Cipher.ENCRYPT_MODE, key)
		cipher.doFinal(message.getBytes())
	}
	
	/** create a SHA-256 hash from a Byte array. Found in net.liftweb.util.SecurityHelpers */  
	//def hash256(in : Array[Byte]) : Array[Byte] =
	//	(MessageDigest.getInstance("SHA-256")).digest(in)
  
	/** create a SHA-256 hash from a String. Found in net.liftweb.util.SecurityHelpers */  
/*	def hash256orig(in : String): String = {
		import java.security.MessageDigest
		Base64.encode(MessageDigest.getInstance("SHA-256").digest(in.getBytes("UTF-8")))
	} */
	/*
	import net.liftweb.util.SecurityHelpers
	def hash256base64(in: String): String = {
	  base64Encode(hash256(in.getBytes("UTF-8")))
	  //We may migrate to http://migbase64.sourceforge.net/ which claims to be super fast.
	} */
}
