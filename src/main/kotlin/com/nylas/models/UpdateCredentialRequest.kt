package com.nylas.models

/**
 * Class representing a request to update a credential
 */
data class UpdateCredentialRequest(
  /**
   * Unique name of this credential
   */
  val name: String?,
  /**
   * Data that specifies some special data required for this credential
   */
  val credentialData: CredentialData?,
) {
  /**
   * Builder for [UpdateCredentialRequest]
   */
  class Builder {
    private var name: String? = null
    private var credentialData: CredentialData? = null

    /**
     * Update the unique name of this credential.
     * @param name The unique name of this credential.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Update data that specifies some special data required for this credential.
     * @param credentialData The data that specifies some special data required for this credential.
     * @return The builder.
     */
    fun credentialData(credentialData: CredentialData) = apply { this.credentialData = credentialData }

    /**
     * Build the UpdateCredentialRequest object.
     */
    fun build() = UpdateCredentialRequest(name, credentialData)
  }
}
