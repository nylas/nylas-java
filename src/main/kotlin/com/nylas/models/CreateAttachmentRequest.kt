package com.nylas.models

import com.squareup.moshi.Json
import java.io.InputStream

/**
 * Class representing a Nylas attachment object.
 */
class CreateAttachmentRequest(
  /**
   * The filename of the attachment.
   */
  @Json(name = "filename")
  val filename: String,
  /**
   * The content type of the attachment.
   */
  @Json(name = "content_type")
  val contentType: String,
  /**
   * The content of the attachment.
   */
  @Transient
  val content: InputStream,
  /**
   * The size of the attachment in bytes.
   */
  @Json(name = "size")
  val size: Int,
  /**
   * If it's an inline attachment.
   */
  @Json(name = "is_inline")
  val isInline: Boolean? = null,
  /**
   * The content ID of the attachment.
   */
  @Json(name = "content_id")
  val contentId: String? = null,
  /**
   * The content disposition if the attachment is located inline.
   */
  @Json(name = "content_disposition")
  val contentDisposition: String? = null,
) {
  /**
   * Builder for [CreateAttachmentRequest].
   * @property filename The filename of the attachment.
   * @property contentType The content type of the attachment.
   * @property size The size of the attachment in bytes.
   */
  data class Builder(
    private val filename: String,
    private val contentType: String,
    private val content: InputStream,
    private val size: Int,
  ) {
    private var isInline: Boolean? = null
    private var contentId: String? = null
    private var contentDisposition: String? = null

    /**
     * Set if the attachment is inline.
     * @param isInline If the attachment is inline.
     * @return The builder.
     */
    fun isInline(isInline: Boolean) = apply { this.isInline = isInline }

    /**
     * Set the content ID of the attachment.
     * @param contentId The content ID of the attachment.
     * @return The builder.
     */
    fun contentId(contentId: String) = apply { this.contentId = contentId }

    /**
     * Set the content disposition of the attachment.
     * @param contentDisposition The content disposition of the attachment.
     * @return The builder.
     */
    fun contentDisposition(contentDisposition: String) = apply { this.contentDisposition = contentDisposition }

    /**
     * Build the [CreateAttachmentRequest].
     * @return The [CreateAttachmentRequest].
     */
    fun build() = CreateAttachmentRequest(
      filename = filename,
      contentType = contentType,
      content = content,
      size = size,
      isInline = isInline,
      contentId = contentId,
      contentDisposition = contentDisposition,
    )
  }
}
