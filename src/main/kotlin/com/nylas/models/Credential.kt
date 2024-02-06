package com.nylas.models

import com.squareup.moshi.Json

data class Credential(
  /**
   * Globally unique object identifier
   */
  @Json(name = "id")
  val id: String,
  /**
   * Name of the credential
   */
  @Json(name = "name")
  val name: String,
  /**
   * The type of credential
   */
  @Json(name = "credential_type")
  val credentialType: CredentialType? = null,
  /**
   * Hashed value of the credential that you created
   */
  @Json(name = "hashed_data")
  val hashedData: String? = null,
  /**
   * Timestamp of when the credential was created
   */
  @Json(name = "created_at")
  val createdAt: Long? = null,
  /**
   * Timestamp of when the credential was updated
   */
  @Json(name = "updated_at")
  val updatedAt: Long? = null,
)
