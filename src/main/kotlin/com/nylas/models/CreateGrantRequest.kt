package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to create a grant.
 */
data class CreateGrantRequest(
  /**
   * OAuth provider
   */
  @Json(name = "provider")
  val provider: AuthProvider,
  /**
   * Settings required by provider.
   */
  @Json(name = "settings")
  val settings: Map<String, Any>,
  /**
   * Optional state value to return to developer's website after authentication flow is completed.
   */
  @Json(name = "state")
  val state: String? = null,
  /**
   * Optional list of scopes to request. If not specified it will use the integration default scopes.
   */
  @Json(name = "scope")
  val scope: List<String>? = null,
) {
  /**
   * Builder for [CreateGrantRequest].
   * @property provider OAuth provider
   * @property settings Settings required by provider.
   */
  data class Builder(
    private val provider: AuthProvider,
    private val settings: Map<String, Any>,
  ) {
    private var state: String? = null
    private var scopes: List<String>? = null

    /**
     * Set the state value to return to developer's website after authentication flow is completed.
     * @param state State value
     * @return This builder
     */
    fun state(state: String) = apply { this.state = state }

    /**
     * Set the list of scopes to request. If not specified it will use the integration default scopes.
     * @param scopes List of scopes
     * @return This builder
     */
    fun scopes(scopes: List<String>) = apply { this.scopes = scopes }

    /**
     * Build the [CreateGrantRequest].
     * @return The built [CreateGrantRequest]
     */
    fun build() = CreateGrantRequest(provider, settings, state, scopes)
  }
}
