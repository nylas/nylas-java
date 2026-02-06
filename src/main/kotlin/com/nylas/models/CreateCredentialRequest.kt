package com.nylas.models

import com.squareup.moshi.Json
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

/**
 * Class representing a request to create a credential
 */
sealed class CreateCredentialRequest(
  /**
   * Unique name of this credential
   */
  @Json(name = "name")
  open val name: String,
  /**
   * Data that specifies some special data required for this credential
   */
  @Json(name = "credential_data")
  open val credentialData: CredentialData,
  /**
   * Type of credential for the admin consent flow
   */
  @Json(name = "credential_type")
  val credentialType: CredentialType,
) {
  /**
   * Class representing a request to create a Microsoft credential
   */
  data class Microsoft(
    /**
     * Unique name of this credential
     */
    @Json(name = "name")
    override val name: String,
    /**
     * Data that specifies some special data required for a Microsoft credential
     */
    @Json(name = "credential_data")
    override val credentialData: CredentialData.MicrosoftAdminConsent,
  ) : CreateCredentialRequest(name, credentialData, CredentialType.ADMINCONSENT)

  /**
   * Class representing a request to create a Google credential
   */
  data class Google(
    /**
     * Unique name of this credential
     */
    @Json(name = "name")
    override val name: String,
    /**
     * Data that specifies some special data required for a Google credential
     */
    @Json(name = "credential_data")
    override val credentialData: CredentialData.GoogleServiceAccount,
  ) : CreateCredentialRequest(name, credentialData, CredentialType.SERVICEACCOUNT)

  /**
   * Class representing a request to create a connector credential.
   * For multi-credential OAuth flows, provide clientId and clientSecret in credentialData.
   * For other overrides, use extraProperties in credentialData.
   */
  data class Connector(
    /**
     * Unique name of this credential
     */
    @Json(name = "name")
    override val name: String,
    /**
     * Data that specifies the credential details (client_id/client_secret for OAuth, or extraProperties for overrides)
     */
    @Json(name = "credential_data")
    override val credentialData: CredentialData.ConnectorOverride,
  ) : CreateCredentialRequest(name, credentialData, CredentialType.CONNECTOR)

  /**
   * Alias for [Connector] to maintain backward compatibility.
   * @deprecated Use [Connector] instead.
   */
  @Deprecated("Use Connector instead", ReplaceWith("Connector"))
  data class Override(
    /**
     * Unique name of this credential
     */
    @Json(name = "name")
    override val name: String,
    /**
     * Data that specifies some special data required for an override credential
     */
    @Json(name = "credential_data")
    override val credentialData: CredentialData.ConnectorOverride,
  ) : CreateCredentialRequest(name, credentialData, CredentialType.CONNECTOR)

  companion object {
    @JvmStatic
    val CREATE_CREDENTIAL_JSON_ADAPTER_FACTORY: PolymorphicJsonAdapterFactory<CreateCredentialRequest> =
      PolymorphicJsonAdapterFactory.of(CreateCredentialRequest::class.java, "credential_type")
        .withSubtype(Microsoft::class.java, CredentialType.ADMINCONSENT.value)
        .withSubtype(Google::class.java, CredentialType.SERVICEACCOUNT.value)
        .withSubtype(Connector::class.java, CredentialType.CONNECTOR.value)
  }
}
