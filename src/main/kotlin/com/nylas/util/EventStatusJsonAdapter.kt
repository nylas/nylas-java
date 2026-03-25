package com.nylas.util

import com.nylas.models.EventStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

/**
 * Normalizes legacy event status values and avoids failing deserialization on new values.
 */
class EventStatusJsonAdapter {
  @FromJson
  fun fromJson(reader: JsonReader): EventStatus? {
    if (reader.peek() == JsonReader.Token.NULL) {
      return reader.nextNull()
    }

    return when (reader.nextString()) {
      "confirmed" -> EventStatus.CONFIRMED
      "maybe", "tentative" -> EventStatus.MAYBE
      "cancelled" -> EventStatus.CANCELLED
      else -> null
    }
  }

  @ToJson
  @Suppress("DEPRECATION")
  fun toJson(writer: JsonWriter, value: EventStatus?) {
    when (value) {
      null -> writer.nullValue()
      EventStatus.CONFIRMED -> writer.value("confirmed")
      EventStatus.MAYBE, EventStatus.TENTATIVE -> writer.value("maybe")
      EventStatus.CANCELLED -> writer.value("cancelled")
    }
  }
}
