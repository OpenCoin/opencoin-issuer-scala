package org.opencoin.core.util.CustomJson

import org.opencoin.core.util.BigIntSerializer
import org.opencoin.core.util.BigIntDeserializer
//import org.opencoin.core.util.BigHexInt
//import org.opencoin.core.util.BigHexIntSerializer
//import org.opencoin.core.util.BigHexIntDeserializer
import org.codehaus.jackson.map.module.SimpleModule
import org.codehaus.jackson.Version
import org.codehaus.jackson.map.SerializationConfig
import org.codehaus.jackson.map.DeserializationConfig
import org.codehaus.jackson.map.ObjectWriter
import org.codehaus.jackson.map.ObjectMapper

object CustomJson extends com.codahale.jerkson.Json {
  val module = new SimpleModule("CustomJson", Version.unknownVersion())
//  module.addSerializer(classOf[BigInt], new BigHexIntSerializer)
//  module.addDeserializer(classOf[BigInt], new BigHexIntDeserializer)
  module.addSerializer(classOf[BigInt], new BigIntSerializer)
  module.addDeserializer(classOf[BigInt], new BigIntDeserializer)
  mapper.registerModule(module)
  
  //Define the appropriate date format:
  val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  val serConfig: SerializationConfig = mapper.getSerializationConfig
  serConfig.withDateFormat(dateFormat)
  val deserializationConfig: DeserializationConfig = mapper.getDeserializationConfig
  deserializationConfig.withDateFormat(dateFormat)
  mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false)
  //TODO The date format is still not exactly appropriate. E.g. "2012-11-10T16:00:00.000+0000" This might help: http://stackoverflow.com/questions/2201925/converting-iso8601-compliant-string-to-java-util-date
}
