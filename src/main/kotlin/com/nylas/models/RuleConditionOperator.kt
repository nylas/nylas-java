package com.nylas.models

import com.squareup.moshi.Json

/**
 * The comparison operator for a Nylas rule condition.
 */
enum class RuleConditionOperator {
  /**
   * The field value must exactly match the given value.
   */
  @Json(name = "is")
  IS,

  /**
   * The field value must not match the given value.
   */
  @Json(name = "is_not")
  IS_NOT,

  /**
   * The field value must contain the given value as a substring.
   */
  @Json(name = "contains")
  CONTAINS,

  /**
   * Matches against all values in a referenced List resource.
   */
  @Json(name = "in_list")
  IN_LIST,
}
