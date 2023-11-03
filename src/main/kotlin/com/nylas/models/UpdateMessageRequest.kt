package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to update a message.
 */
data class UpdateMessageRequest(
  /**
   * Sets the message as starred or unstarred.
   */
  @Json(name = "starred")
  val starred: Boolean? = null,
  /**
   * Sets the message as read or unread.
   */
  @Json(name = "unread")
  val unread: Boolean? = null,
  /**
   * The IDs of the folders the message should appear in.
   */
  @Json(name = "folders")
  val folders: List<String>? = null,
  /**
   * A list of key-value pairs storing additional data.
   */
  @Json(name = "metadata")
  val metadata: Map<String, Any>? = null,
) {
  /**
   * Builder for [UpdateMessageRequest].
   */
  class Builder {
    private var starred: Boolean? = null
    private var unread: Boolean? = null
    private var folders: List<String>? = null
    private var metadata: Map<String, Any>? = null

    /**
     * Set if the message is starred.
     * @param starred If the message is starred.
     * @return The builder.
     */
    fun starred(starred: Boolean) = apply { this.starred = starred }

    /**
     * Set if the message is unread.
     * @param unread If the message is unread.
     * @return The builder.
     */
    fun unread(unread: Boolean) = apply { this.unread = unread }

    /**
     * Set the folders the message should appear in.
     * @param folders The folders the message should appear in.
     * @return The builder.
     */
    fun folders(folders: List<String>) = apply { this.folders = folders }

    /**
     * Set the metadata.
     * @param metadata The metadata.
     * @return The builder.
     */
    fun metadata(metadata: Map<String, Any>) = apply { this.metadata = metadata }

    /**
     * Build the [UpdateMessageRequest].
     * @return The [UpdateMessageRequest].
     */
    fun build() = UpdateMessageRequest(
      starred = starred,
      unread = unread,
      folders = folders,
      metadata = metadata,
    )
  }
}
