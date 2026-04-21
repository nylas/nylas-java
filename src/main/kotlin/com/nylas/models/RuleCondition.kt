package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a single condition in a Nylas rule match.
 */
data class RuleCondition(
  /**
   * The field to evaluate.
   * Inbound: from.address, from.domain, from.tld.
   * Outbound: recipient.address, recipient.domain, recipient.tld, outbound.type.
   */
  @Json(name = "field")
  val field: String = "",
  /**
   * The comparison operator.
   */
  @Json(name = "operator")
  val operator: RuleConditionOperator = RuleConditionOperator.IS,
  /**
   * The value to compare against. For [RuleConditionOperator.IN_LIST], this is the List resource ID.
   */
  @Json(name = "value")
  val value: String = "",
)
