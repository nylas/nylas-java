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
    delegateGoogle: JsonAdapter<CreateGoogleConnectorRequest>,
    delegateMicrosoft: JsonAdapter<CreateMicrosoftConnectorRequest>,
    delegateImap: JsonAdapter<CreateImapConnectorRequest>,
    delegateVirtualCalendar: JsonAdapter<CreateVirtualCalendarConnectorRequest>,
  ) {
    when (value) {
      is CreateGoogleConnectorRequest -> delegateGoogle.toJson(writer, value)
      is CreateMicrosoftConnectorRequest -> delegateMicrosoft.toJson(writer, value)
      is CreateImapConnectorRequest -> delegateImap.toJson(writer, value)
      is CreateVirtualCalendarConnectorRequest -> delegateVirtualCalendar.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}
