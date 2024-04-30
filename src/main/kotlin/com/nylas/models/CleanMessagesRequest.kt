package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas clean message request
 */
data class CleanMessagesRequest(
  /**
   * IDs of the email messages to clean.
   */
  @Json(name = "message_id")
  val messageId: List<String>,
  /**
   * If true, removes link-related tags (<a>) from the email message while keeping the text.
   */
  @Json(name = "ignore_links")
  val ignoreLinks: Boolean? = null,
  /**
   * If true, removes images from the email message.
   */
  @Json(name = "ignore_images")
  val ignoreImages: Boolean? = null,
  /**
   * If true, converts images in the email message to Markdown.
   */
  @Json(name = "images_as_markdown")
  val imagesAsMarkdown: Boolean? = null,
  /**
   * If true, removes table-related tags (<table>, <th>, <td>, <tr>) from the email message while keeping rows.
   */
  @Json(name = "ignore_tables")
  val ignoreTables: Boolean? = null,
  /**
   * If true, removes phrases such as "Best" and "Regards" in the email message signature.
   */
  @Json(name = "remove_conclusion_phrases")
  val removeConclusionPhrases: Boolean? = null,
) {

  /**
   * Builder for the [CleanMessagesRequest] class.
   * @param messageId IDs of the email messages to clean.
   */
  data class Builder(
    private val messageId: List<String>,
  ) {
    private var ignoreLinks: Boolean? = null
    private var ignoreImages: Boolean? = null
    private var imagesAsMarkdown: Boolean? = null
    private var ignoreTables: Boolean? = null
    private var removeConclusionPhrases: Boolean? = null

    /**
     * If true, removes link-related tags (<a>) from the email message while keeping the text.
     * @param ignoreLinks The boolean value to set.
     * @return The [Builder] instance.
     */
    fun ignoreLinks(ignoreLinks: Boolean) = apply { this.ignoreLinks = ignoreLinks }

    /**
     * If true, removes images from the email message.
     * @param ignoreImages The boolean value to set.
     * @return The [Builder] instance.
     */
    fun ignoreImages(ignoreImages: Boolean) = apply { this.ignoreImages = ignoreImages }

    /**
     * If true, converts images in the email message to Markdown.
     * @param imagesAsMarkdown The boolean value to set.
     * @return The [Builder] instance.
     */
    fun imagesAsMarkdown(imagesAsMarkdown: Boolean) = apply { this.imagesAsMarkdown = imagesAsMarkdown }

    /**
     * If true, removes table-related tags (<table>, <th>, <td>, <tr>) from the email message while keeping rows.
     * @param ignoreTables The boolean value to set.
     * @return The [Builder] instance.
     */
    fun ignoreTables(ignoreTables: Boolean) = apply { this.ignoreTables = ignoreTables }

    /**
     * If true, removes phrases such as "Best" and "Regards" in the email message signature.
     * @param removeConclusionPhrases The boolean value to set.
     * @return The [Builder] instance.
     */
    fun removeConclusionPhrases(removeConclusionPhrases: Boolean) = apply { this.removeConclusionPhrases = removeConclusionPhrases }

    /**
     * Builds the [CleanMessagesRequest] instance.
     * @return The [CleanMessagesRequest] instance.
     */
    fun build() = CleanMessagesRequest(
      messageId = messageId,
      ignoreLinks = ignoreLinks,
      ignoreImages = ignoreImages,
      imagesAsMarkdown = imagesAsMarkdown,
      ignoreTables = ignoreTables,
      removeConclusionPhrases = removeConclusionPhrases,
    )
  }
}
