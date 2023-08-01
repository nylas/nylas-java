package com.nylas.models

import com.squareup.moshi.Json

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
  var clientId: String? = null,
  /**
   * Client secret of the application.
   */
  @Json(name = "client_secret")
  var clientSecret: String? = null,
) {
  /**
   * The grant type for the request. For refreshing tokens, it should always be 'refresh_token'.
   */
  @Json(name = "grant_type")
  private var grantType: String = "refresh_token"

  /**
   * A builder for creating a [TokenExchangeRequest].
   * @param redirectUri Should match the same redirect URI that was used for getting the code during the initial authorization request.
   * @param refreshToken Token to refresh/request your short-lived access token
   */
  data class Builder(
    private val redirectUri: String,
    private val refreshToken: String,
  ) {
    private var clientId: String? = null
    private var clientSecret: String? = null

    /**
     * Set the client ID.
     * @param clientId The client ID.
     * @return The builder.
     */
    fun clientId(clientId: String) = apply { this.clientId = clientId }

    /**
     * Set the client secret.
     * @param clientSecret The client secret.
     * @return The builder.
     */
    fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }

    /**
     * Build the [TokenExchangeRequest].
     * @return The [TokenExchangeRequest].
     */
    fun build() = TokenExchangeRequest(redirectUri, refreshToken, clientId, clientSecret)
  }
}
