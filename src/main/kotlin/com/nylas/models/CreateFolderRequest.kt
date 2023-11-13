package com.nylas.models

/**
 * Class representation of the Nylas folder creation request.
 */
data class CreateFolderRequest(
  val name: String,
  val parentId: String? = null,
  val backgroundColor: String? = null,
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
