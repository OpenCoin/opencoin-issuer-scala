package org.opencoin.core.util

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer, SerializerProvider, JsonSerializer}
import org.codehaus.jackson.{Version, JsonParser, JsonGenerator}
import org.eintr.loglady.Logging
import java.math.BigInteger

@JsonCachable
class BigIntDeserializer extends JsonDeserializer[BigInt] with Logging {
  def deserialize(jp: JsonParser, context: DeserializationContext) = {
    //if (!Base64.isValid(jp.getText)) throw context.mappingException("invalid Base64 " + jp.getText)
	log.debug("BigIntDeserializer: " + jp.getText)
    BigInt(jp.getText, 16)
  }
}

