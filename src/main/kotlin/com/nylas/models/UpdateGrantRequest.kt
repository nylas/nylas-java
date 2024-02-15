package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update grant request
 */
data class UpdateGrantRequest(
  /**
   * Settings required by provider.
   */
  @Json(name = "settings")
  val settings: Map<String, Any>? = null,
  /**
   * List of integration scopes for the grant.
   */
  @Json(name = "scope")
  val scope: List<String>? = null,
) {
  /**
   * Builder for [UpdateGrantRequest].
   */
  class Builder {
    private var settings: Map<String, Any>? = null
    private var scope: List<String>? = null

    /**
     * Update the settings required by provider.
     * @param settings Settings required by provider.
     * @return This builder
     */
    fun settings(settings: Map<String, Any>) = apply { this.settings = settings }

    /**
     * Update the list of integration scopes for the grant.
     * @param scopes List of scopes
     * @return This builder
     */
    fun scopes(scopes: List<String>) = apply { this.scope = scopes }

    /**
     * Build the [UpdateGrantRequest].
     * @return The built [UpdateGrantRequest]
     */
    fun build() = UpdateGrantRequest(settings, scope)
  }
}
