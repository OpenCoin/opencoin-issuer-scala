package org.opencoin.core.util

import java.lang.reflect.{Type, ParameterizedType}
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.`type`.TypeReference;
import com.fasterxml.jackson.module.scala.experimental._
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationFeature

  /*TODO: Upgrade to Jackson Scala Module 2.2 once released and delete Java file under 'experimental'
   * For further information check Jackson mailinglist and 
   * http://stackoverflow.com/questions/8792973/jackson-scala-module-nested-lists-and-maps
   */

object JacksonWrapper {
  private val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

  val module = new SimpleModule("CustomJson", Version.unknownVersion())
  module.addSerializer(classOf[BigInt], new BigIntSerializer)
  module.addDeserializer(classOf[BigInt], new BigIntDeserializer)
    
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.setDateFormat(dateFormat)
  mapper.registerModule(module)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)

  def serialize(value: Any): String = {
    import java.io.StringWriter
    val writer = new StringWriter()
    //TODO: Enable pretty printer but it will break tests
    //mapper.writer.withDefaultPrettyPrinter.writeValue(writer, value)
    mapper.writeValue(writer, value)
    writer.toString
  }

  def deserialize[T: Manifest](value: String) : T =
    mapper.readValue(value, typeReference[T])

  private [this] def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType = typeFromManifest(manifest[T])
  }

  private [this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) { m.erasure }
    else new ParameterizedType {
      def getRawType = m.erasure
      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray
      def getOwnerType = null
    }
  }
}