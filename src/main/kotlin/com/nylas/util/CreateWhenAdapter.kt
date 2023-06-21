package com.nylas.util

import com.nylas.models.CreateEventRequest
import com.squareup.moshi.*

class CreateWhenAdapter {
  @FromJson
  @Throws(UnsupportedOperationException::class)
  fun fromJson(reader: JsonReader): CreateEventRequest.When? {
    throw UnsupportedOperationException("CreateWhenAdapter is only used for serialization")
  }

  @ToJson
  fun toJson(
    writer: JsonWriter,
    value: CreateEventRequest.When?,
    delegateTime: JsonAdapter<CreateEventRequest.When.Time>,
    delegateTimespan: JsonAdapter<CreateEventRequest.When.Timespan>,
    delegateDate: JsonAdapter<CreateEventRequest.When.Date>,
    delegateDatespan: JsonAdapter<CreateEventRequest.When.Datespan>,
  ) {
    when (value) {
      is CreateEventRequest.When.Time -> delegateTime.toJson(writer, value)
      is CreateEventRequest.When.Timespan -> delegateTimespan.toJson(writer, value)
      is CreateEventRequest.When.Date -> delegateDate.toJson(writer, value)
      is CreateEventRequest.When.Datespan -> delegateDatespan.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}
