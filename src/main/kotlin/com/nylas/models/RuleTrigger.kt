package com.nylas.models

import com.squareup.moshi.Json

/**
 * The trigger type for a Nylas rule.
 */
enum class RuleTrigger {
  /**
   * Rule is evaluated when mail arrives.
   */
  @Json(name = "inbound")
  INBOUND,

  /**
   * Rule is evaluated before a send is submitted to the email provider.
   */
  @Json(name = "outbound")
  OUTBOUND,
}
