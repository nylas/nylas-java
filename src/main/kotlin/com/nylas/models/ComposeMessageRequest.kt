package com.nylas.models

/**
 * Class representing a request to compose a message.
 */
data class ComposeMessageRequest(
  /**
   * The prompt that smart compose will use to generate a message suggestion.
   */
  val prompt: String,
)
