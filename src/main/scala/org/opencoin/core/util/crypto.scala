package org.opencoin.core.util.crypto

import org.opencoin.core.util.Base64
import java.math.BigInteger
import java.security.interfaces.RSAPrivateKey
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec

object hash {

  /** create a SHA-256 hash from a String. Found in net.liftweb.util.SecurityHelpers */  
  //TODO A good library for more algorithms https://github.com/Nycto/Hasher
  def apply(in: String, algorithm: String): String = algorithm match { 
    case "SHA-256" =>
	  Base64.encode(MessageDigest.getInstance("SHA-256").digest(in.getBytes("UTF-8")).mkString)
	case _ => null
  }
}

object sign {
  def apply(message: String, privkey: RSAPrivKey, cipherSuite: String): Base64 = {
    //TODO Install Bouncycastle?
    //import java.security.Security
    //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    //TODO Use different cipher suites and key lengths. ECDSA: https://github.com/baturinsky/Scala-Ecc#readme
    require(cipherSuite=="RSA-2048")

	val privateKeySpec: RSAPrivateKeySpec = new RSAPrivateKeySpec(privkey.modulus, privkey.privateExponent)
    val key: RSAPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec).asInstanceOf[RSAPrivateKey]
    val signature: Signature = Signature.getInstance("RSA") //, "BC");
    
    signature.initSign(key);
    signature.update(message.getBytes());
    new Base64(signature.sign())
  }
}

//case class RSAPrivKey(modulus: BigInt, privateExponent: BigInt, hashAlg: String, signAlg: String)
case class RSAPrivKey(modulus: BigInteger, privateExponent: BigInteger, hashAlg: String, signAlg: String)

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