package org.opencoin.core.util

//import org.codehaus.jackson.map.annotate.JsonCachable
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, SerializerProvider, JsonSerializer}
import com.fasterxml.jackson.core.{Version, JsonParser, JsonGenerator}
import org.eintr.loglady.Logging

class BigIntSerializer extends JsonSerializer[BigInt] {
  def serialize(value: BigInt, json: JsonGenerator, provider: SerializerProvider) {
    json.writeString(value.toString(16))
  }
}
