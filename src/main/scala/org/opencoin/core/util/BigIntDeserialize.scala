package org.opencoin.core.util

//import org.codehaus.jackson.map.annotate.JsonCachable
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, SerializerProvider, JsonSerializer}
import com.fasterxml.jackson.core.{Version, JsonParser, JsonGenerator}
import org.eintr.loglady.Logging
import java.math.BigInteger

class BigIntDeserializer extends JsonDeserializer[BigInt] {
  def deserialize(jp: JsonParser, context: DeserializationContext) = {
    //if (!Base64.isValid(jp.getText)) throw context.mappingException("invalid Base64 " + jp.getText)
    BigInt(jp.getText, 16)
  }
  
  override def isCachable = true //instead of using @JsonCachable
}

