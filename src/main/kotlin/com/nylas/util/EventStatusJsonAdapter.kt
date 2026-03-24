package com.nylas.util

import com.nylas.models.EventStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapters.EnumJsonAdapter

/**
 * Handles EventStatus values without failing deserialization when the API returns a new status.
 */
class EventStatusJsonAdapter {
  private val delegate = EnumJsonAdapter.create(EventStatus::class.java).withUnknownFallback(null)

  @FromJson
  fun fromJson(reader: JsonReader): EventStatus? = delegate.fromJson(reader)

  @ToJson
  fun toJson(writer: JsonWriter, value: EventStatus?) {
    delegate.toJson(writer, value)
  }
}
