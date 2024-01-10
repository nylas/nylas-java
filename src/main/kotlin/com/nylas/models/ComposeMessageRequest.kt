package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to compose a message.
 */
data class ComposeMessageRequest(
  /**
   * The prompt that smart compose will use to generate a message suggestion.
   */
  @Json(name = "prompt")
  val prompt: String,
)
