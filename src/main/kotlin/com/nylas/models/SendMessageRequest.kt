package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to send a message.
 */
data class SendMessageRequest(
  /**
   * An array of message recipients.
   */
  @Json(name = "to")
  val to: List<EmailName>,
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
  @Json(name = "attachments")
  override val attachments: List<CreateAttachmentRequest>? = null,
  /**
   * The message subject.
   */
  @Json(name = "subject")
  val subject: String? = null,
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
  /**
   * Whether or not to use draft support.
   * This is primarily used when dealing with large attachments.
   */
  @Json(name = "use_draft")
  val useDraft: Boolean? = null,
) : IMessageAttachmentRequest {
  /**
   * Builder for [SendMessageRequest].
   * @property to An array of message recipients.
   */
  data class Builder(
    private val to: List<EmailName>,
  ) {
    private var bcc: List<EmailName>? = null
    private var cc: List<EmailName>? = null
    private var replyTo: List<EmailName>? = null
    private var attachments: List<CreateAttachmentRequest>? = null
    internal var subject: String? = null
    private var body: String? = null
    private var starred: Boolean? = null
    private var sendAt: Int? = null
    private var replyToMessageId: String? = null
    private var trackingOptions: TrackingOptions? = null
    private var useDraft: Boolean? = null

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
     * Sets the message subject.
     * @param subject The message subject.
     * @return The builder.
     */
    fun subject(subject: String?) = apply { this.subject = subject }

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
     * Sets whether or not to use draft support.
     * This is primarily used when dealing with large attachments.
     * @param useDraft Whether or not to use draft support.
     * @return The builder.
     */
    fun useDraft(useDraft: Boolean?) = apply { this.useDraft = useDraft }

    /**
     * Builds a [SendMessageRequest] instance.
     * @return The [SendMessageRequest] instance.
     */
    fun build() =
      SendMessageRequest(
        to,
        bcc,
        cc,
        replyTo,
        attachments,
        subject,
        body,
        starred,
        sendAt,
        replyToMessageId,
        trackingOptions,
        useDraft,
      )
  }
}
