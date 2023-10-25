package com.nylas.models

import com.squareup.moshi.Json

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
    override val credentialData: MicrosoftAdminConsentCredentialData,
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
    override val credentialData: GoogleServiceAccountCredentialData,
  ) : CreateCredentialRequest(name, credentialData, CredentialType.SERVICEACCOUNT)

  /**
   * Class representing a request to create an override credential
   */
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
    override val credentialData: ConnectorOverrideCredentialData,
  ) : CreateCredentialRequest(name, credentialData, CredentialType.CONNECTOR)
}
