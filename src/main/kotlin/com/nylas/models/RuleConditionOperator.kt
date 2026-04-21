package com.nylas.models

import com.squareup.moshi.Json

/**
 * The comparison operator for a Nylas rule condition.
 */
enum class RuleConditionOperator {
  @Json(name = "is")
  IS,

  @Json(name = "is_not")
  IS_NOT,

  @Json(name = "contains")
  CONTAINS,

  /**
   * Matches against all values in a referenced List resource.
   */
  @Json(name = "in_list")
  IN_LIST,
}
