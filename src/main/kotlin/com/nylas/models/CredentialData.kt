package com.nylas.models

import com.squareup.moshi.Json

/**
 * Sealed class representing the data needed to create a credential
 */
sealed class CredentialData(
  open val extraProperties: Map<String, String>? = emptyMap(),
) {
  /**
   * Class representing additional data needed to create a credential for Microsoft Admin Consent
   */
  data class MicrosoftAdminConsent(
    @Json(name = "client_id")
    val clientId: String,
    @Json(name = "client_secret")
    val clientSecret: String,
    override val extraProperties: Map<String, String>? = emptyMap(),
  ) : CredentialData(extraProperties)

  /**
   * Class representing additional data needed to create a credential for Google Service Account
   */
  data class GoogleServiceAccount(
    @Json(name = "private_key_id")
    val privateKeyId: String,
    @Json(name = "private_key")
    val privateKey: String,
    @Json(name = "client_email")
    val clientEmail: String,
    override val extraProperties: Map<String, String>? = emptyMap(),
  ) : CredentialData(extraProperties)

  /**
   * Class representing additional data needed to create a credential for a Connector Override
   */
  data class ConnectorOverride(
    override val extraProperties: Map<String, String>,
  ) : CredentialData(extraProperties)
}
