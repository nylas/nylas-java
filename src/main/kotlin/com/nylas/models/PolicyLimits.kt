package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas policy limits object.
 */
data class PolicyLimits(
  /**
   * Maximum size in bytes for a single attachment.
   */
  @Json(name = "limit_attachment_size_limit")
  val attachmentSizeLimit: Long? = null,
  /**
   * Maximum number of attachments per message.
   */
  @Json(name = "limit_attachment_count_limit")
  val attachmentCountLimit: Int? = null,
  /**
   * Allowed MIME types for attachments.
   */
  @Json(name = "limit_attachment_allowed_types")
  val attachmentAllowedTypes: List<String>? = null,
  /**
   * Maximum total MIME size in bytes per message.
   */
  @Json(name = "limit_size_total_mime")
  val sizeTotalMime: Long? = null,
  /**
   * Maximum total storage in bytes for the agent account.
   */
  @Json(name = "limit_storage_total")
  val storageTotal: Long? = null,
  /**
   * Maximum number of messages the agent account can send per day.
   */
  @Json(name = "limit_count_daily_message_per_grant")
  val countDailyMessagePerGrant: Int? = null,
  /**
   * Number of days to retain messages in the inbox.
   */
  @Json(name = "limit_inbox_retention_period")
  val inboxRetentionPeriod: Int? = null,
  /**
   * Number of days to retain messages in the spam folder.
   */
  @Json(name = "limit_spam_retention_period")
  val spamRetentionPeriod: Int? = null,
)
