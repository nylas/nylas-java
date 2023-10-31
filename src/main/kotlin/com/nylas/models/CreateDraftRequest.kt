package com.nylas.models

/**
 * Class representing a request to create a draft.
 */
data class CreateDraftRequest(
  /**
   * An array of message recipients.
   */
  val to: List<EmailName>,
  /**
   * An array of bcc recipients.
   */
  val bcc: List<EmailName>?,
  /**
   * An array of cc recipients.
   */
  val cc: List<EmailName>?,
  /**
   * An array of name and email pairs that override the sent reply-to headers.
   */
  val replyTo: List<EmailName>?,
  /**
   * An array of files to attach to the message.
   */
  override val attachments: List<CreateAttachmentRequest>?,
  /**
   * A short snippet of the message body.
   * This is the first 100 characters of the message body, with any HTML tags removed.
   */
  val snippet: String?,
  /**
   * The message subject.
   */
  val subject: String?,
  /**
   * A reference to the parent thread object.
   * If this is a new draft, the thread will be empty.
   */
  val threadId: String?,
  /**
   * The full HTML message body.
   * Messages with only plain-text representations are up-converted to HTML.
   */
  val body: String?,
  /**
   * Whether or not the message has been starred by the user.
   */
  val starred: Boolean?,
  /**
   * Whether or not the message has been read by the user.
   */
  val unread: Boolean?,
  /**
   * Unix timestamp to send the message at.
   */
  val sendAt: Int?,
  /**
   * The ID of the message that you are replying to.
   */
  val replyToMessageId: String?,
  /**
   * Options for tracking opens, links, and thread replies.
   */
  val trackingOptions: TrackingOptions?,
) : IMessageAttachmentRequest {
  /**
   * Builder for [CreateDraftRequest].
   * @property to An array of message recipients.
   */
  data class Builder(
    private val to: List<EmailName>,
  ) {
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
