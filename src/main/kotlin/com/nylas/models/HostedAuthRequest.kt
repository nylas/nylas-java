package com.nylas.models

import com.squareup.moshi.Json

data class HostedAuthRequest(
  @Json(name = "provider")
  val provider: AuthProvider,
  @Json(name = "redirect_uri")
  val redirectUri: String,
  @Json(name = "scope")
  val scope: List<String>?,
  @Json(name = "state")
  val state: String?,
  @Json(name = "login_hint")
  val loginHint: String?,
  @Json(name = "cookie_nonce")
  val cookieNonce: String?,
) {
  data class Builder(
    private val provider: AuthProvider,
    private val redirectUri: String
  ) {
    private var scope: List<String>? = null
    private var state: String? = null
    private var loginHint: String? = null
    private var cookieNonce: String? = null

    fun scope(scope: List<String>) = apply { this.scope = scope }
    fun state(state: String) = apply { this.state = state }
    fun loginHint(loginHint: String) = apply { this.loginHint = loginHint }
    fun cookieNonce(cookieNonce: String) = apply { this.cookieNonce = cookieNonce }

    fun build() = HostedAuthRequest(
      provider,
      redirectUri,
      scope,
      state,
      loginHint,
      cookieNonce
    )
  }
}
