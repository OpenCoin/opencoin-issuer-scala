package org.opencoin.core

import java.net.URL
import org.opencoin.core.token.{Blank, Coin}
import org.scalatest.FunSuite


class URLTest extends FunSuite {
	test("URL is tried to be created with empty content") {
    	var thrown = intercept[Exception] {
			new URL("")
	    }
	}
}

/*
class Base64Test extends FunSuite {
	test("Base64 is tried to be created with empty content and illegal characters") {
	    var thrown = intercept[Exception] {
	    	var x = new Base64("")
	    }
		
	    thrown = intercept[Exception] {
	    	var x = new Base64("qwerty1234567890!@#$%^&*")
	    }
	}
} */

class BlankAndCoinTest extends FunSuite {
  
    val b = Blank(
	  `type` = "payload",
      protocol_version = new URL("http://opencoin.org/OpenCoinProtocol/1.0"), 
      issuer_id = BigInt("123456f", 16), 
      cdd_location = new URL("http://opencent.net/OpenCent"),
      denomination = 3,
      mint_key_id = BigInt("42a73c56f", 16),
      serial = BigInt("85e5d76a", 16)
	)
	
    val b1 = Blank(
	  `type` = "payload",
      protocol_version = new URL("http://opencoin.org/OpenCoinProtocol/1.0"), 
      issuer_id = BigInt("123456f", 16), 
      cdd_location = new URL("http://opencent.net/OpenCent"),
      denomination = 3,
      mint_key_id = BigInt("42a73c56f", 16),
      serial = BigInt("85e5d76a", 16)
	)
	
	val c = Coin(
	  `type` = "coin",
	  payload = b,
	  signature = BigInt("8765feac876", 16)
	)

	//Identical to previous coin
	val c1 = Coin(
	  `type` = "coin",
	  payload = b1,
	  signature = BigInt("8765feac876", 16)
	)
	
	test("Coin with null and empty parameter is tried to be created") {      
	    var thrown = intercept[Exception] {
	    	var coin = new Coin("", c.payload, c.signature)
	    }
		
	    thrown = intercept[Exception] {
	    	var coin = new Coin(null, c.payload, c.signature)
	    }
		
	    thrown = intercept[Exception] {
	    	var coin = new Coin(c.`type`, null, c.signature)
	    }
		
	    thrown = intercept[Exception] {
	    	var coin = new Coin(c.`type`, c.payload, null)
	    }
	}
	
    test("Blank with null and empty parameter, zero denomination is tried to be created") {
	    var thrown = intercept[Exception] {
	    	var blank = Blank("", b.protocol_version, b.issuer_id, b.cdd_location, b.denomination, b.mint_key_id, b.serial)
		}
		
	    thrown = intercept[Exception] {
	    	var blank = Blank(null, b.protocol_version, b.issuer_id, b.cdd_location, b.denomination, b.mint_key_id, b.serial)
		}
		
	    thrown = intercept[Exception] {
	    	var blank = Blank(b.`type`, null, b.issuer_id, b.cdd_location, b.denomination, b.mint_key_id, b.serial)
		}
		
	    thrown = intercept[Exception] {
	    	var blank = Blank(b.`type`, b.protocol_version, null, b.cdd_location, b.denomination, b.mint_key_id, b.serial)
		}
		
	    thrown = intercept[Exception] {
	    	var blank = Blank(b.`type`, b.protocol_version, b.issuer_id, null, b.denomination, b.mint_key_id, b.serial)
		}
		
/* According to the current specification, zero and negative denominations are allowed.
	    thrown = intercept[Exception] {
	    	var blank = Blank(b.`type`, b.protocol_version, b.issuer_id, b.cdd_location, 0, b.mint_key_id, b.serial)
		}
		
	    thrown = intercept[Exception] {
	    	var blank = Blank(b.`type`, b.protocol_version, b.issuer_id, b.cdd_location, -3, b.mint_key_id, b.serial)
		}
*/		
	    thrown = intercept[Exception] {
	    	var blank = Blank(b.`type`, b.protocol_version, b.issuer_id, b.cdd_location, b.denomination, null, b.serial)
		}
		
	    thrown = intercept[Exception] {
	    	var blank = Blank(b.`type`, b.protocol_version, b.issuer_id, b.cdd_location, b.denomination, b.mint_key_id, null)
	    }
    }
        
    test("Two identical coins are compared") {
	    assert(c === c1)
    }
/*TODO
    test("Coin with wrong signature is tried to be created") {

    }
            
    test("Correctly signed coin with denomination different than keyID's denomination is tried to be created") {

    }
     
    test("Serial of coin is verified") {

    }
*/
}
