package com.nylas.models

import com.squareup.moshi.Json

data class UpdateGrantRequest(
  @Json(name = "settings")
  val settings: Map<String, Any>?,
  @Json(name = "scopes")
  val scopes: List<String>?,
) {
  class Builder {
    private var settings: Map<String, Any>? = null
    private var scopes: List<String>? = null

    fun settings(settings: Map<String, Any>) = apply { this.settings = settings }
    fun scopes(scopes: List<String>) = apply { this.scopes = scopes }

    fun build() = UpdateGrantRequest(settings, scopes)
  }
}
