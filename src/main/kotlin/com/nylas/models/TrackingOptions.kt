package com.nylas.models

/**
 * Class representing the different tracking options for when a message is sent.
 */
data class TrackingOptions(
  /**
   * The label to apply to tracked messages.
   */
  val label: String? = null,
  /**
   * Whether to track links.
   */
  val links: Boolean? = null,
  /**
   * Whether to track opens.
   */
  val opens: Boolean? = null,
  /**
   * Whether to track thread replies.
   */
  val threadReplies: Boolean? = null,
)
