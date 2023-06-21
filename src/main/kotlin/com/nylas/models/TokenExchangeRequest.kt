package com.nylas.models

import com.squareup.moshi.Json

data class TokenExchangeRequest(
  @Json(name = "redirect_uri")
  val redirectUri: String,
  @Json(name = "refresh_token")
  val refreshToken: String,
  @Json(name = "client_id")
  var clientId: String? = null,
  @Json(name = "client_secret")
  var clientSecret: String? = null,
) {
  @Json(name = "grant_type")
  private val grantType = "refresh_token"

  // builder
  data class Builder(
    private val redirectUri: String,
    private val code: String,
  ) {
    private var clientId: String? = null
    private var clientSecret: String? = null

    fun clientId(clientId: String) = apply { this.clientId = clientId }
    fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }

    fun build() = TokenExchangeRequest(redirectUri, code, clientId, clientSecret)
  }
}
