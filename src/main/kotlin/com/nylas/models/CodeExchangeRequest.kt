package com.nylas.models

import com.squareup.moshi.Json

data class CodeExchangeRequest(
  @Json(name = "redirect_uri")
  val redirectUri: String,
  @Json(name = "code")
  val code: String,
  @Json(name = "code_verifier")
  val codeVerifier: String? = null,
  @Json(name = "client_id")
  var clientId: String? = null,
  @Json(name = "client_secret")
  var clientSecret: String? = null,
) {
  @Json(name = "grant_type")
  private val grantType = "authorization_code"
    // builder
    data class Builder(
        private val redirectUri: String,
        private val code: String,
    ) {
      private var codeVerifier: String? = null
      private var clientId: String? = null
      private var clientSecret: String? = null

      fun codeVerifier(codeVerifier: String) = apply { this.codeVerifier = codeVerifier }
      fun clientId(clientId: String) = apply { this.clientId = clientId }
      fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }

      fun build() = CodeExchangeRequest(redirectUri, code, clientId, clientSecret, codeVerifier)
    }
}
