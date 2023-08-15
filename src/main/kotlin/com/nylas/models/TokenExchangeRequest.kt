package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas token exchange request
 */
data class TokenExchangeRequest(
  /**
   * Should match the same redirect URI that was used for getting the code during the initial authorization request.
   */
  @Json(name = "redirect_uri")
  val redirectUri: String,
  /**
   * Token to refresh/request your short-lived access token
   */
  @Json(name = "refresh_token")
  val refreshToken: String,
  /**
   * Client ID of the application.
   */
  @Json(name = "client_id")
  var clientId: String,
  /**
   * Client secret of the application.
   */
  @Json(name = "client_secret")
  var clientSecret: String,
) {
  /**
   * The grant type for the request. For refreshing tokens, it should always be 'refresh_token'.
   */
  @Json(name = "grant_type")
  private var grantType: String = "refresh_token"
}
