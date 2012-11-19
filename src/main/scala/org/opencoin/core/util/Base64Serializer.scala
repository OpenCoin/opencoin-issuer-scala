package org.opencoin.core.util

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer, SerializerProvider, JsonSerializer}
import org.codehaus.jackson.{Version, JsonParser, JsonGenerator}
import org.eintr.loglady.Logging

@JsonCachable
class Base64Serializer extends JsonSerializer[Base64] with Logging {
  def serialize(value: Base64, json: JsonGenerator, provider: SerializerProvider) {
	log.debug("Base64Serializer: " + value.toString)
    json.writeString(value.toString)
  }
}
