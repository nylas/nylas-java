package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the query parameters for listing drafts.
 */
data class ListDraftsQueryParams(
  /**
   * The maximum number of objects to return.
   * This field defaults to 50. The maximum allowed value is 200.
   */
  @Json(name = "limit")
  val limit: Int? = null,
  /**
   * An identifier that specifies which page of data to return.
   * This value should be taken from the [ListResponse.nextCursor] response field.
   */
  @Json(name = "page_token")
  val pageToken: String? = null,
  /**
   * Return items with a matching literal subject.
   */
  @Json(name = "subject")
  val subject: String? = null,
  /**
   * Return emails that have been sent or received from this list of email addresses.
   */
  @Json(name = "any_email")
  val anyEmail: List<String>? = null,
  /**
   * Return items containing drafts to be sent these email address.
   */
  @Json(name = "to")
  val to: List<String>? = null,
  /**
   * Return items containing drafts cc'ing these email address.
   */
  @Json(name = "cc")
  val cc: List<String>? = null,
  /**
   * Return items containing drafts bcc'ing these email address.
   */
  @Json(name = "bcc")
  val bcc: List<String>? = null,
  /**
   * Return drafts that are unread.
   */
  @Json(name = "unread")
  val unread: Boolean? = null,
  /**
   * Return drafts that are starred.
   */
  @Json(name = "starred")
  val starred: Boolean? = null,
  /**
   * Return drafts that belong to this thread.
   */
  @Json(name = "thread_id")
  val threadId: String? = null,
  /**
   * Return drafts that contain attachments.
   */
  @Json(name = "has_attachment")
  val hasAttachment: Boolean? = null,
) : IQueryParams {
  /**
   * Builder for [ListDraftsQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var subject: String? = null
    private var anyEmail: List<String>? = null
    private var to: List<String>? = null
    private var cc: List<String>? = null
    private var bcc: List<String>? = null
    private var unread: Boolean? = null
    private var starred: Boolean? = null
    private var threadId: String? = null
    private var hasAttachment: Boolean? = null

    /**
     * Sets the maximum number of objects to return.
     * This field defaults to 50. The maximum allowed value is 200.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int?) = apply { this.limit = limit }

    /**
     * Sets the identifier that specifies which page of data to return.
     * This value should be taken from the [ListResponse.nextCursor] response field.
     * @param pageToken The identifier that specifies which page of data to return.
     * @return The builder.
     */
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }

    /**
     * Sets the subject.
     * @param subject The subject.
     * @return The builder.
     */
    fun subject(subject: String?) = apply { this.subject = subject }

    /**
     * Sets the list of email addresses.
     * @param anyEmail The list of email addresses.
     * @return The builder.
     */
    fun anyEmail(anyEmail: List<String>?) = apply { this.anyEmail = anyEmail }

    /**
     * Sets the list of email addresses.
     * @param to The list of email addresses.
     * @return The builder.
     */
    fun to(to: List<String>?) = apply { this.to = to }

    /**
     * Sets the list of email addresses.
     * @param cc The list of email addresses.
     * @return The builder.
     */
    fun cc(cc: List<String>?) = apply { this.cc = cc }

    /**
     * Sets the list of email addresses.
     * @param bcc The list of email addresses.
     * @return The builder.
     */
    fun bcc(bcc: List<String>?) = apply { this.bcc = bcc }

    /**
     * Sets the unread flag.
     * @param unread The unread flag.
     * @return The builder.
     */
    fun unread(unread: Boolean?) = apply { this.unread = unread }

    /**
     * Sets the starred flag.
     * @param starred The starred flag.
     * @return The builder.
     */
    fun starred(starred: Boolean?) = apply { this.starred = starred }

    /**
     * Sets the thread id.
     * @param threadId The thread id.
     * @return The builder.
     */
    fun threadId(threadId: String?) = apply { this.threadId = threadId }

    /**
     * Sets the has attachment flag.
     * @param hasAttachment The has attachment flag.
     * @return The builder.
     */
    fun hasAttachment(hasAttachment: Boolean?) = apply { this.hasAttachment = hasAttachment }

    /**
     * Builds a [ListDraftsQueryParams] instance.
     * @return The [ListDraftsQueryParams] instance.
     */
    fun build() = ListDraftsQueryParams(
      limit = limit,
      pageToken = pageToken,
      subject = subject,
      anyEmail = anyEmail,
      to = to,
      cc = cc,
      bcc = bcc,
      unread = unread,
      starred = starred,
      threadId = threadId,
      hasAttachment = hasAttachment,
    )
  }
}
