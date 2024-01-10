package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the query parameters for finding a message.
 */
data class FindMessageQueryParams(
  /**
   * Allows you to specify to the message with headers included.
   */
  @Json(name = "fields")
  val fields: MessageFields? = null,
)
