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
    microsoftAdapter: JsonAdapter<CredentialData.MicrosoftAdminConsent>,
    googleAdapter: JsonAdapter<CredentialData.GoogleServiceAccount>,
    overrideAdapter: JsonAdapter<CredentialData.ConnectorOverride>,
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
    microsoftAdapter: JsonAdapter<CredentialData.MicrosoftAdminConsent>,
    googleAdapter: JsonAdapter<CredentialData.GoogleServiceAccount>,
    overrideAdapter: JsonAdapter<CredentialData.ConnectorOverride>,
  ) {
    when (value) {
      is CredentialData.MicrosoftAdminConsent -> microsoftAdapter.toJson(writer, value)
      is CredentialData.GoogleServiceAccount -> googleAdapter.toJson(writer, value)
      is CredentialData.ConnectorOverride -> overrideAdapter.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}

/**
 * This class is used to serialize and deserialize the CredentialData.MicrosoftAdminConsent object.
 * @suppress Not for public use.
 */
class MicrosoftAdminConsentCredentialDataAdapter {
  @FromJson
  fun fromJson(reader: JsonReader): CredentialData.MicrosoftAdminConsent {
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

    return CredentialData.MicrosoftAdminConsent(clientId, clientSecret, extraProperties)
  }

  @ToJson
  fun toJson(writer: JsonWriter, value: CredentialData.MicrosoftAdminConsent?) {
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
 * This class is used to serialize and deserialize the CredentialData.GoogleServiceAccount object.
 * @suppress Not for public use.
 */
class GoogleServiceAccountCredentialDataAdapter {
  @FromJson
  fun fromJson(reader: JsonReader): CredentialData.GoogleServiceAccount {
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

    return CredentialData.GoogleServiceAccount(privateKeyId, privateKey, clientEmail, extraProperties)
  }

  @ToJson
  fun toJson(writer: JsonWriter, value: CredentialData.GoogleServiceAccount?) {
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
 * This class is used to serialize and deserialize the CredentialData.ConnectorOverride object.
 * @suppress Not for public use.
 */
class ConnectorOverrideCredentialDataAdapter {
  @FromJson
  fun fromJson(reader: JsonReader): CredentialData.ConnectorOverride {
    val extraProperties = mutableMapOf<String, String>()

    reader.beginObject()
    while (reader.hasNext()) {
      val key = reader.nextName()
      val value = reader.nextString()
      extraProperties[key] = value
    }
    reader.endObject()

    return CredentialData.ConnectorOverride(extraProperties)
  }

  @ToJson
  fun toJson(writer: JsonWriter, value: CredentialData.ConnectorOverride?) {
    writer.beginObject()
    value?.extraProperties?.forEach { (k, v) ->
      writer.name(k).value(v)
    }
    writer.endObject()
  }
}
