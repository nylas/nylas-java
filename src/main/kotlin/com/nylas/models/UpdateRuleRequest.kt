package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update rule request.
 */
data class UpdateRuleRequest(
  /**
   * Name of the rule.
   */
  @Json(name = "name")
  val name: String? = null,
  /**
   * When this rule is evaluated.
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
  val actions: List<RuleAction>? = null,
  /**
   * Optional description of the rule.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * Evaluation order — lower numbers run first. Range 0–1000.
   */
  @Json(name = "priority")
  val priority: Int? = null,
  /**
   * Whether the rule is active.
   */
  @Json(name = "enabled")
  val enabled: Boolean? = null,
) {
  /**
   * Builder for [UpdateRuleRequest].
   */
  class Builder {
    private var name: String? = null
    private var trigger: RuleTrigger? = null
    private var match: RuleMatch? = null
    private var actions: List<RuleAction>? = null
    private var description: String? = null
    private var priority: Int? = null
    private var enabled: Boolean? = null

    /**
     * Set the name of the rule.
     * @param name Name of the rule.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Set when the rule is evaluated.
     * @param trigger When this rule is evaluated.
     * @return The builder.
     */
    fun trigger(trigger: RuleTrigger) = apply { this.trigger = trigger }

    /**
     * Set the match conditions.
     * @param match The match conditions for this rule.
     * @return The builder.
     */
    fun match(match: RuleMatch) = apply { this.match = match }

    /**
     * Set the actions to perform.
     * @param actions The actions to perform when conditions are met.
     * @return The builder.
     */
    fun actions(actions: List<RuleAction>) = apply { this.actions = actions }

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
     * Build the [UpdateRuleRequest].
     * @return An [UpdateRuleRequest] with the provided values.
     */
    fun build() = UpdateRuleRequest(name, trigger, match, actions, description, priority, enabled)
  }
}
