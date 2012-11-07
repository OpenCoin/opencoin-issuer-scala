package org.opencoin.issuer

import org.opencoin.core.util.Base64
import java.util.Date

case class DSDB(serial: Base64, signature: Base64, mintKeyID: Base64, date: Date) {
  require(serial != null)
  require(signature != null)
  require(mintKeyID != null)
  require(date != null)
}