package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Nylas Grant object.
 */
data class Grant(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String,
  /**
   * OAuth provider that the user authenticated with.
   */
  @Json(name = "provider")
  val provider: String,
  /**
   * Scopes specified for the grant.
   */
  @Json(name = "scope")
  val scope: List<String>,
  /**
   * Unix timestamp when the grant was created.
   */
  @Json(name = "created_at")
  val createdAt: Long,
  /**
   * Status of the grant, if it is still valid or if the user needs to re-authenticate.
   */
  @Json(name = "grant_status")
  val grantStatus: String?,
  /**
   * Email address associated with the grant.
   */
  @Json(name = "email")
  val email: String?,
  /**
   * End user's client user agent.
   */
  @Json(name = "user_agent")
  val userAgent: String?,
  /**
   * End user's client IP address.
   */
  @Json(name = "ip")
  val ip: String?,
  /**
   * Initial state that was sent as part of the OAuth request.
   */
  @Json(name = "state")
  val state: String?,
  /**
   * Unix timestamp when the grant was updated.
   */
  @Json(name = "updated_at")
  val updatedAt: Long?,
  /**
   * Provider's ID for the user this grant is associated with.
   */
  @Json(name = "provider_user_id")
  val providerUserId: String?,

  /**
   * Settings required by the provider that were sent as part of the OAuth request.
   */
  @Json(name = "settings")
  val settings: Map<String, Any>?,
)
