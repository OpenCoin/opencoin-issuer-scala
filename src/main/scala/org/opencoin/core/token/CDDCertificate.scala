package org.opencoin.core.token

import org.opencoin.core.util.Base64
import org.opencoin.core.util.BencodeEncoder
//import com.fasterxml.jackson.annotation._

//@JsonPropertyOrder(alphabetic=true)
case class CDDCertificate (`type`: String, cdd: CDD, signature: Base64) {
  require(`type` == "cdd certificate")
}