package org.opencoin.core

import java.net.URL
import org.opencoin.core.token._
import org.scalatest.FunSuite
import java.util.Date

/*
class Base64Test extends FunSuite {
	test("Base64 is tried to be created with empty content and illegal characters") {
	    var intercept[Exception] {
	    	var x = new Base64("")
	    }
		
	    intercept[Exception] {
	    	var x = new Base64("qwerty1234567890!@#$%^&*")
	    }
	}
} */

class MintKeyTest extends FunSuite {
	
    val pKey = PublicRSAKey(
	    modulus = BigInt("1111111111123456777777777777777777777"), 
	    public_exponent = BigInt("78777777777777777777777777777777778765456787654")
	)
	
	val mkc = MintKeyCore (
	    `type` = "mint key", 
		id = BigInt("87654345678765434567"),
	    issuer_id = BigInt("122234567865432345676543"), 
	    cdd_serial = 8765432, 
	    public_mint_key = pKey, 
	    denomination = 1000, 
	    sign_coins_not_before = new Date, 
	    sign_coins_not_after = new Date, 
	    coins_expiry_date = new Date)
	
	test("Mint Key Core with negative, null and empty parameter is tried to be created") {
      
		intercept[Exception] {
	    	MintKeyCore(
	    	    "", 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    null, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    -1, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    0, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    null, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    -1, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    0, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    null, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    -1, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    0, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    null,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    -1,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    0,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    null,
	    	    mkc.sign_coins_not_after,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    null,
	    	    mkc.coins_expiry_date
	    	)
		}
	        
		intercept[Exception] {
	    	MintKeyCore(
	    	    mkc.`type`, 
	    	    mkc.id, 
	    	    mkc.issuer_id, 
	    	    mkc.cdd_serial, 
	    	    mkc.public_mint_key,
	    	    mkc.denomination,
	    	    mkc.sign_coins_not_before,
	    	    mkc.sign_coins_not_after,
	    	    null
	    	)
		}
	}
    
    	val mk = MintKey(
	    `type` = "mint key certificate",
	    mint_key = mkc,
	    signature = BigInt("65432345678765434567898765434567876543"))
	
	test("Mint Key with negative, null and empty parameter is tried to be created") {
      
		intercept[Exception] {
	    	MintKey("", mk.mint_key, mk.signature)
		}
	        
		intercept[Exception] {
	    	MintKey(null, mk.mint_key, mk.signature)
		}
	        
		intercept[Exception] {
	    	MintKey(mk.`type`, null, mk.signature)
		}
	        
		intercept[Exception] {
	    	MintKey(mk.`type`, mk.mint_key, -1)
		}
	        
		intercept[Exception] {
	    	MintKey(mk.`type`, mk.mint_key, 0)
		}
	        
		intercept[Exception] {
	    	MintKey(mk.`type`, mk.mint_key, null)
		}	        
	}

}
