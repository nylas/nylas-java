package com.nylas.models

import com.squareup.moshi.Json
import java.io.InputStream

/**
 * Class representing a Nylas file object.
 */
class CreateFileRequest(
  /**
   * The filename of the file.
   */
  @Json(name = "filename")
  val filename: String,
  /**
   * The content type of the file.
   */
  @Json(name = "content_type")
  val contentType: String,
  /**
   * The content of the file.
   */
  @Json(name = "content")
  val content: InputStream,
  /**
   * The size of the file in bytes.
   */
  @Json(name = "size")
  val size: Int,
  /**
   * If it's an inline attachment.
   */
  @Json(name = "is_inline")
  val isInline: Boolean? = null,
  /**
   * The content ID of the file.
   */
  @Json(name = "content_id")
  val contentId: String? = null,
  /**
   * The content disposition if the file is located inline.
   */
  @Json(name = "content_disposition")
  val contentDisposition: Long? = null,
) {
  /**
   * Builder for [CreateFileRequest].
   * @property filename The filename of the file.
   * @property contentType The content type of the file.
   * @property size The size of the file in bytes.
   */
  data class Builder(
    private val filename: String,
    private val contentType: String,
    private val content: InputStream,
    private val size: Int,
  ) {
    private var isInline: Boolean? = null
    private var contentId: String? = null
    private var contentDisposition: Long? = null

    /**
     * Set if the file is inline.
     * @param isInline If the file is inline.
     * @return The builder.
     */
    fun isInline(isInline: Boolean) = apply { this.isInline = isInline }

    /**
     * Set the content ID of the file.
     * @param contentId The content ID of the file.
     * @return The builder.
     */
    fun contentId(contentId: String) = apply { this.contentId = contentId }

    /**
     * Set the content disposition of the file.
     * @param contentDisposition The content disposition of the file.
     * @return The builder.
     */
    fun contentDisposition(contentDisposition: Long) = apply { this.contentDisposition = contentDisposition }

    /**
     * Build the [CreateFileRequest].
     * @return The [CreateFileRequest].
     */
    fun build() = CreateFileRequest(
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
