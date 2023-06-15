package com.nylas.models

import com.squareup.moshi.Json

data class ExchangeCodeRequest(
  @Json(name = "redirect_uri")
  val redirectUri: String,
  @Json(name = "code")
  val code: String,
  @Json(name = "client_id")
  val clientId: String,
  @Json(name = "client_secret")
  val clientSecret: String,
  @Json(name = "code_verifier")
  val codeVerifier: String? = null,
) {
  @Json(name = "grant_type")
  private val grantType = "authorization_code"
    // builder
    data class Builder(
        private val redirectUri: String,
        private val code: String,
        private val clientId: String,
        private val clientSecret: String,
    ) {
        private var codeVerifier: String? = null

        fun codeVerifier(codeVerifier: String) = apply { this.codeVerifier = codeVerifier }

        fun build() = ExchangeCodeRequest(redirectUri, code, clientId, clientSecret, codeVerifier)
    }
}
