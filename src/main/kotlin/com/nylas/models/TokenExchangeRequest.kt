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
   * If not provided, the API Key will be used instead.
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
   *
   * @param redirectUri Should match the same redirect URI that was used for getting the code during the initial authorization request.
   * @param refreshToken Token to refresh/request your short-lived access token
   * @param clientId Client ID of the application.
   */
  data class Builder(
    private val redirectUri: String,
    private val refreshToken: String,
    private val clientId: String,
  ) {
    private var clientSecret: String? = null

    /**
     * Set the client secret.
     * If not provided, the API Key will be used instead.
     * @param clientSecret The client secret.
     * @return The builder.
     */
    fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }

    /**
     * Build the [TokenExchangeRequest].
     *
     * @return The [TokenExchangeRequest].
     */
    fun build() = TokenExchangeRequest(
      redirectUri = redirectUri,
      refreshToken = refreshToken,
      clientId = clientId,
      clientSecret = clientSecret,
    )
  }
}
