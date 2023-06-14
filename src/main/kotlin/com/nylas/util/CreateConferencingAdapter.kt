package com.nylas.util

import com.nylas.models.CreateEventRequest
import com.squareup.moshi.*

class CreateConferencingAdapter {
  @FromJson
  @Throws(UnsupportedOperationException::class)
  fun fromJson(reader: JsonReader): CreateEventRequest.Conferencing? {
    throw UnsupportedOperationException("CreateConferencingAdapter is only used for serialization")
  }

  @ToJson
  fun toJson(writer: JsonWriter,
             value: CreateEventRequest.Conferencing?,
             delegateAutocreate: JsonAdapter<CreateEventRequest.Conferencing.Autocreate>,
             delegateDetails: JsonAdapter<CreateEventRequest.Conferencing.Details>
  ) {
    when(value) {
      is CreateEventRequest.Conferencing.Autocreate -> delegateAutocreate.toJson(writer, value)
      is CreateEventRequest.Conferencing.Details -> delegateDetails.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}