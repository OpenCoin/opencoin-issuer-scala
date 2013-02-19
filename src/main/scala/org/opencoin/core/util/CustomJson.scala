package org.opencoin.core.util.CustomJson

import org.opencoin.core.util.BigIntSerializer
import org.opencoin.core.util.BigIntDeserializer
import org.opencoin.core.util.DateSerializer
import org.opencoin.core.util.TupleSerializer
import org.codehaus.jackson.map.module.SimpleModule
import org.codehaus.jackson.Version
import org.codehaus.jackson.map.SerializationConfig
import org.codehaus.jackson.map.DeserializationConfig
import org.codehaus.jackson.map.ObjectWriter
import org.codehaus.jackson.map.ObjectMapper
import java.net.URL
import java.util.Date

object CustomJson extends com.codahale.jerkson.Json {
  val module = new SimpleModule("CustomJson", Version.unknownVersion())
  module.addSerializer(classOf[BigInt], new BigIntSerializer)
  module.addDeserializer(classOf[BigInt], new BigIntDeserializer)
  module.addSerializer(classOf[(Int,URL)], new TupleSerializer)
  module.addSerializer(classOf[Date], new DateSerializer)
  mapper.registerModule(module)
  
  //Define the appropriate date format:
/*  val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  val serConfig: SerializationConfig = mapper.getSerializationConfig
  serConfig.withDateFormat(dateFormat)
  val deserializationConfig: DeserializationConfig = mapper.getDeserializationConfig
  deserializationConfig.withDateFormat(dateFormat)
  mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false) */
}
