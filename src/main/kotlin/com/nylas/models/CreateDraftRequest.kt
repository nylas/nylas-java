package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to create a draft.
 */
data class CreateDraftRequest(
  /**
   * An array of message recipients.
   */
  @Json(name = "to")
  val to: List<EmailName>? = null,
  /**
   * An array of bcc recipients.
   */
  @Json(name = "bcc")
  val bcc: List<EmailName>? = null,
  /**
   * An array of cc recipients.
   */
  @Json(name = "cc")
  val cc: List<EmailName>? = null,
  /**
   * An array of name and email pairs that override the sent reply-to headers.
   */
  @Json(name = "reply_to")
  val replyTo: List<EmailName>? = null,
  /**
   * An array of files to attach to the message.
   */
  @Transient
  override val attachments: List<CreateAttachmentRequest>? = null,
  /**
   * A short snippet of the message body.
   * This is the first 100 characters of the message body, with any HTML tags removed.
   */
  @Json(name = "snippet")
  val snippet: String? = null,
  /**
   * The message subject.
   */
  @Json(name = "subject")
  val subject: String? = null,
  /**
   * A reference to the parent thread object.
   * If this is a new draft, the thread will be empty.
   */
  @Json(name = "thread_id")
  val threadId: String? = null,
  /**
   * The full HTML message body.
   * Messages with only plain-text representations are up-converted to HTML.
   */
  @Json(name = "body")
  val body: String? = null,
  /**
   * Whether or not the message has been starred by the user.
   */
  @Json(name = "starred")
  val starred: Boolean? = null,
  /**
   * Whether or not the message has been read by the user.
   */
  @Json(name = "unread")
  val unread: Boolean? = null,
  /**
   * Unix timestamp to send the message at.
   */
  @Json(name = "send_at")
  val sendAt: Int? = null,
  /**
   * The ID of the message that you are replying to.
   */
  @Json(name = "reply_to_message_id")
  val replyToMessageId: String? = null,
  /**
   * Options for tracking opens, links, and thread replies.
   */
  @Json(name = "tracking_options")
  val trackingOptions: TrackingOptions? = null,
) : IMessageAttachmentRequest {
  /**
   * Builder for [CreateDraftRequest].
   */
  class Builder {
    private var to: List<EmailName>? = null
    private var bcc: List<EmailName>? = null
    private var cc: List<EmailName>? = null
    private var replyTo: List<EmailName>? = null
    private var attachments: List<CreateAttachmentRequest>? = null
    private var snippet: String? = null
    private var subject: String? = null
    private var threadId: String? = null
    private var body: String? = null
    private var starred: Boolean? = null
    private var unread: Boolean? = null
    private var sendAt: Int? = null
    private var replyToMessageId: String? = null
    private var trackingOptions: TrackingOptions? = null

    /**
     * Sets the recipients.
     * @param to The recipients.
     * @return The builder.
     */
    fun to(to: List<EmailName>?) = apply { this.to = to }

    /**
     * Sets the bcc recipients.
     * @param bcc The bcc recipients.
     * @return The builder.
     */
    fun bcc(bcc: List<EmailName>?) = apply { this.bcc = bcc }

    /**
     * Sets the cc recipients.
     * @param cc The cc recipients.
     * @return The builder.
     */
    fun cc(cc: List<EmailName>?) = apply { this.cc = cc }

    /**
     * Sets the reply-to recipients.
     * @param replyTo The reply-to recipients.
     * @return The builder.
     */
    fun replyTo(replyTo: List<EmailName>?) = apply { this.replyTo = replyTo }

    /**
     * Sets the files to attach to the message.
     * @param attachments The files to attach to the message.
     * @return The builder.
     */
    fun attachments(attachments: List<CreateAttachmentRequest>?) = apply { this.attachments = attachments }

    /**
     * Sets the snippet of the message body.
     * This is the first 100 characters of the message body, with any HTML tags removed.
     * @param snippet The snippet of the message body.
     * @return The builder.
     */
    fun snippet(snippet: String?) = apply { this.snippet = snippet }

    /**
     * Sets the message subject.
     * @param subject The message subject.
     * @return The builder.
     */
    fun subject(subject: String?) = apply { this.subject = subject }

    /**
     * Sets the reference to the parent thread object.
     * If this is a new draft, the thread will be empty.
     * @param threadId The reference to the parent thread object.
     * @return The builder.
     */
    fun threadId(threadId: String?) = apply { this.threadId = threadId }

    /**
     * Sets the full HTML message body.
     * Messages with only plain-text representations are up-converted to HTML.
     * @param body The full HTML message body.
     * @return The builder.
     */
    fun body(body: String?) = apply { this.body = body }

    /**
     * Sets whether or not the message has been starred by the user.
     * @param starred Whether or not the message has been starred by the user.
     * @return The builder.
     */
    fun starred(starred: Boolean?) = apply { this.starred = starred }

    /**
     * Sets whether or not the message has been read by the user.
     * @param unread Whether or not the message has been read by the user.
     * @return The builder.
     */
    fun unread(unread: Boolean?) = apply { this.unread = unread }

    /**
     * Sets the unix timestamp to send the message at.
     * @param sendAt The unix timestamp to send the message at.
     * @return The builder.
     */
    fun sendAt(sendAt: Int?) = apply { this.sendAt = sendAt }

    /**
     * Sets the ID of the message that you are replying to.
     * @param replyToMessageId The ID of the message that you are replying to.
     * @return The builder.
     */
    fun replyToMessageId(replyToMessageId: String?) = apply { this.replyToMessageId = replyToMessageId }

    /**
     * Sets the options for tracking opens, links, and thread replies.
     * @param trackingOptions The options for tracking opens, links, and thread replies.
     * @return The builder.
     */
    fun trackingOptions(trackingOptions: TrackingOptions?) = apply { this.trackingOptions = trackingOptions }

    /**
     * Builds a [SendMessageRequest] instance.
     * @return The [SendMessageRequest] instance.
     */
    fun build() = CreateDraftRequest(
      to,
      bcc,
      cc,
      replyTo,
      attachments,
      snippet,
      subject,
      threadId,
      body,
      starred,
      unread,
      sendAt,
      replyToMessageId,
      trackingOptions,
    )
  }
}
