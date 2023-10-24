package com.nylas.models

import com.squareup.moshi.Json

/**
 * This sealed class represents the base Nylas connector creation request.
 */
sealed class CreateConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  open val name: String,
  /**
   * The provider type
   */
  @Json(name = "provider")
  val provider: AuthProvider,
)
