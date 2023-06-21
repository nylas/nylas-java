package com.nylas.util

import com.nylas.models.Conferencing
import com.squareup.moshi.*
import java.io.IOException

class ConferencingAdapter {
  @FromJson
  @Throws(IOException::class)
  fun fromJson(
    reader: JsonReader,
    delegateAutocreate: JsonAdapter<Conferencing.Autocreate>,
    delegateDetails: JsonAdapter<Conferencing.Details>,
  ): Conferencing? {
    val json = JsonHelper.jsonToMap(reader)

    return if (json.containsKey("details")) {
      delegateDetails.fromJsonValue(json)
    } else {
      delegateAutocreate.fromJsonValue(json)
    }
  }

  @ToJson
  fun toJson(writer: JsonWriter, value: Conferencing?) {
    throw UnsupportedOperationException("ConferencingAdapter is only used for deserialization")
  }
}
