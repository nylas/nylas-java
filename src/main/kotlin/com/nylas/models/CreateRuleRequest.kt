package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas create rule request.
 */
data class CreateRuleRequest(
  /**
   * Name of the rule.
   */
  @Json(name = "name")
  val name: String,
  /**
   * When this rule is evaluated.
   */
  @Json(name = "trigger")
  val trigger: RuleTrigger,
  /**
   * The match conditions for this rule.
   */
  @Json(name = "match")
  val match: RuleMatch,
  /**
   * The actions to perform when conditions are met.
   */
  @Json(name = "actions")
  val actions: List<RuleAction>,
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
   * Whether the rule is active. Defaults to true.
   */
  @Json(name = "enabled")
  val enabled: Boolean? = null,
) {
  /**
   * Builder for [CreateRuleRequest].
   * @param name Name of the rule.
   * @param trigger When this rule is evaluated.
   * @param match The match conditions.
   * @param actions The actions to perform.
   */
  data class Builder(
    private val name: String,
    private val trigger: RuleTrigger,
    private val match: RuleMatch,
    private val actions: List<RuleAction>,
  ) {
    private var description: String? = null
    private var priority: Int? = null
    private var enabled: Boolean? = null

    /**
     * Set the description of the rule.
     * @param description Optional description of the rule.
     * @return The builder.
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * Set the evaluation priority.
     * @param priority Evaluation order — lower numbers run first. Range 0–1000.
     * @return The builder.
     */
    fun priority(priority: Int) = apply { this.priority = priority }

    /**
     * Set whether the rule is active.
     * @param enabled Whether the rule is active.
     * @return The builder.
     */
    fun enabled(enabled: Boolean) = apply { this.enabled = enabled }

    /**
     * Build the [CreateRuleRequest].
     * @return A [CreateRuleRequest] with the provided values.
     */
    fun build() = CreateRuleRequest(name, trigger, match, actions, description, priority, enabled)
  }
}
