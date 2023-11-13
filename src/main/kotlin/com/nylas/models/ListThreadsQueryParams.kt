package com.nylas.models

import com.squareup.moshi.Json

data class ListThreadsQueryParams(
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
   * Return items containing messages sent to these email address.
   */
  @Json(name = "to")
  val to: List<String>? = null,
  /**
   * Return items containing messages sent from these email address.
   */
  @Json(name = "from")
  val from: List<String>? = null,
  /**
   * Return items containing messages cc'd on these email address.
   */
  @Json(name = "cc")
  val cc: List<String>? = null,
  /**
   * Return items containing messages bcc'd on these email address.
   */
  @Json(name = "bcc")
  val bcc: List<String>? = null,
  /**
   * Return emails that are in these folder IDs.
   */
  @Json(name = "in")
  val inFolder: List<String>? = null,
  /**
   * Return emails that are unread.
   */
  @Json(name = "unread")
  val unread: Boolean? = null,
  /**
   * Return emails that are starred.
   */
  @Json(name = "starred")
  val starred: Boolean? = null,
  /**
   * Return threads whose most recent message was received before this Unix timestamp.
   */
  @Json(name = "latest_message_before")
  val latestMessageBefore: Long? = null,
  /**
   * Return threads whose most recent message was received after this Unix timestamp.
   */
  @Json(name = "latest_message_after")
  val latestMessageAfter: Long? = null,
  /**
   * Return emails that contain attachments.
   */
  @Json(name = "has_attachment")
  val hasAttachment: Boolean? = null,
  /**
   * The provider-specific query string used to search messages.
   * Available for Google and Microsoft Graph only.
   */
  @Json(name = "search_query_native")
  val searchQueryNative: String? = null,
) : IQueryParams {
  /**
   * Builder for [ListThreadsQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var subject: String? = null
    private var anyEmail: List<String>? = null
    private var to: List<String>? = null
    private var from: List<String>? = null
    private var cc: List<String>? = null
    private var bcc: List<String>? = null
    private var inFolder: List<String>? = null
    private var unread: Boolean? = null
    private var starred: Boolean? = null
    private var threadId: String? = null
    private var latestMessageBefore: Long? = null
    private var latestMessageAfter: Long? = null
    private var hasAttachment: Boolean? = null
    private var fields: MessageFields? = null
    private var searchQueryNative: String? = null

    /**
     * Sets the maximum number of objects to return.
     * This field defaults to 10. The maximum allowed value is 200.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int?) = apply { this.limit = limit }

    /**
     * Sets the identifier that specifies which page of data to return.
     * This value should be taken from the next_cursor response field.
     * @param pageToken The identifier that specifies which page of data to return.
     * @return The builder.
     */
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }

    /**
     * Sets the subject to match.
     * @param subject The subject to match.
     * @return The builder.
     */
    fun subject(subject: String?) = apply { this.subject = subject }

    /**
     * Set the list of email addresses to match.
     * @param anyEmail The list of email addresses to match.
     * @return The builder
     */
    fun anyEmail(anyEmail: List<String>?) = apply { this.anyEmail = anyEmail }

    /**
     * Set the list of email addresses to match in the to field.
     * @param to The list of email addresses to match in the to field.
     * @return The builder
     */
    fun to(to: List<String>?) = apply { this.to = to }

    /**
     * Set the list of email addresses to match in the from field.
     * @param from The list of email addresses to match in the from field.
     * @return The builder
     */
    fun from(from: List<String>?) = apply { this.from = from }

    /**
     * Set the list of email addresses to match in the cc field.
     * @param cc The list of email addresses to match in the cc field.
     * @return The builder
     */
    fun cc(cc: List<String>?) = apply { this.cc = cc }

    /**
     * Set the list of email addresses to match in the bcc field.
     * @param bcc The list of email addresses to match in the bcc field.
     * @return The builder
     */
    fun bcc(bcc: List<String>?) = apply { this.bcc = bcc }

    /**
     * Set the list of folder IDs to match.
     * @param inFolder The list of folder IDs to match.
     * @return The builder
     */
    fun inFolder(inFolder: List<String>?) = apply { this.inFolder = inFolder }

    /**
     * Set to true to return emails that are unread.
     * @param unread True to return emails that are unread.
     * @return The builder
     */
    fun unread(unread: Boolean?) = apply { this.unread = unread }

    /**
     * Set to true to return emails that are starred.
     * @param starred True to return emails that are starred.
     * @return The builder
     */
    fun starred(starred: Boolean?) = apply { this.starred = starred }

    /**
     * Set the thread ID to match.
     * @param threadId The thread ID to match.
     * @return The builder
     */
    fun threadId(threadId: String?) = apply { this.threadId = threadId }

    /**
     * Set the timestamp to match for emails received before.
     * @param receivedBefore The timestamp to match for emails received before.
     * @return The builder
     */
    fun latestMessageBefore(latestMessageBefore: Long?) = apply { this.latestMessageBefore = latestMessageBefore }

    /**
     * Set the timestamp to match for emails received after.
     * @param receivedAfter The timestamp to match for emails received after.
     * @return The builder
     */
    fun latestMessageAfter(latestMessageAfter: Long?) = apply { this.latestMessageAfter = latestMessageAfter }

    /**
     * Set to true to return emails that contain attachments.
     * @param hasAttachment True to return emails that contain attachments.
     * @return The builder
     */
    fun hasAttachment(hasAttachment: Boolean?) = apply { this.hasAttachment = hasAttachment }

    /**
     * Set to true to return emails that contain attachments.
     * @param fields The fields to include in the response.
     * @return The builder
     */
    fun fields(fields: MessageFields?) = apply { this.fields = fields }

    /**
     * Set the provider-specific query string used to search messages.
     * Available for Google and Microsoft Graph only.
     * @param searchQueryNative The provider-specific query string used to search messages.
     * @return The builder
     */
    fun searchQueryNative(searchQueryNative: String?) = apply { this.searchQueryNative = searchQueryNative }

    /**
     * Builds the [ListThreadsQueryParams] object.
     * @return The [ListThreadsQueryParams] object.
     */
    fun build() = ListThreadsQueryParams(
      limit = limit,
      pageToken = pageToken,
      subject = subject,
      anyEmail = anyEmail,
      to = to,
      from = from,
      cc = cc,
      bcc = bcc,
      inFolder = inFolder,
      unread = unread,
      starred = starred,
      latestMessageBefore = latestMessageBefore,
      latestMessageAfter = latestMessageAfter,
      hasAttachment = hasAttachment,
      searchQueryNative = searchQueryNative,
    )
  }
}
