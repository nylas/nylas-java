package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update policy request.
 */
data class UpdatePolicyRequest(
  /**
   * Name of the policy.
   */
  @Json(name = "name")
  val name: String? = null,
  /**
   * Optional mailbox and behavior settings.
   */
  @Json(name = "options")
  val options: PolicyOptions? = null,
  /**
   * Resource and rate limits for agent accounts using this policy.
   */
  @Json(name = "limits")
  val limits: PolicyLimits? = null,
  /**
   * IDs of rules to link to this policy.
   */
  @Json(name = "rules")
  val rules: List<String>? = null,
  /**
   * Spam detection configuration.
   */
  @Json(name = "spam_detection")
  val spamDetection: PolicySpamDetection? = null,
) {
  /**
   * Builder for [UpdatePolicyRequest].
   */
  class Builder {
    private var name: String? = null
    private var options: PolicyOptions? = null
    private var limits: PolicyLimits? = null
    private var rules: List<String>? = null
    private var spamDetection: PolicySpamDetection? = null

    /**
     * Set the name of the policy.
     * @param name Name of the policy.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Set the mailbox and behavior settings.
     * @param options Optional mailbox and behavior settings.
     * @return The builder.
     */
    fun options(options: PolicyOptions) = apply { this.options = options }

    /**
     * Set the resource and rate limits.
     * @param limits Resource and rate limits for agent accounts using this policy.
     * @return The builder.
     */
    fun limits(limits: PolicyLimits) = apply { this.limits = limits }

    /**
     * Set the IDs of rules to link to this policy.
     * @param rules IDs of rules to link to this policy.
     * @return The builder.
     */
    fun rules(rules: List<String>) = apply { this.rules = rules }

    /**
     * Set the spam detection configuration.
     * @param spamDetection Spam detection configuration.
     * @return The builder.
     */
    fun spamDetection(spamDetection: PolicySpamDetection) = apply { this.spamDetection = spamDetection }

    /**
     * Build the [UpdatePolicyRequest].
     * @return An [UpdatePolicyRequest] with the provided values.
     */
    fun build() = UpdatePolicyRequest(name, options, limits, rules, spamDetection)
  }
}
