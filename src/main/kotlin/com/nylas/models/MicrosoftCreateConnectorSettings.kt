package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Microsoft connector creation request.
 */
data class MicrosoftCreateConnectorSettings(
  /**
   * The Microsoft Client ID
   */
  @Json(name = "client_id")
  val clientId: String,
  /**
   * The Microsoft Client Secret
   */
  @Json(name = "client_secret")
  val clientSecret: String,
  /**
   * The Microsoft tenant ID
   */
  @Json(name = "tenant")
  val tenant: String? = null,
)
