package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Google connector creation request.
 */
data class GoogleCreateConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  override val name: String,
  /**
   * The Google OAuth provider credentials and settings
   */
  @Json(name = "settings")
  val settings: GoogleCreateConnectorSettings,
  /**
   * The Google OAuth scopes
   */
  @Json(name = "scope")
  val scope: List<String>?,
) : CreateConnectorRequest(name, AuthProvider.GOOGLE)
