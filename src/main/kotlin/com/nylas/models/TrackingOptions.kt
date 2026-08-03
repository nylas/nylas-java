package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the different tracking options for when a message is sent.
 */
data class TrackingOptions @JvmOverloads constructor(
  /**
   * The label to apply to tracked messages.
   */
  @Json(name = "label")
  val label: String? = null,
  /**
   * Whether to track links.
   */
  @Json(name = "links")
  val links: Boolean? = null,
  /**
   * Whether to track opens.
   */
  @Json(name = "opens")
  val opens: Boolean? = null,
  /**
   * Whether to track thread replies.
   */
  @Json(name = "thread_replies")
  val threadReplies: Boolean? = null,
  /**
   * The custom hostname to use for link and open tracking.
   * The hostname must be active and owned by the authenticated organization.
   */
  @Json(name = "domain_name")
  val domainName: String? = null,
) {
  /**
   * Builder for [TrackingOptions].
   */
  class Builder {
    private var label: String? = null
    private var links: Boolean? = null
    private var opens: Boolean? = null
    private var threadReplies: Boolean? = null
    private var domainName: String? = null

    /**
     * Set the label to apply to tracked messages.
     * @param label The tracking label.
     * @return The builder.
     */
    fun label(label: String?) = apply { this.label = label }

    /**
     * Set whether to track links.
     * @param links Whether to track links.
     * @return The builder.
     */
    fun links(links: Boolean?) = apply { this.links = links }

    /**
     * Set whether to track opens.
     * @param opens Whether to track opens.
     * @return The builder.
     */
    fun opens(opens: Boolean?) = apply { this.opens = opens }

    /**
     * Set whether to track thread replies.
     * @param threadReplies Whether to track thread replies.
     * @return The builder.
     */
    fun threadReplies(threadReplies: Boolean?) = apply { this.threadReplies = threadReplies }

    /**
     * Set the custom hostname used for link and open tracking.
     * @param domainName An active custom hostname owned by the authenticated organization.
     * @return The builder.
     */
    fun domainName(domainName: String?) = apply { this.domainName = domainName }

    /**
     * Build the [TrackingOptions].
     * @return The built [TrackingOptions].
     */
    fun build() = TrackingOptions(label, links, opens, threadReplies, domainName)
  }
}
