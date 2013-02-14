package org.opencoin.issuer

import java.util.Date

case class DSDB(serial: BigInt, signature: BigInt, mintKeyID: BigInt, date: Date) {
  require(serial != null)
  require(signature != null)
  require(mintKeyID != null)
  require(date != null)
}