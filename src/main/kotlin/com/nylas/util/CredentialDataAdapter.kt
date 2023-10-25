package com.nylas.util

import com.nylas.models.*
import com.squareup.moshi.*

/**
 * This class is used to serialize and deserialize the CredentialData object.
 * @suppress Not for public use.
 */
class CredentialDataAdapter {
  @FromJson
  fun fromJson(
    reader: JsonReader,
    microsoftAdapter: JsonAdapter<MicrosoftAdminConsentCredentialData>,
    googleAdapter: JsonAdapter<GoogleServiceAccountCredentialData>,
    overrideAdapter: JsonAdapter<ConnectorOverrideCredentialData>,
  ): CredentialData? {
    val map = reader.readJsonValue() as? Map<*, *> ?: return null
    return when {
      map.containsKey("client_id") -> microsoftAdapter.fromJsonValue(map)
      map.containsKey("private_key_id") -> googleAdapter.fromJsonValue(map)
      else -> overrideAdapter.fromJsonValue(map)
    }
  }

  @ToJson
  fun toJson(
    writer: JsonWriter,
    value: CredentialData?,
    microsoftAdapter: JsonAdapter<MicrosoftAdminConsentCredentialData>,
    googleAdapter: JsonAdapter<GoogleServiceAccountCredentialData>,
    overrideAdapter: JsonAdapter<ConnectorOverrideCredentialData>,
  ) {
    when (value) {
      is MicrosoftAdminConsentCredentialData -> microsoftAdapter.toJson(writer, value)
      is GoogleServiceAccountCredentialData -> googleAdapter.toJson(writer, value)
      is ConnectorOverrideCredentialData -> overrideAdapter.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}

/**
 * This class is used to serialize and deserialize the MicrosoftAdminConsentCredentialData object.
 * @suppress Not for public use.
 */
class MicrosoftAdminConsentCredentialDataAdapter {
  @FromJson
  fun fromJson(reader: JsonReader): MicrosoftAdminConsentCredentialData {
    var clientId = ""
    var clientSecret = ""
    val extraProperties = mutableMapOf<String, String>()

    reader.beginObject()
    while (reader.hasNext()) {
      when (val key = reader.nextName()) {
        "client_id" -> clientId = reader.nextString()
        "client_secret" -> clientSecret = reader.nextString()
        else -> extraProperties[key] = reader.nextString()
      }
    }
    reader.endObject()

    return MicrosoftAdminConsentCredentialData(clientId, clientSecret, extraProperties)
  }

  @ToJson
  fun toJson(writer: JsonWriter, value: MicrosoftAdminConsentCredentialData?) {
    writer.beginObject()
    writer.name("client_id").value(value?.clientId)
    writer.name("client_secret").value(value?.clientSecret)
    value?.extraProperties?.forEach { (k, v) ->
      writer.name(k).value(v)
    }
    writer.endObject()
  }
}

/**
 * This class is used to serialize and deserialize the GoogleServiceAccountCredentialData object.
 * @suppress Not for public use.
 */
class GoogleServiceAccountCredentialDataAdapter {
  @FromJson
  fun fromJson(reader: JsonReader): GoogleServiceAccountCredentialData {
    var privateKeyId = ""
    var privateKey = ""
    var clientEmail = ""
    val extraProperties = mutableMapOf<String, String>()

    reader.beginObject()
    while (reader.hasNext()) {
      when (val key = reader.nextName()) {
        "private_key_id" -> privateKeyId = reader.nextString()
        "private_key" -> privateKey = reader.nextString()
        "client_email" -> clientEmail = reader.nextString()
        else -> extraProperties[key] = reader.nextString()
      }
    }
    reader.endObject()

    return GoogleServiceAccountCredentialData(privateKeyId, privateKey, clientEmail, extraProperties)
  }

  @ToJson
  fun toJson(writer: JsonWriter, value: GoogleServiceAccountCredentialData?) {
    writer.beginObject()
    writer.name("private_key_id").value(value?.privateKeyId)
    writer.name("private_key").value(value?.privateKey)
    writer.name("client_email").value(value?.clientEmail)
    value?.extraProperties?.forEach { (k, v) ->
      writer.name(k).value(v)
    }
    writer.endObject()
  }
}

/**
 * This class is used to serialize and deserialize the ConnectorOverrideCredentialData object.
 * @suppress Not for public use.
 */
class ConnectorOverrideCredentialDataAdapter {
  @FromJson
  fun fromJson(reader: JsonReader): ConnectorOverrideCredentialData {
    val extraProperties = mutableMapOf<String, String>()

    reader.beginObject()
    while (reader.hasNext()) {
      val key = reader.nextName()
      val value = reader.nextString()
      extraProperties[key] = value
    }
    reader.endObject()

    return ConnectorOverrideCredentialData(extraProperties)
  }

  @ToJson
  fun toJson(writer: JsonWriter, value: ConnectorOverrideCredentialData?) {
    writer.beginObject()
    value?.extraProperties?.forEach { (k, v) ->
      writer.name(k).value(v)
    }
    writer.endObject()
  }
}
