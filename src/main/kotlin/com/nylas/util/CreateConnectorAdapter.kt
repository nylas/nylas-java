package com.nylas.util

import com.nylas.models.*
import com.squareup.moshi.*

/**
 * This class is used to serialize and deserialize the CreateConnectorRequest object.
 * @suppress Not for public use.
 */
class CreateConnectorAdapter {
  @FromJson
  @Throws(UnsupportedOperationException::class)
  fun fromJson(reader: JsonReader): CreateEventRequest.Conferencing? {
    throw UnsupportedOperationException("CreateConnectorRequest is only used for serialization")
  }

  @ToJson
  fun toJson(
    writer: JsonWriter,
    value: CreateConnectorRequest?,
    delegateGoogle: JsonAdapter<CreateConnectorRequest.Google>,
    delegateMicrosoft: JsonAdapter<CreateConnectorRequest.Microsoft>,
    delegateImap: JsonAdapter<CreateConnectorRequest.Imap>,
    delegateVirtualCalendar: JsonAdapter<CreateConnectorRequest.VirtualCalendar>,
  ) {
    when (value) {
      is CreateConnectorRequest.Google -> delegateGoogle.toJson(writer, value)
      is CreateConnectorRequest.Microsoft -> delegateMicrosoft.toJson(writer, value)
      is CreateConnectorRequest.Imap -> delegateImap.toJson(writer, value)
      is CreateConnectorRequest.VirtualCalendar -> delegateVirtualCalendar.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}
