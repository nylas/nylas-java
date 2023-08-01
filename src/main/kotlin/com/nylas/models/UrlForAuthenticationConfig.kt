package com.nylas.models

import com.squareup.moshi.Json

data class UrlForAuthenticationConfig(
  @Json(name = "client_id")
  val clientId: String,
  @Json(name = "redirect_uri")
  val redirectUri: String,
  @Json(name = "access_type")
  val accessType: AccessType = AccessType.OFFLINE,
  @Json(name = "provider")
  val provider: AuthProvider?,
  @Json(name = "prompt")
  val prompt: String?,
  @Json(name = "scope")
  val scope: List<String>?,
  @Json(name = "include_grant_scopes")
  val includeGrantScopes: Boolean?,
  @Json(name = "metadata")
  val metadata: String?,
  @Json(name = "state")
  val state: String?,
  @Json(name = "login_hint")
  val loginHint: String?,
) {
  data class Builder(
    private val clientId: String,
    private val redirectUri: String,
  ) {
    private var accessType: AccessType = AccessType.OFFLINE
    private var provider: AuthProvider? = null
    private var prompt: String? = null
    private var scope: List<String>? = null
    private var includeGrantScopes: Boolean? = null
    private var metadata: String? = null
    private var state: String? = null
    private var loginHint: String? = null

    fun provider(provider: AuthProvider) = apply { this.provider = provider }
    fun accessType(accessType: AccessType) = apply { this.accessType = accessType }
    fun prompt(prompt: String) = apply { this.prompt = prompt }
    fun scope(scope: List<String>) = apply { this.scope = scope }
    fun includeGrantScopes(includeGrantScopes: Boolean) = apply { this.includeGrantScopes = includeGrantScopes }
    fun metadata(metadata: String) = apply { this.metadata = metadata }
    fun state(state: String) = apply { this.state = state }
    fun loginHint(loginHint: String) = apply { this.loginHint = loginHint }

    fun build() = UrlForAuthenticationConfig(
      clientId,
      redirectUri,
      accessType,
      provider,
      prompt,
      scope,
      includeGrantScopes,
      metadata,
      state,
      loginHint,
    )
  }
}
