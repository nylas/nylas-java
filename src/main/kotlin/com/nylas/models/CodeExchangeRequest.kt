package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas code exchange request
 */
data class CodeExchangeRequest(
  /**
   * Should match the same redirect URI that was used for getting the code during the initial authorization request.
   */
  @Json(name = "redirect_uri")
  val redirectUri: String,
  /**
   * OAuth 2.0 code fetched from the previous step.
   */
  @Json(name = "code")
  val code: String,
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
  /**
   * The original plain text code verifier (code_challenge) used in the initial authorization request (PKCE).
   */
  @Json(name = "code_verifier")
  val codeVerifier: String? = null,
) {
  /**
   * The grant type for the request. For code exchange, it should always be 'authorization_code'.
   */
  @Json(name = "grant_type")
  private var grantType: String = "authorization_code"

  /**
   * A builder for creating a [CodeExchangeRequest].
   *
   * @param redirectUri Should match the same redirect URI that was used for getting the code during the initial authorization request.
   * @param code OAuth 2.0 code fetched from the previous step.
   * @param clientId Client ID of the application.
   */
  data class Builder(
    private val redirectUri: String,
    private val code: String,
    private val clientId: String,
  ) {
    private var clientSecret: String? = null
    private var codeVerifier: String? = null

    /**
     * Set the client secret.
     * If not provided, the API Key will be used instead.
     * @param clientSecret The client secret.
     * @return The builder.
     */
    fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }

    /**
     * Set the code verifier.
     * Should be the original plain text code verifier (code_challenge) used in the initial authorization request (PKCE).
     * @param codeVerifier The code verifier.
     * @return The builder.
     */
    fun codeVerifier(codeVerifier: String) = apply { this.codeVerifier = codeVerifier }

    /**
     * Build the [CodeExchangeRequest].
     * @return The [CodeExchangeRequest].
     */
    fun build() = CodeExchangeRequest(redirectUri, code, clientId, clientSecret, codeVerifier)
  }
}
