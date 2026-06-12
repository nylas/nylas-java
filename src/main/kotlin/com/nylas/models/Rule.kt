package com.nylas.models

import com.nylas.util.JsonHelper
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
  val priority: Int? = null,
  /**
   * Whether the rule is active.
   */
  @Json(name = "enabled")
  val enabled: Boolean? = null,
  /**
   * When this rule is evaluated — on inbound mail or outbound sends.
   */
  @Json(name = "trigger")
  val trigger: RuleTrigger? = null,
  /**
   * The match conditions for this rule.
   */
  @Json(name = "match")
  val match: RuleMatch? = null,
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

/**
 * Class representation of the nested list envelope returned by GET /v3/rules.
 */
data class RulesListResponse(
  /**
   * Rules list payload. The API has returned both a standard list array and
   * a nested object with `items` and `next_cursor`; normalize either shape.
   */
  @Json(name = "data")
  val data: Any? = emptyList<Any>(),
  /**
   * The request ID.
   */
  @Json(name = "request_id")
  val requestId: String = "",
  /**
   * The cursor to use to get the next page of rules when returned at the top level.
   */
  @Json(name = "next_cursor")
  val nextCursor: String? = null,
) {
  /**
   * Convert the rules list envelope into the SDK's standard list response.
   */
  fun toListResponse(): ListResponse<Rule> {
    val nestedData = data as? Map<*, *>
    val rawItems = when (data) {
      is List<*> -> data
      is Map<*, *> -> data["items"] as? List<*> ?: emptyList<Any>()
      else -> emptyList<Any>()
    }
    val rules = rawItems.mapNotNull { ruleAdapter.fromJsonValue(it) }
    return ListResponse(
      data = rules,
      requestId = requestId,
      nextCursor = nextCursor ?: (nestedData?.get("next_cursor") as? String),
    )
  }

  companion object {
    private val ruleAdapter = JsonHelper.moshi().adapter(Rule::class.java)
  }
}
