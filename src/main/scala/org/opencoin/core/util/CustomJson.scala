package org.opencoin.core.util.CustomJson

import org.opencoin.core.util.BigIntSerializer
import org.opencoin.core.util.BigIntDeserializer
//import org.opencoin.core.util.UrlDeserializer
import org.opencoin.core.util.DateSerializer
import org.opencoin.core.util.TupleSerializer

import com.fasterxml.jackson.databind.module.SimpleModule
//import com.fasterxml.jackson.databind.cfg.DatabindVersion
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.Version

import java.net.URL
import java.util.Date

object CustomJson extends com.codahale.jerkson.Json {
  val module = new SimpleModule("CustomJson", Version.unknownVersion())
  module.addSerializer(classOf[BigInt], new BigIntSerializer)
  module.addDeserializer(classOf[BigInt], new BigIntDeserializer)
//  module.addDeserializer(classOf[URL], new UrlDeserializer)
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
