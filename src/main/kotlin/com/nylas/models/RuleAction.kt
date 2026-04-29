package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of an action in a Nylas rule.
 */
data class RuleAction(
  /**
   * The action to perform.
   */
  @Json(name = "type")
  val type: RuleActionType = RuleActionType.BLOCK,
  /**
   * Required when type is [RuleActionType.ASSIGN_TO_FOLDER] — the folder ID to assign to.
   */
  @Json(name = "value")
  val value: String? = null,
)
