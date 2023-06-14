package com.nylas.util

import com.nylas.models.UpdateEventRequest
import com.squareup.moshi.*

class UpdateWhenAdapter {
  @FromJson
  @Throws(UnsupportedOperationException::class)
  fun fromJson(reader: JsonReader): UpdateEventRequest.When? {
    throw UnsupportedOperationException("UpdateWhenAdapter is only used for serialization")
  }

  @ToJson
  fun toJson(writer: JsonWriter,
             value: UpdateEventRequest.When?,
             delegateTime: JsonAdapter<UpdateEventRequest.When.Time>,
             delegateTimespan: JsonAdapter<UpdateEventRequest.When.Timespan>,
             delegateDate: JsonAdapter<UpdateEventRequest.When.Date>,
             delegateDatespan: JsonAdapter<UpdateEventRequest.When.Datespan>
  ) {
    when(value) {
      is UpdateEventRequest.When.Time -> delegateTime.toJson(writer, value)
      is UpdateEventRequest.When.Timespan -> delegateTimespan.toJson(writer, value)
      is UpdateEventRequest.When.Date -> delegateDate.toJson(writer, value)
      is UpdateEventRequest.When.Datespan -> delegateDatespan.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}