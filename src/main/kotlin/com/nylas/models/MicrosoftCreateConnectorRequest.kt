package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Microsoft connector creation request.
 */
sealed class MicrosoftCreateConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  override val name: String,
  /**
   * The Microsoft OAuth provider credentials and settings
   */
  @Json(name = "settings")
  val settings: MicrosoftCreateConnectorSettings,
  /**
   * The Microsoft OAuth scopes
   */
  @Json(name = "scope")
  val scope: List<String>?,
) : CreateConnectorRequest(name, AuthProvider.MICROSOFT)
