package org.opencoin.core.token

//import org.opencoin.core.util.BencodeEncoder
//import com.fasterxml.jackson.annotation._

//@JsonPropertyOrder(alphabetic=true)
case class CDD (`type`: String, cdd: CDDCore, signature: BigInt) {
  require(`type` == "cdd certificate")
}