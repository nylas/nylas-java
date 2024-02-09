package com.nylas.models

import com.squareup.moshi.Json

/**
 * A request to query the information of a token.
 * @suppress Used internally by the SDK
 */
data class TokenInfoRequest(
  /**
   * The ID token to query.
   */
  @Json(name = "id_token")
  val idToken: String? = null,
  /**
   * The access token to query.
   */
  @Json(name = "access_token")
  val accessToken: String? = null,
) : IQueryParams
