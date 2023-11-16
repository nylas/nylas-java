package com.nylas.models

/**
 * Class representing a response to a message composition request.
 */
data class ComposeMessageResponse(
  /**
   * The message suggestion generated by smart compose.
   */
  val suggestion: String,
)