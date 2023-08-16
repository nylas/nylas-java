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
  @Json(name = "callback_url")
  val callbackUrl: String,
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
   * A builder for creating a [CreateWebhookRequest].
   * @param triggerTypes Select the event that triggers the webhook.
   * @param callbackUrl The url to send webhooks to.
   */
  data class Builder(
    private val triggerTypes: List<WebhookTriggers>,
    private val callbackUrl: String,
  ) {
    private var description: String? = null
    private var notificationEmailAddress: String? = null

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
     * Build the [CreateWebhookRequest].
     * @return The created [CreateWebhookRequest].
     */
    fun build() = CreateWebhookRequest(triggerTypes, callbackUrl, description, notificationEmailAddress)
  }
}
