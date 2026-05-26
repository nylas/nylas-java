package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas policy object.
 */
data class Policy(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String = "",
  /**
   * Name of the policy.
   */
  @Json(name = "name")
  val name: String = "",
  /**
   * The ID of the Nylas application this policy belongs to.
   */
  @Json(name = "application_id")
  val applicationId: String = "",
  /**
   * The ID of the organization this policy belongs to.
   */
  @Json(name = "organization_id")
  val organizationId: String = "",
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
   * IDs of rules linked to this policy.
   */
  @Json(name = "rules")
  val rules: List<String>? = null,
  /**
   * Spam detection configuration.
   */
  @Json(name = "spam_detection")
  val spamDetection: PolicySpamDetection? = null,
  /**
   * Unix timestamp when the policy was created.
   */
  @Json(name = "created_at")
  val createdAt: Long = 0,
  /**
   * Unix timestamp when the policy was last updated.
   */
  @Json(name = "updated_at")
  val updatedAt: Long = 0,
)
