package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the Nylas connector response.
 */
data class Connector(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  val name: String,
  /**
   * The provider type
   */
  @Json(name = "provider")
  val provider: AuthProvider,
  /**
   * Optional settings from provider
   */
  @Json(name = "settings")
  val settings: Map<String, Any>? = null,
  /**
   * Default scopes for the connector
   */
  @Json(name = "scope")
  val scope: List<String>? = null,
)
