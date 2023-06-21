package com.nylas.models

import com.squareup.moshi.Json

data class HostedAuthResponse(
  @Json(name = "url")
  val url: String,
  @Json(name = "id")
  val id: String,
  @Json(name = "expires_at")
  val expiresAt: Long,
  @Json(name = "request")
  val request: Request
) {
  data class Request(
    @Json(name = "redirect_uri")
    val redirectUri: String,
    @Json(name = "provider")
    val provider: AuthProvider,
    @Json(name = "access_type")
    val scope: List<String>?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "login_hint")
    val loginHint: String?,
    @Json(name = "prompt")
    val prompt: String?,
    @Json(name = "include_granted_scopes")
    val includeGrantedScopes: Boolean?
  )
}
