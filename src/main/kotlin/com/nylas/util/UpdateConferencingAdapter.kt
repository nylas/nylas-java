package com.nylas.util

import com.nylas.models.UpdateEventRequest
import com.squareup.moshi.*

/**
 * This class is used to serialize and deserialize the UpdateEventRequest.Conferencing object.
 * @suppress Not for public use.
 */
class UpdateConferencingAdapter {
  @FromJson
  @Throws(UnsupportedOperationException::class)
  fun fromJson(reader: JsonReader): UpdateEventRequest.Conferencing? {
    throw UnsupportedOperationException("UpdateConferencingAdapter is only used for serialization")
  }

  @ToJson
  fun toJson(
    writer: JsonWriter,
    value: UpdateEventRequest.Conferencing?,
    delegateAutocreate: JsonAdapter<UpdateEventRequest.Conferencing.Autocreate>,
    delegateDetails: JsonAdapter<UpdateEventRequest.Conferencing.Details>,
  ) {
    when (value) {
      is UpdateEventRequest.Conferencing.Autocreate -> delegateAutocreate.toJson(writer, value)
      is UpdateEventRequest.Conferencing.Details -> delegateDetails.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}
