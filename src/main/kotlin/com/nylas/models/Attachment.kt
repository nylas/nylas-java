package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas attachment object
 */
data class Attachment(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String,
  /**
   * Nylas grant ID that is now successfully created.
   */
  @Json(name = "grant_id")
  val grantId: String,
  /**
   * The size of the attachment in bytes.
   */
  @Json(name = "size")
  val size: Int,
  /**
   * The filename of the attachment.
   */
  @Json(name = "filename")
  val filename: String? = null,
  /**
   * The content type of the attachment.
   */
  @Json(name = "content_type")
  val contentType: String? = null,
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
  val contentDisposition: Long? = null,
)
