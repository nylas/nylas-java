package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the different tracking options for when a message is sent.
 */
data class TrackingOptions(
  /**
   * The label to apply to tracked messages.
   */
  @Json(name = "label")
  val label: String? = null,
  /**
   * Whether to track links.
   */
  @Json(name = "links")
  val links: Boolean? = null,
  /**
   * Whether to track opens.
   */
  @Json(name = "opens")
  val opens: Boolean? = null,
  /**
   * Whether to track thread replies.
   */
  @Json(name = "thread_replies")
  val threadReplies: Boolean? = null,
)
