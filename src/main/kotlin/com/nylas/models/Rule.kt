package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas rule object.
 */
data class Rule(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String = "",
  /**
   * Name of the rule.
   */
  @Json(name = "name")
  val name: String = "",
  /**
   * Optional description of the rule.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * Evaluation order — lower numbers run first. Range 0–1000, default 10.
   */
  @Json(name = "priority")
  val priority: Int = 10,
  /**
   * Whether the rule is active.
   */
  @Json(name = "enabled")
  val enabled: Boolean = true,
  /**
   * When this rule is evaluated — on inbound mail or outbound sends.
   */
  @Json(name = "trigger")
  val trigger: RuleTrigger = RuleTrigger.INBOUND,
  /**
   * The match conditions for this rule.
   */
  @Json(name = "match")
  val match: RuleMatch = RuleMatch(),
  /**
   * The actions to perform when conditions are met.
   */
  @Json(name = "actions")
  val actions: List<RuleAction> = emptyList(),
  /**
   * The ID of the Nylas application this rule belongs to.
   */
  @Json(name = "application_id")
  val applicationId: String = "",
  /**
   * The ID of the organization this rule belongs to.
   */
  @Json(name = "organization_id")
  val organizationId: String = "",
  /**
   * Unix timestamp when the rule was created.
   */
  @Json(name = "created_at")
  val createdAt: Long = 0,
  /**
   * Unix timestamp when the rule was last updated.
   */
  @Json(name = "updated_at")
  val updatedAt: Long = 0,
)
