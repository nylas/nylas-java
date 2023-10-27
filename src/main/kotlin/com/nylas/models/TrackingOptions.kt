package com.nylas.models

/**
 * Class representing the different tracking options for when a message is sent.
 */
data class TrackingOptions(
  val label: String?,
  val links: String?,
  val opens: String?,
  val threadReplies: String?,
)
