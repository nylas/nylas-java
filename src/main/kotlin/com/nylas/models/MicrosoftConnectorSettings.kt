package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing Microsoft credentials and settings.
 */
data class MicrosoftConnectorSettings(
  /**
   * The Microsoft tenant ID
   */
  @Json(name = "tenant")
  val tenant: String? = null,
)
