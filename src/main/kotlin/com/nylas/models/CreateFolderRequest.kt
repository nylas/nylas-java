package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the Nylas folder creation request.
 */
data class CreateFolderRequest(
  /**
   * The name of the folder.
   */
  @Json(name = "name")
  val name: String,
  /**
   * The parent ID of the folder. (Microsoft only)
   */
  @Json(name = "parent_id")
  val parentId: String? = null,
  /**
   * The background color of the folder. (Google only)
   */
  @Json(name = "background_color")
  val backgroundColor: String? = null,
  /**
   * The text color of the folder. (Google only)
   */
  @Json(name = "text_color")
  val textColor: String? = null,
) {
  /**
   * Builder for [CreateFolderRequest].
   * @param name The name of the folder.
   */
  data class Builder(
    private val name: String,
  ) {
    private var parentId: String? = null
    private var backgroundColor: String? = null
    private var textColor: String? = null

    /**
     * Set the parent ID of the folder. (Microsoft only)
     * @param parentId The parent ID of the folder.
     * @return The builder.
     */
    fun parentId(parentId: String) = apply { this.parentId = parentId }

    /**
     * Set the background color of the folder. (Google only)
     *
     * The background color of the folder in the hexadecimal format `#0099EE`.
     * See Google Defined Values for more information.
     *
     * @param backgroundColor The background color of the folder.
     * @return The builder.
     */
    fun backgroundColor(backgroundColor: String) = apply { this.backgroundColor = backgroundColor }

    /**
     * Set the text color of the folder. (Google only)
     *
     * The text color of the folder in the hexadecimal format `#0099EE`.
     * See Google Defined Values for more information.
     *
     * @param textColor The text color of the folder.
     * @return The builder.
     */
    fun textColor(textColor: String) = apply { this.textColor = textColor }

    /**
     * Build the [CreateFolderRequest].
     * @return The [CreateFolderRequest].
     */
    fun build() = CreateFolderRequest(
      name = name,
      parentId = parentId,
      backgroundColor = backgroundColor,
      textColor = textColor,
    )
  }
}
