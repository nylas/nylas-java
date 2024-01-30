package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Nylas webhook.
 */
data class Webhook(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String,
  /**
   * The url to send webhooks to.
   */
  @Json(name = "webhook_url")
  val webhookUrl: String,
  /**
   * Select the event that triggers the webhook.
   */
  @Json(name = "trigger_types")
  val triggerTypes: List<WebhookTriggers>,
  /**
   * The status of the new destination.
   */
  @Json(name = "status")
  val status: WebhookStatus,
  /**
   * The time the status field was last updated, represented as a Unix timestamp in seconds.
   */
  @Json(name = "status_updated_at")
  val statusUpdatedAt: Long,
  /**
   * The time the status field was created, represented as a Unix timestamp in seconds
   */
  @Json(name = "created_at")
  val createdAt: Long,
  /**
   * The time the status field was last updated, represented as a Unix timestamp in seconds.
   */
  @Json(name = "updated_at")
  val updatedAt: Long,
  /**
   * A human-readable description of the webhook destination.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * The email addresses that Nylas notifies when a webhook is down for a while.
   */
  @Json(name = "notification_email_addresses")
  val notificationEmailAddresses: List<String>? = null,
)
