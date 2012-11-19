package org.opencoin.core.util

import com.github.tototoshi.base64.{Base64 => Tototoshi}
//import com.fasterxml.jackson.annotation.JsonSerialize
import java.lang.reflect.Modifier
import com.codahale.jerkson.JsonSnakeCase
//import com.codahale.jerkson.Util._
//import com.fasterxml.jackson.core.JsonGenerator
//import com.fasterxml.jackson.annotation._ //{JsonIgnore, JsonIgnoreProperties}
//import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}
//import com.fasterxml.jackson.JsonProcessingException
//import com.fasterxml.jackson.core.JsonProcessingException
import org.codehaus.jackson.map.annotate.JsonSerialize
import org.codehaus.jackson.map.JsonSerializer
import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.SerializerProvider

//import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}
import com.codahale.jerkson.AST._
/*
@JsonCachable
object Base64Serializer extends JsonSerializer[Base64] {
  def serialize(value: Base64, jgen: JsonGenerator, provider: SerializerProvider) = {
		jgen.writeStartObject
		jgen.writeFieldName("name")
		jgen.writeString(value.toString)
		jgen.writeEndObject
		//logger.debug("Customer [" + value + "] mapped to JSON");
  }
}
*/
//object Base64Serializer { classOf[Base64Serializer] }

/**
 * Parameter "b64string" must be Base64 encoded. Otherwise use Seq[Byte] or Base64.encode instead.
 */

//@JsonSerialize(using = Base64Serializer)
case class Base64 (b64string: String) {
  require(isValid(b64string), "String is not Base64 encoded: " + b64string)
  require(b64string.nonEmpty, "String is empty.")
  require(b64string.length <= 65536, "String exceeds allowed lengths of 65,536 bytes.")

  def this(b: Seq[Byte]) = this(Tototoshi.encode(b))
  
  def this(b: Array[Byte]) = this(Tototoshi.encode(b))
  
  //def decode: Seq[Byte] = Tototoshi.decode(b64string)
  
  def decode: Array[Byte] = Tototoshi.decode(b64string).toArray
  
// This is not working:  def getInt: Int = Tototoshi.binaryToDecimal(decode.asInstanceOf[Seq[Int]])

  override def toString: String = b64string
  
  def isValid(b: String): Boolean = {
    if(b.endsWith("==")) b.dropRight(b.length-2).distinct.forall(c => Tototoshi.encodeTable.contains(c))
	else if(b.endsWith("=")) b.dropRight(b.length-1).distinct.forall(c => Tototoshi.encodeTable.contains(c))
	else b.distinct.forall(c => Tototoshi.encodeTable.contains(c))
  }
  
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
