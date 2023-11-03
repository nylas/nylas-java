package com.nylas.models

/**
 * Class representing the different tracking options for when a message is sent.
 */
data class TrackingOptions(
  val label: String? = null,
  val links: String? = null,
  val opens: String? = null,
  val threadReplies: String? = null,
)
