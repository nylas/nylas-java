package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas code exchange response
 */
data class CodeExchangeResponse(
  /**
   * Supports exchanging the code for tokens, or refreshing an access token using [Auth.refreshAccessToken][com.nylas.resources.Auth.refreshAccessToken].
   */
  @Json(name = "access_token")
  val accessToken: String,
  /**
   * Nylas grant ID that is now successfully created.
   */
  @Json(name = "grant_id")
  val grantId: String,
  /**
   * The remaining lifetime of the access token in seconds.
   */
  @Json(name = "expires_in")
  val expiresIn: Int,
  /**
   * List of scopes associated with this token.
   */
  @Json(name = "scope")
  val scope: String,
  /**
   * Only returned if the code was requested using [AccessType.OFFLINE][com.nylas.models.AccessType.OFFLINE].
   */
  @Json(name = "refresh_token")
  val refreshToken: String? = null,
  /**
   *
   * A JWT that contains identity information about the user that is digitally signed by Nylas.
   */
  @Json(name = "id_token")
  val idToken: String? = null,
  /**
   * Currently always Bearer.
   */
  @Json(name = "token_type")
  val tokenType: String? = "Bearer",
)
