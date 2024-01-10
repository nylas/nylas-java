package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Nylas webhook delete response.
 */
data class WebhookDeleteResponse(
  /**
   * ID of the request.
   */
  @Json(name = "request_id")
  val requestId: String,
  /**
   * Object containing the webhook deletion status.
   */
  @Json(name = "data")
  val data: DataWebhookDeleteData? = null,
) {
  /**
   * Class representing the object enclosing the webhook deletion status.
   */
  data class DataWebhookDeleteData(
    /**
     * The status of the webhook deletion.
     */
    @Json(name = "status")
    val status: String,
  )
}
