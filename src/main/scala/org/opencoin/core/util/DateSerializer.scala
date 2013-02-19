package org.opencoin.core.util

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer, SerializerProvider, JsonSerializer}
import org.codehaus.jackson.{Version, JsonParser, JsonGenerator}
import org.eintr.loglady.Logging
import java.util.Date

@JsonCachable
class DateSerializer extends JsonSerializer[Date] with Logging {
  def serialize(value: Date, json: JsonGenerator, provider: SerializerProvider) {
    val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
	log.debug("DateSerializer: " + dateFormat.format(value))
    json.writeString(dateFormat.format(value))
  }
}
