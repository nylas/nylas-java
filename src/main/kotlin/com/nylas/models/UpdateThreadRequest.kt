package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to update a thread.
 */
data class UpdateThreadRequest(
  /**
   * Sets all messages in the thread as starred or unstarred.
   */
  @Json(name = "starred")
  val starred: Boolean? = null,
  /**
   * Sets all messages in the thread as read or unread.
   */
  @Json(name = "unread")
  val unread: Boolean? = null,
  /**
   * The IDs of the folders to apply, overwriting all previous folders for all messages in the thread.
   */
  @Json(name = "folders")
  val folders: List<String>? = null,
) {
  /**
   * Builder for [UpdateThreadRequest].
   */
  class Builder {
    private var starred: Boolean? = null
    private var unread: Boolean? = null
    private var folders: List<String>? = null
    private var metadata: Map<String, Any>? = null

    /**
     * Sets if all messages in the thread as starred or unstarred.
     * @param starred If the thread is starred.
     * @return The builder.
     */
    fun starred(starred: Boolean) = apply { this.starred = starred }

    /**
     * Sets if all messages in the thread as read or unread.
     * @param unread If the thread is unread.
     * @return The builder.
     */
    fun unread(unread: Boolean) = apply { this.unread = unread }

    /**
     * The IDs of the folders to apply, overwriting all previous folders for all messages in the thread.
     * @param folders The folders the thread should appear in.
     * @return The builder.
     */
    fun folders(folders: List<String>) = apply { this.folders = folders }

    /**
     * Build the [UpdateThreadRequest].
     * @return The [UpdateThreadRequest].
     */
    fun build() = UpdateThreadRequest(
      starred = starred,
      unread = unread,
      folders = folders,
    )
  }
}
