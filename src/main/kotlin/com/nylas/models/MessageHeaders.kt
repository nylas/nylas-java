package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a message header.
 */
data class MessageHeaders(
  /**
   * The header name.
   */
  @Json(name = "name")
  val name: String,
  /**
   * The header value.
   */
  @Json(name = "value")
  val value: String,
)
