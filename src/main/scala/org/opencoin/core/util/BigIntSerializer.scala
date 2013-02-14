package org.opencoin.core.util

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer, SerializerProvider, JsonSerializer}
import org.codehaus.jackson.{Version, JsonParser, JsonGenerator}
import org.eintr.loglady.Logging

@JsonCachable
class BigIntSerializer extends JsonSerializer[BigInt] with Logging {
  def serialize(value: BigInt, json: JsonGenerator, provider: SerializerProvider) {
	log.debug("BigIntSerializer: " + value.toString)
    json.writeString(value.toString(16))
  }
}
