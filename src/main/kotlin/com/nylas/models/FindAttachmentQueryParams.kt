package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for finding an attachment.
 */
data class FindAttachmentQueryParams(
  /**
   * Message ID to find the attachment in.
   */
  @Json(name = "message_id")
  val messageId: String,
) : IQueryParams {
  /**
   * Builder for [FindEventQueryParams].
   * @property messageId Message ID to find the attachment in.
   */
  data class Builder(
    private val messageId: String,
  ) {
    /**
     * Builds a new [FindEventQueryParams] instance.
     * @return [FindEventQueryParams]
     */
    fun build() = FindEventQueryParams(
      messageId,
    )
  }
}
