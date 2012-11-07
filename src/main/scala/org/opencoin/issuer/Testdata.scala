package org.opencoin.issuer

import java.math.BigInteger
import java.net.URL
import java.util.Date
import org.opencoin.core.token._
import org.opencoin.core.util.Base64

object Testdata {
  val dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy")
  val exampleCdd = new FlatCDD(
	  true,
	  new URL("http://opencoin.org"),
	  new URL("http://mycdd.com"),
	  Base64("se567ujhgt78u"),
	  "RSA",
	  123456789, 
	  new Date, //current day
	  dateFormat.parse("31.12.2013"),
	  "mycurrency",
	  1,
	  List(new URL("http://myissuer.com/info")),
	  List(new URL("http://myissuer.com/validate")),
	  List(new URL("http://myissuer.com/renew")),
	  List(new URL("http://myissuer.com/invalidate")),
	  List(1,2,5,10),
	  "This CDD is just for testing purposes.")
	  
  val exampleMintKey = FlatMintKey(
    id = Base64("frt678uijhab"),
    issuer_id = Base64("de45678uhyt78"),
    cdd_serial = 123456789,
    modulus = new BigInteger("123456789"),
	public_exponent = new BigInteger("0987654"),
	secret_exponent = new BigInteger("56789"),
    denomination = 2,
    sign_coins_not_before = new Date,
    sign_coins_not_after = dateFormat.parse("31.12.2013"),
    coins_expiry_date = dateFormat.parse("31.12.2015"),
	signature = Base64("der4567u8hgty6ab")
  )
  
  val blank1 = Blank (
    `type` = "token",
    protocol_version = new URL("http://opencoin.org"),
    issuer_id = Base64("se567ujhgt78u"), 
    cdd_location = new URL("http://mycdd.com"),
    denomination = 1, // This is wrong, see the assigned issuer key's denomination
    mint_key_id = Base64("frt678uijhab"),
    serial = Base64("awseredrf234")
  )
  
  val coin1 = Coin(
    `type` = "coin", 
    token = blank1,
    signature = Base64("fr5678uhgtyy6")
  )
  
  val blank2 = Blank (
    `type` = "token",
    protocol_version = new URL("http://opencoin.org"),
    issuer_id = Base64("ji9876tgy6"), 
    cdd_location = new URL("http://mycdd.com"),
    denomination = 2,
    mint_key_id = Base64("frt678uijhab"),
    serial = Base64("bgygfredr35")
  )
  
  val coin2 = Coin(
    `type` = "coin", 
    token = blank2,
    signature = Base64("juytfvbji8")
  )
  
  val blank3 = Blank (
    `type` = "token",
    protocol_version = new URL("http://opencoin.org"),
    issuer_id = Base64("dse56ygft6y7"), 
    cdd_location = new URL("http://mycdd.com"),
    denomination = 3, // This is wrong, see the assigned issuer key's denomination
    mint_key_id = Base64("frt678uijhab"),
    serial = Base64("45ywuyw")
  )
  
  val coin3 = Coin(
    `type` = "coin", 
    token = blank3,
    signature = Base64("we4yery")
  )
}