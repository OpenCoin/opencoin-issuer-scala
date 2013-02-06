package org.opencoin.issuer

import java.net.URL
import java.util.Date
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import org.eintr.loglady.Logging
import org.opencoin.core.token.CDDCore
import org.opencoin.core.token.CDD
import org.opencoin.core.token.PublicRSAKey
import org.opencoin.core.util.Base64
import org.scalaquery.session.Database
import org.scalaquery.session._
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.ql.extended.H2Driver.Implicit._
import org.scalaquery.ql.basic.{BasicTable => Table}
import org.scalaquery.ql._
import org.opencoin.core.util.crypto._

object Wizard extends Logging{

	println("Opencoin Issuer Wizard\n")
	
	var database = "jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1"
	println("Database init string ["+database+"] :")
	var x = readLine
	if(x != "") database = x
	println(database)
	val db: Database = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
	
	println("Creating CDD...")
	println("Is this CDD the latest? [Y/N] :")
	val latest = readBoolean
	println(latest)
	
	var protocol_version = new URL("http://okfnpad.org/opencoin-v3")
	println("Protocol version ["+protocol_version+"] :")
	x = readLine
	if(x != "") protocol_version = new URL(x)
	println(protocol_version)
	
	val r = new scala.util.Random
	var cdd_serial = r.nextInt(10000)
	println("Serial ["+cdd_serial+"] :")
	x = readLine
	if(x != "") cdd_serial = x.toInt
	println(cdd_serial)
	
	var issuer_cipher_suite = "SHA256-RSA2048-CHAUM83"
	println("Issuer cipher suite ["+issuer_cipher_suite+"] :")
	x = readLine
	if(x != "") issuer_cipher_suite = x
	println(issuer_cipher_suite)
	
	println("Generating RSA-2048 keys...")
	val keyPair = generateKeyPair(Base64(cdd_serial.toString), issuer_cipher_suite)
	println("Public exponent: " + keyPair._1.public_exponent)
	println("Modulus: " + keyPair._1.modulus)
	println("Private exponent: " + keyPair._2.private_exponent)
	
	val dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy")
	val cdd_signing_date = new Date //today
	println("CDD signing date: " + cdd_signing_date)
	
	var cdd_expiry_date = dateFormat.parse("31.12.2013")
	println("CDD expiry date ["+cdd_expiry_date+"] :")
	x = readLine
	if(x != "") cdd_expiry_date = dateFormat.parse(x)
	println(cdd_expiry_date)
	
	println("Currency name: ")
	val currency_name = readLine
	
	var currency_divisor = 1
	println("Currency divisor ["+currency_divisor+"] :")
	x = readLine
	if(x != "") currency_divisor = x.toInt
	println(currency_divisor)
	
	val base_url = "https://mighty-lake-9219.herokuapp.com/"
	val cdd_location = new URL(base_url + currency_name + "/cdds/serial/" + cdd_serial)
	val info_service = List((10, new URL(base_url + currency_name + "/mintkeys/")))
	val validation_service = List((10, new URL(base_url + currency_name + "/validation")))
	val renewal_service = List((10, new URL(base_url + currency_name + "/renewal")))
	val invalidation_service = List((10, new URL(base_url + currency_name + "/invalidation")))
	
	var denominations = List(1,2,5,10)
	println("Denominations ["+denominations+"] :")
/*	println("[1]: ")
	i = readInt
	if(i != null) var list ::= i else var list ::= 1
	println("[2]: ")
	i = readInt
	if(i != null) list ::= i else list ::= 2
	println("[5]: ")
	i = readInt
	if(i != null) list ::= i else list ::= 5
	println("[10]: ")
	i = readInt
	if(i != null) list ::= i else list ::= 10 */
	
	println("Additional information: ")
	val additional_info = readLine
	
	println("Creating CDD...")
	val cdd = CDDCore (
      "cdd",
      protocol_version,
      cdd_location,
      issuer_cipher_suite,
      keyPair._1,
      cdd_serial,
      cdd_signing_date,
      cdd_expiry_date,
      currency_name,
      currency_divisor,
      info_service,
      validation_service,
      renewal_service,
      invalidation_service,
      denominations,
      additional_info)
	  
	println("Calculating signature...")
	val signature = sign(cdd.canonical, keyPair._2, "SHA256withRSA")
	println("Signature: " + signature)

	println("Creating CDD Certificate...")
	val cddcert = CDD("cdd certificate", cdd, signature)
	println("CDD Certificate created successfully: " + cddcert)
	
	println("Generating Mint Keys...")
	val exampleMintKeys = Testdata.generateFlatMintKeys(cdd, keyPair._2)
	println("Mint Keys created.")
	
	try {
      db withSession { session: Session => // passes the session
        CDDTable.ddl.create(session)
	    log.debug("CDD Table created. Inserting example CDD...")
		CDDTable.insert(cdd.getFlatCDD(true, signature))(session)
	    log.debug("Example CDD inserted. Creating MintKeyTable...")

		MintKeyTable.ddl.create(session)
	    log.debug("MintKeyTable created. Inserting example MintKey...")
		exampleMintKeys.foreach(MintKeyTable.insert(_)(session))
	    log.debug("Example MintKey inserted. Creating DSDBTable...")
	  }
	}

}

