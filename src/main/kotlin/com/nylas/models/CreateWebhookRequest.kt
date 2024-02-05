package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas create webhook request.
 */
data class CreateWebhookRequest(
  /**
   * Select the event that triggers the webhook.
   */
  @Json(name = "trigger_types")
  val triggerTypes: List<WebhookTriggers>,
  /**
   * The url to send webhooks to.
   */
  @Json(name = "webhook_url")
  val webhookUrl: String,
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
) {
  /**
   * A builder for creating a [CreateWebhookRequest].
   * @param triggerTypes Select the event that triggers the webhook.
   * @param webhookUrl The url to send webhooks to.
   */
  data class Builder(
    private val triggerTypes: List<WebhookTriggers>,
    private val webhookUrl: String,
  ) {
    private var description: String? = null
    private var notificationEmailAddresses: List<String>? = null

    /**
     * Set a human-readable description of the webhook destination.
     * @param description A human-readable description of the webhook destination.
     * @return The builder.
     */
    fun description(description: String?) = apply { this.description = description }

    /**
     * Set the email addresses that Nylas notifies when a webhook is down for a while.
     * @param notificationEmailAddresses The email addresses that Nylas notifies when a webhook is down for a while.
     * @return The builder.
     */
    fun notificationEmailAddresses(notificationEmailAddresses: List<String>?) = apply {
      this.notificationEmailAddresses = notificationEmailAddresses
    }

    /**
     * Build the [CreateWebhookRequest].
     * @return The created [CreateWebhookRequest].
     */
    fun build() = CreateWebhookRequest(triggerTypes, webhookUrl, description, notificationEmailAddresses)
  }
}
