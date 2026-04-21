package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the match block in a Nylas rule.
 */
data class RuleMatch(
  /**
   * Logical operator used to combine conditions.
   */
  @Json(name = "operator")
  val operator: RuleMatchOperator = RuleMatchOperator.ANY,
  /**
   * The list of conditions to evaluate.
   */
  @Json(name = "conditions")
  val conditions: List<RuleCondition> = emptyList(),
)
