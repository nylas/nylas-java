package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Nylas update connector request.
 */
data class UpdateConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  val name: String?,
  /**
   * The OAuth provider credentials and settings
   */
  @Json(name = "settings")
  val settings: Map<String, Any>?,
  /**
   * The OAuth scopes
   */
  @Json(name = "scope")
  val scope: List<String>?,
)
