package com.nylas.models

/**
 * Class representation of the Nylas folder update request.
 */
data class UpdateFolderRequest(
  val name: String? = null,
  val parentId: String? = null,
  val backgroundColor: String? = null,
  val textColor: String? = null,
) {
  /**
   * Builder for [UpdateFolderRequest].
   */
  class Builder {
    private var name: String? = null
    private var parentId: String? = null
    private var backgroundColor: String? = null
    private var textColor: String? = null

    /**
     * Set the name of the folder.
     * @param name The name of the folder.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Set the parent ID of the folder.
     * @param parentId The parent ID of the folder.
     * @return The builder.
     */
    fun parentId(parentId: String) = apply { this.parentId = parentId }

    /**
     * Set the background color of the folder.
     * @param backgroundColor The background color of the folder.
     * @return The builder.
     */
    fun backgroundColor(backgroundColor: String) = apply { this.backgroundColor = backgroundColor }

    /**
     * Set the text color of the folder.
     * @param textColor The text color of the folder.
     * @return The builder.
     */
    fun textColor(textColor: String) = apply { this.textColor = textColor }

    /**
     * Build the [UpdateFolderRequest].
     * @return The [UpdateFolderRequest].
     */
    fun build() = UpdateFolderRequest(
      name = name,
      parentId = parentId,
      backgroundColor = backgroundColor,
      textColor = textColor,
    )
  }
}
