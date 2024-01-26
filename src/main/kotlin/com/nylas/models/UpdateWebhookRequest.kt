package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update webhook request.
 */
data class UpdateWebhookRequest(
  /**
   * Select the event that triggers the webhook.
   */
  @Json(name = "trigger_types")
  val triggerTypes: List<WebhookTriggers>? = null,
  /**
   * The url to send webhooks to.
   */
  @Json(name = "webhook_url")
  val webhookUrl: String? = null,
  /**
   * A human-readable description of the webhook destination.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * The email addresses that Nylas notifies when a webhook is down for a while.
   */
  @Json(name = "notification_email_address")
  val notificationEmailAddress: String? = null,
) {
  /**
   * A builder for creating a [UpdateWebhookRequest].
   */
  class Builder {
    private var triggerTypes: List<WebhookTriggers>? = null
    private var webhookUrl: String? = null
    private var description: String? = null
    private var notificationEmailAddress: String? = null

    /**
     * Set the event that triggers the webhook.
     * @param triggerTypes Select the event that triggers the webhook.
     * @return The builder.
     */
    fun triggerTypes(triggerTypes: List<WebhookTriggers>?) = apply { this.triggerTypes = triggerTypes }

    /**
     * Set the url to send webhooks to.
     * @param webhookUrl The url to send webhooks to.
     * @return The builder.
     */
    fun webhookUrl(webhookUrl: String?) = apply { this.webhookUrl = webhookUrl }

    /**
     * Set a human-readable description of the webhook destination.
     * @param description A human-readable description of the webhook destination.
     * @return The builder.
     */
    fun description(description: String?) = apply { this.description = description }

    /**
     * Set the email addresses that Nylas notifies when a webhook is down for a while.
     * @param notificationEmailAddress The email addresses that Nylas notifies when a webhook is down for a while.
     * @return The builder.
     */
    fun notificationEmailAddress(notificationEmailAddress: String?) = apply { this.notificationEmailAddress = notificationEmailAddress }

    /**
     * Build the [UpdateWebhookRequest].
     * @return The created [UpdateWebhookRequest].
     */
    fun build() = UpdateWebhookRequest(triggerTypes, webhookUrl, description, notificationEmailAddress)
  }
}
