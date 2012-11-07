package org.opencoin.core.util

import com.github.tototoshi.base64.{Base64 => Tototoshi}

/**
 * Parameter "b64string" must be Base64 encoded. Otherwise use Seq[Byte] or Base64.encode instead.
 */
case class Base64 (b64string: String) {
  require(b64string.distinct.forall(c => Tototoshi.encodeTable.contains(c)), "String is not Base64 encoded: " + b64string)
  require(b64string.nonEmpty, "String is empty.")
  require(b64string.length <= 65536, "String exceeds allowed lengths of 65,536 bytes.")

  def this(b: Seq[Byte]) = this(Tototoshi.encode(b))
  
  def this(b: Array[Byte]) = this(Tototoshi.encode(b))
  
  //def decode: Seq[Byte] = Tototoshi.decode(b64string)
  
  def decode: Array[Byte] = Tototoshi.decode(b64string).toArray

  override def toString: String = b64string
  
  /**
   * Required for Squeryl.
  *
  import org.squeryl.dsl.ast.LogicalBoolean
  def ===(b: Base64): LogicalBoolean = {
    if (b.toString == b64string) true
    else false
  } */
}

/**
 * This object encodes a String into Base64.
 */

object Base64 {
  def encode(s: String): String = Tototoshi.encode(s.getBytes)
  //def apply(b: Seq[Byte]) = new Base64(Tototoshi.encode(b)) //TODO obsolete? Get second constructor to work.
}
