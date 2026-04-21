package com.nylas.models

import com.squareup.moshi.Json

/**
 * The logical operator used to combine conditions in a Nylas rule match.
 */
enum class RuleMatchOperator {
  /**
   * At least one condition must match.
   */
  @Json(name = "any")
  ANY,

  /**
   * All conditions must match.
   */
  @Json(name = "all")
  ALL,
}
