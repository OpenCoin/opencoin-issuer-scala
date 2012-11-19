package org.opencoin.core.util

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer, SerializerProvider, JsonSerializer}
import org.codehaus.jackson.{Version, JsonParser, JsonGenerator}
import org.eintr.loglady.Logging

@JsonCachable
class Base64Deserializer extends JsonDeserializer[Base64] {
  def deserialize(jp: JsonParser, context: DeserializationContext) = {
    //if (!Base64.isValid(jp.getText)) throw context.mappingException("invalid Base64 " + jp.getText)
    Base64(jp.getText)
  }
}

