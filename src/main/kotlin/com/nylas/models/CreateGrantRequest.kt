package com.nylas.models

import com.squareup.moshi.Json

data class CreateGrantRequest(
  @Json(name = "provider")
  val provider: AuthProvider,
  @Json(name = "settings")
  val settings: Map<String, Any>,
  @Json(name = "state")
  val state: String?,
  @Json(name = "scopes")
  val scopes: List<String>?,
) {
  data class Builder(
    private val provider: AuthProvider,
    private val settings: Map<String, Any>,
  ) {
    private var state: String? = null
    private var scopes: List<String>? = null

    fun state(state: String) = apply { this.state = state }
    fun scopes(scopes: List<String>) = apply { this.scopes = scopes }

    fun build() = CreateGrantRequest(provider, settings, state, scopes)
  }
}
