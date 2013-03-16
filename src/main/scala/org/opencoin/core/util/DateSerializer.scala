package org.opencoin.core.util

//import com.fasterxml.jackson.map.annotate.JsonCachable
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, SerializerProvider, JsonSerializer}
import com.fasterxml.jackson.core.{Version, JsonParser, JsonGenerator}
import org.eintr.loglady.Logging
import java.util.Date

class DateSerializer extends JsonSerializer[Date] with Logging {
  def serialize(value: Date, json: JsonGenerator, provider: SerializerProvider) {
    val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    json.writeString(dateFormat.format(value))
  }
}
