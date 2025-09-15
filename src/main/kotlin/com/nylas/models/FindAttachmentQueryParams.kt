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
  /**
   * Specify fields that you want Nylas to return, as a comma-separated list (for example, select=id,updated_at).
   * This allows you to receive only the portion of object data that you're interested in.
   * You can use select to optimize response size and reduce latency by limiting queries to only the information that you need
   */
  @Json(name = "select")
  var select: String? = null,
) : IQueryParams {
  /**
   * Builder for [FindAttachmentQueryParams].
   * @property messageId Message ID to find the attachment in.
   */
  data class Builder(
    private val messageId: String,
  ) {
    private var select: String? = null

    /**
     * Sets the fields to select.
     * @param select The fields to select.
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds a new [FindAttachmentQueryParams] instance.
     * @return [FindAttachmentQueryParams]
     */
    fun build() = FindAttachmentQueryParams(
      messageId,
      select,
    )
  }
}
