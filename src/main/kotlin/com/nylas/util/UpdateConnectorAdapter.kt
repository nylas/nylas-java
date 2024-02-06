package com.nylas.util

import com.nylas.models.*
import com.squareup.moshi.*

/**
 * This class is used to serialize and deserialize the UpdateConnectorRequest object.
 * @suppress Not for public use.
 */
class UpdateConnectorAdapter {
  @FromJson
  @Throws(UnsupportedOperationException::class)
  fun fromJson(reader: JsonReader): UpdateConnectorRequest? {
    throw UnsupportedOperationException("UpdateConnectorRequest is only used for serialization")
  }

  @ToJson
  fun toJson(
    writer: JsonWriter,
    value: UpdateConnectorRequest?,
    delegateGoogle: JsonAdapter<UpdateConnectorRequest.Google>,
    delegateMicrosoft: JsonAdapter<UpdateConnectorRequest.Microsoft>,
  ) {
    when (value) {
      is UpdateConnectorRequest.Google -> delegateGoogle.toJson(writer, value)
      is UpdateConnectorRequest.Microsoft -> delegateMicrosoft.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}
