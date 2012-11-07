
import java.net.URL
import org.scalatest.FunSuite

class CoinTest extends FunSuite {
  
    val blank = Blank(
	  `type` = "token",
      protocol_version = new URL(http://opencoin.org/OpenCoinProtocol/1.0"), 
      issuer_id = Base64("123456"), 
      cdd_location = new URL("http://opencent.net/OpenCent"),
      denomination = 3,
      mint_key_id = Base64("gft6789ohgfr56"),
      serial = Base64("fr4567ugt67")
	)
	
	val coin = Coin(
	  `type` = "coin",
	  token = blank,
	  signature = Base64("fr5678ikjh")
	)
    
    test("Coin with null parameter is tried to be created") {
      
	    var thrown = intercept[Exception] {
	    	var coin = new Coin(null, currencyId, denomination, keyId, serial, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
      
	    thrown = intercept[Exception] {
	    	var coin = new Coin(standardId, null, denomination, keyId, serial, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
	    
	    thrown = intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, denomination, null, serial, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
	    
	    thrown = intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, denomination, keyId, null, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
	}
	
    test("Coin with empty parameter is tried to be created") {
      
	    var thrown = intercept[Exception] {
	    	var coin = new Coin("", currencyId, denomination, keyId, serial, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
      
	    thrown = intercept[Exception] {
	    	var coin = new Coin(standardId, "", denomination, keyId, serial, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")

	    thrown = intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, denomination, "", serial, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
	    
	    thrown = intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, denomination, keyId, "", signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
    }

    test("Coin with zero denomination is tried to be created") {
	    
	    var thrown = intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, 0, keyId, serial, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
	    
	    thrown = intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, -1, keyId, serial, signature)
	    }
	    assert(thrown.getMessage === "Missing or invalid parameter.")
    }
        
    test("Coin with wrong signature is tried to be created") {
	    
	    intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, denomination, keyId, serial, "1234567890abcdef")
	    }
	    //assert(thrown.getMessage === "String index out of range: -1")
    }
        
    test("Coin with non-base64 encoded parameter is tried to be created") {

	    intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, denomination, "1234567890abcdef$", serial, signature)
	    }
	    //assert(thrown.getMessage === "String index out of range: -1")
    }
            
    test("Correctly signed coin with denomination different than keyID's denomination is tried to be created") {
      
	    intercept[Exception] {
	    	var coin = new Coin(standardId, currencyId, 3, keyId, serial, signature)
	    }
	    //assert(thrown.getMessage === "String index out of range: -1")
    }
 /*       
    test("Serial of coin is verified") {
	    
	    coin = new Coin(standardId, currencyId, denomination, keyId, serial) //signature removed

	    var serial:String = rsa(hash("to be done...")).toString()
	    assert(coin.serial === serial)
    }
    */
    test("Two identical coins are created and compared") {
	    
	    var coin = new Coin(standardId, currencyId, denomination, keyId, serial, signature)
	    var coin2 = new Coin(standardId, currencyId, denomination, keyId, serial, signature)
	    assert(coin === coin2)
    }
}
