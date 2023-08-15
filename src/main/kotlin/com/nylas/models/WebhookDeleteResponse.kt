package com.nylas.models

/**
 * Class representing a Nylas webhook delete response.
 */
data class WebhookDeleteResponse(
  /**
   * ID of the request.
   */
  val requestId: String,
  /**
   * Object containing the webhook deletion status.
   */
  val data: DataWebhookDeleteData? = null,
) {
  /**
   * Class representing the object enclosing the webhook deletion status.
   */
  data class DataWebhookDeleteData(
    /**
     * The status of the webhook deletion.
     */
    val status: Boolean,
  )
}
