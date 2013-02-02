package org.opencoin.issuer

import java.math.BigInteger
import java.net.URL
import java.util.Date
import org.opencoin.core.token._
import org.opencoin.core.util.Base64
import org.opencoin.core.util.crypto._
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import org.eintr.loglady.Logging

/* RSA2048 issuer master key 2342:
  modulus:          16499471766454974108538589966273723430508824408570768710473929458877269860284018943574218910313073812521228450646173268974205693295519688195726885597292249759191625346534346403233102908220134775764772697523409414436462520131098694725323494312684702592847658036368022586472215840506294630878927479953779519793500462541303558388286282592101149618894982287521977220824094047010171291846171249051803354578060372516209765793792739319934470359962878804951941502623433686754831832941115103549719597949908480932938264541412786977774873169194555952316723824443448522876264998869026923365430518978838845174658226934472665295611
  public exponent:  65537
  private exponent:  7562310342566405423993807550039368668166441362660005043337593925351411142762277202604366810015473872476809438950665313539637008334388519371091813625455309188192899311216544810722430766698451384628869833654396325599927692443931559152223432903007813852998427637768324190189548185256085541333006143748594369830010123640991267995796939441922308140291158146732032148759729801706829116794630652703310416953037345874287461489529535675777551307435648606456116237509281487473648797850517535999941761700953196644862570444238689710179635871321622221199162941434985928703471407160826348817029715576055705922729789608658586067841
  */
object Testdata extends Logging {
  private val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  
  log.debug("Generating issuer key...")
  val issuerKeyPair = generateKeyPair(Base64("2342"), "RSA")
  //val issuerPublicKey = keyPair._1
  //val issuerPrivateKey = keyPair._2
  val examplePrivateIssuerKey = issuerKeyPair._2
  
  val exampleCDD = new CDD(
	  //latest = true,
	  `type` = "cdd",
	  protocol_version = new URL("http://okfnpad.org/opencoin-v3"),
	  cdd_location = new URL("https://mighty-lake-9219.herokuapp.com/gulden/cdds/serial/2342"),
	  issuer_cipher_suite = "SHA256-RSA2048-CHAUM83",
	  issuerKeyPair._1,
	  cdd_serial = 2342, 
	  cdd_signing_date = dateFormat.parse("2012-11-11T12:00:00Z"),
	  cdd_expiry_date = dateFormat.parse("2013-12-31T12:00:00Z"),
	  currency_name = "Gulden",
	  currency_divisor = 1,
	  info_service = List(new URL("https://mighty-lake-9219.herokuapp.com/gulden/mintkeys/")),
	  validation_service = List(new URL("https://mighty-lake-9219.herokuapp.com/gulden/validation")),
	  renewal_service = List(new URL("https://mighty-lake-9219.herokuapp.com/gulden/renewal")),
	  invalidation_service = List(new URL("https://mighty-lake-9219.herokuapp.com/gulden/invalidation")),
	  denominations = List(1,2,5,10),
	  additional_info = "This CDD is for testing purposes only.")
	  //signature = Base64("FIumYIFs07MBZGQ+DsmJLnUGVydDMPqe9yWAxIhNQ5Tc+uePqHa/d5ns6XALWoVd3ol3bsQCXCkRVOsYk3PYGVo+VQGQAZ4CyrlKPEiHHXHrDYVVtO+UOegy7lQlaQ3dec7TfC8UKS0dfXKju/RioXoF2tOHUDwldxQnph93JBLJxRt1AJacBmwZwiHV/fxplZI471vlRgEHZmJwUDHWUo1ODbOgZ5aZY4cMqzlFr6hBFP44CKmiBo+jgdQj4sTYswgqN7WjDmquBH30U0r+O4vD3vkyR6JLQwkz3qvy7iHCWBmy0dNrtyXdhgGYQ1afUH/+1ltbOV3ekG6rid9xhg=="))
	
	private val cddSignature = sign(exampleCDD.canonical, examplePrivateIssuerKey, "SHA256withRSA")
	val exampleFlatCDD: FlatCDD = exampleCDD.getFlatCDD(true, cddSignature)
	
        log.debug("Generating mint keys...")
	val exampleMintKeys = generateFlatMintKeys(exampleCDD, examplePrivateIssuerKey)
	
	def generateFlatMintKeys(cdd: CDD, privKey: PrivateRSAKey): List[FlatMintKey] =
		cdd.denominations.map(generateFlatMintKey(_, cdd, privKey))
	
	def generateFlatMintKey(denom: Int, cdd: CDD, privKey: PrivateRSAKey): FlatMintKey = {
		val keyPair = generateKeyPair(Base64(cdd.cdd_serial.toString), cdd.issuer_cipher_suite)
		val publicKey = keyPair._1
		val r = new scala.util.Random
		
		val mk = MintKey(
			id = hash(publicKey.canonical, "SHA-256"),
			issuer_id = hash(cdd.issuer_public_master_key.canonical, "SHA-256"),
			cdd_serial = r.nextInt(10000),
			public_mint_key = publicKey,
			denomination = denom,
			sign_coins_not_before = new Date,
			sign_coins_not_after = dateFormat.parse("2013-12-31T12:00:00Z"),
			coins_expiry_date = dateFormat.parse("2015-12-31T12:00:00Z")
		)
		
		val mintSignature = sign(mk.canonical, examplePrivateIssuerKey, "SHA256withRSA")	
		mk.getFlatMintKey(mintSignature)
	}
	
  /*
  Sun RSA private CRT key, 2048 bits, Mint Key 1884
  modulus:          21151197004632100267776648586612400057596480376884352919986840149561306768179901766802010038088943904250928811839446587992958676420873367764630385505736461194290489526525434039793173615910988900854911282812878287898864767381999896312101099025413936615647547055749638747670212184063425020690353292071295806563952196704659849007684426049199683649229847652600414953131690510668895525771425067683271196771322126969662890073408360469679235367026503702172103387542438686759820914566425820682880615203671165619805129163254464964686687402971676336036951105581980226310017086928192909785572114003428818714717528047922711211567
  public exponent:  65537
  private exponent: 97763409930926893405479384009897130253858520160608636137180731771443780554756181137367668351280999854672800947287870369941056842110730740852676142902447301905404200487893624659873783538028239892686112055627768908713003575619198446539537435597881954920303873447482804666741850783473346464859877919865677796119198780201604159110771115077306942906174502923432997378751182103649875422382058538109196684610331249022567585783719184475159024665229683327960118470626096243498401172476772485245466932983969852730987649250878995869930755476911872129196939663685077830737000755881654474991424163377948394502171720062338322
15009
  */
/*  val exampleMintKey = FlatMintKey(
    id = Base64("X"),
    issuer_id = Base64("MTExMjIzMzQzLTExMS0zLTE1MTAxMzctOTcyLTEwNDM5MTA1LTk0Njg5LTk1MC02NDE5MTktMzAzNC0xNjQxLTgzMTUzLTM3LTk5LTg="),
    cdd_serial = 2342,
    modulus = new BigInteger("21151197004632100267776648586612400057596480376884352919986840149561306768179901766802010038088943904250928811839446587992958676420873367764630385505736461194290489526525434039793173615910988900854911282812878287898864767381999896312101099025413936615647547055749638747670212184063425020690353292071295806563952196704659849007684426049199683649229847652600414953131690510668895525771425067683271196771322126969662890073408360469679235367026503702172103387542438686759820914566425820682880615203671165619805129163254464964686687402971676336036951105581980226310017086928192909785572114003428818714717528047922711211567"),
	public_exponent = new BigInteger("65537"),
	//private_exponent = new BigInteger("97763409930926893405479384009897130253858520160608636137180731771443780554756181137367668351280999854672800947287870369941056842110730740852676142902447301905404200487893624659873783538028239892686112055627768908713003575619198446539537435597881954920303873447482804666741850783473346464859877919865677796119198780201604159110771115077306942906174502923432997378751182103649875422382058538109196684610331249022567585783719184475159024665229683327960118470626096243498401172476772485245466932983969852730987649250878995869930755476911872129196939663685077830737000755881654474991424163377948394502171720062338322"),
    denomination = 2,
    sign_coins_not_before = new Date,
    sign_coins_not_after = dateFormat.parse("2013-12-31T12:00:00Z"),
    coins_expiry_date = dateFormat.parse("2015-12-31T12:00:00Z"),
	signature = Base64("der4567u8hgty6ab")
  ) */
  
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
    payload = blank1,
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
    payload = blank2,
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
    payload = blank3,
    signature = Base64("we4yery")
  )
}