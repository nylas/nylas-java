package com.nylas.models

/**
 * Class representing a request to update a draft.
 */
data class UpdateDraftRequest(
  /**
   * An array of message recipients.
   */
  val to: List<EmailName>?,
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
   * The message subject.
   */
  val subject: String?,
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
   * Builder for [UpdateDraftRequest].
   */
  class Builder {
    private var to: List<EmailName>? = null
    private var bcc: List<EmailName>? = null
    private var cc: List<EmailName>? = null
    private var replyTo: List<EmailName>? = null
    private var attachments: List<CreateAttachmentRequest>? = null
    private var subject: String? = null
    private var body: String? = null
    private var starred: Boolean? = null
    private var unread: Boolean? = null
    private var sendAt: Int? = null
    private var replyToMessageId: String? = null
    private var trackingOptions: TrackingOptions? = null

    /**
     * Set an array of message recipients.
     * @param to An array of message recipients.
     * @return The builder.
     */
    fun to(to: List<EmailName>) = apply { this.to = to }

    /**
     * Set an array of bcc recipients.
     * @param bcc An array of bcc recipients.
     * @return The builder.
     */
    fun bcc(bcc: List<EmailName>) = apply { this.bcc = bcc }

    /**
     * Set an array of cc recipients.
     * @param cc An array of cc recipients.
     * @return The builder.
     */
    fun cc(cc: List<EmailName>) = apply { this.cc = cc }

    /**
     * Set an array of name and email pairs that override the sent reply-to headers.
     * @param replyTo An array of name and email pairs that override the sent reply-to headers.
     * @return The builder.
     */
    fun replyTo(replyTo: List<EmailName>) = apply { this.replyTo = replyTo }

    /**
     * Set an array of files to attach to the draft.
     * @param attachments An array of files to attach to the message.
     * @return The builder.
     */
    fun attachments(attachments: List<CreateAttachmentRequest>) = apply { this.attachments = attachments }

    /**
     * Set the message subject.
     * @param subject The message subject.
     * @return The builder.
     */
    fun subject(subject: String) = apply { this.subject = subject }

    /**
     * Set the full HTML message body.
     * Messages with only plain-text representations are up-converted to HTML.
     * @param body The full HTML message body.
     * @return The builder.
     */
    fun body(body: String) = apply { this.body = body }

    /**
     * Set whether or not the message has been starred by the user.
     * @param starred Whether or not the message has been starred by the user.
     * @return The builder.
     */
    fun starred(starred: Boolean) = apply { this.starred = starred }

    /**
     * Set whether or not the message has been read by the user.
     * @param unread Whether or not the message has been read by the user.
     * @return The builder.
     */
    fun unread(unread: Boolean) = apply { this.unread = unread }

    /**
     * Set the unix timestamp to send the message at.
     * @param sendAt The unix timestamp to send the message at.
     * @return The builder.
     */
    fun sendAt(sendAt: Int) = apply { this.sendAt = sendAt }

    /**
     * Set the ID of the message that you are replying to.
     * @param replyToMessageId The ID of the message that you are replying to.
     * @return The builder.
     */
    fun replyToMessageId(replyToMessageId: String) = apply { this.replyToMessageId = replyToMessageId }

    /**
     * Set options for tracking opens, links, and thread replies.
     * @param trackingOptions Options for tracking opens, links, and thread replies.
     * @return The builder.
     */
    fun trackingOptions(trackingOptions: TrackingOptions) = apply { this.trackingOptions = trackingOptions }

    fun build() = UpdateDraftRequest(
      to = to,
      bcc = bcc,
      cc = cc,
      replyTo = replyTo,
      attachments = attachments,
      subject = subject,
      body = body,
      starred = starred,
      unread = unread,
      sendAt = sendAt,
      replyToMessageId = replyToMessageId,
      trackingOptions = trackingOptions,
    )
  }
}
