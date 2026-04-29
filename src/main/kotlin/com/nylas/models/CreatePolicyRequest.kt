package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas create policy request.
 */
data class CreatePolicyRequest(
  /**
   * Name of the policy.
   */
  @Json(name = "name")
  val name: String,
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
   * Builder for [CreatePolicyRequest].
   * @param name Name of the policy.
   */
  data class Builder(
    private val name: String,
  ) {
    private var options: PolicyOptions? = null
    private var limits: PolicyLimits? = null
    private var rules: List<String>? = null
    private var spamDetection: PolicySpamDetection? = null

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
     * Build the [CreatePolicyRequest].
     * @return A [CreatePolicyRequest] with the provided values.
     */
    fun build() = CreatePolicyRequest(name, options, limits, rules, spamDetection)
  }
}
