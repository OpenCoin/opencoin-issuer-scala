package org.opencoin.core.util.crypto

import org.opencoin.core.token.PublicRSAKey
import org.opencoin.issuer.PrivateRSAKey
import java.math.BigInteger
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec
import java.security.KeyPairGenerator
import java.security.KeyPair
import java.security.SecureRandom
import org.eintr.loglady.Logging

object generateKeyPair {
  def apply(reference: BigInt, cipher_suite: String): (PublicRSAKey, PrivateRSAKey) = {
    //TODO cipher_suite is ignored for now. Instead RSA-2048 is used always.
	val r = new scala.util.Random
	val keyGen: KeyPairGenerator  = KeyPairGenerator.getInstance("RSA")
	val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN")
	keyGen.initialize(2048, random)
	val keyPair: KeyPair = keyGen.genKeyPair()
	val privateKey: RSAPrivateKey = keyPair.getPrivate.asInstanceOf[RSAPrivateKey]
	val publicKey: RSAPublicKey = keyPair.getPublic.asInstanceOf[RSAPublicKey]
	//This may help: keyPair.getPrivate.asInstanceOf[RSAPrivateKey].getPrivateExponent
	val key_modulus = new BigInt(privateKey.getModulus)
	val key_public_exponent = new BigInt(publicKey.getPublicExponent)
	//val key_private_exponent = Base64(privateKey.getPrivateExponent.toString)
	val privKey = PrivateRSAKey(reference, cipher_suite, new BigInt(privateKey.getModulus), new BigInt(privateKey.getPrivateExponent))
	val pubKey = PublicRSAKey(key_modulus, key_public_exponent)
	(pubKey, privKey)
  }
}

object hash {

  /** create a SHA-256 hash from a String. Found in net.liftweb.util.SecurityHelpers */  
  //TODO A good library for more algorithms https://github.com/Nycto/Hasher
  def apply(in: String, algorithm: String): BigInt = algorithm match { 
    case "SHA-256" =>
	  BigInt(MessageDigest.getInstance("SHA-256").digest(in.getBytes("UTF-8")))
	  //Base64.encode(MessageDigest.getInstance("SHA-256").digest(in.getBytes("UTF-8")).mkString)
	case _ => null
  }
}
/*
  def sign(token: Array[Byte], key: PrivateMintKey): Base64 = {
    import java.math.BigInteger
    import java.security.Signature
    import java.security.spec.RSAPrivateKeySpec
    import java.security.KeyFactory
	
    //Convert public key into java.security.PublicKey format
    //See this tutorial for details: http://www.java2s.com/Tutorial/Java/0490__Security/BasicRSAexample.htm
    val spec = new RSAPrivateKeySpec(key.modulus, key.private_exponent)
    val kf = KeyFactory.getInstance("RSA")
    val privateKey = kf.generatePrivate(spec)
	
    //Sign
    val sig: Signature = Signature.getInstance("SHA256withRSA") //If it fails, try Bouncycastle provider
    sig.initSign(privateKey)
    sig.update(token)
    new Base64(sig.sign)
  }
  */
/**
 * Cipher Suite is ignored for now.
**/
object sign {
  def apply(token: String, privkey: PrivateRSAKey, cipherSuite: String): BigInt = {
    //TODO Use different cipher suites and key lengths. ECDSA: https://github.com/baturinsky/Scala-Ecc#readme
    //require(cipherSuite=="RSA-2048")

	val privateKeySpec: RSAPrivateKeySpec = new RSAPrivateKeySpec(privkey.modulus.bigInteger, privkey.private_exponent.bigInteger)
    val key: RSAPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec).asInstanceOf[RSAPrivateKey]
    val signature: Signature = Signature.getInstance("SHA256withRSA")
    
    signature.initSign(key);
    signature.update(token.getBytes());
    BigInt(signature.sign())
  }
}

/*
  def sign(message: String, secretKey: Key, cipherSuite: String): Base64 = {
    // Install Bouncycastle?
    // Use different cipher suites, key lengths
    //use fixed keys
    require(cipherSuite=="RSA-2048")
    
    val keyGen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
    keyGen.initialize(2048, new SecureRandom());
    val keyPair: KeyPair= keyGen.generateKeyPair();

    
    val signature: Signature = Signature.getInstance("RSA", "BC");
    
    signature.initSign(keyPair.getPrivate(), new SecureRandom());

    signature.update(message.getBytes());

    val sigBytes = signature.sign();
    signature.initVerify(keyPair.getPublic());
    signature.update(message.getBytes());
    
    if(signature.verify(sigBytes)==false) null
    else Base64(sigBytes)
  }
}

//import java.security.Security;
	import java.security.Key
	import org.opencoin.core.util.Base64
  	def sign(message: String, secretKey: Key, cipherSuite: String): String = {
	  	import java.security.interfaces.RSAPrivateKey
	  	val privateKey: RSAPrivateKey = secretKey.publicKey
	  	val s:String = Base64(rsa(message, privateKey.getModulus(), privateKey.getPrivateExponent()))
		return "test"
	}
	
	import java.math.BigInteger
	def rsa(message: String, modulus: BigInteger, pubExp: BigInteger): Seq[Byte] = {
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
} */