package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to send a transactional email from a verified domain.
 */
data class SendTransactionalEmailRequest(
  /**
   * An array of message recipients.
   */
  @Json(name = "to")
  val to: List<EmailName>,
  /**
   * The sender. Must use a verified domain email address.
   */
  @Json(name = "from")
  val from: EmailName,
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
   * Recommended if there is no Agent Account on the domain to receive replies.
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
   * Unix timestamp to send the message at.
   */
  @Json(name = "send_at")
  val sendAt: Long? = null,
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
  /**
   * A list of custom headers to add to the message.
   */
  @Json(name = "custom_headers")
  val customHeaders: List<CustomHeader>? = null,
  /**
   * When true, the message body is sent as plain text and the MIME data doesn't include the HTML version of the message.
   * When false, the message body is sent as HTML.
   */
  @Json(name = "is_plaintext")
  val isPlaintext: Boolean? = null,
) : IMessageAttachmentRequest {
  /**
   * Builder for [SendTransactionalEmailRequest].
   * @property to An array of message recipients.
   * @property from The sender. Must use a verified domain email address.
   */
  data class Builder(
    private val to: List<EmailName>,
    private val from: EmailName,
  ) {
    private var bcc: List<EmailName>? = null
    private var cc: List<EmailName>? = null
    private var replyTo: List<EmailName>? = null
    private var attachments: List<CreateAttachmentRequest>? = null
    private var subject: String? = null
    private var body: String? = null
    private var sendAt: Long? = null
    private var replyToMessageId: String? = null
    private var trackingOptions: TrackingOptions? = null
    private var useDraft: Boolean? = null
    private var customHeaders: List<CustomHeader>? = null
    private var isPlaintext: Boolean? = null

    fun bcc(bcc: List<EmailName>?) = apply { this.bcc = bcc }

    fun cc(cc: List<EmailName>?) = apply { this.cc = cc }

    fun replyTo(replyTo: List<EmailName>?) = apply { this.replyTo = replyTo }

    fun attachments(attachments: List<CreateAttachmentRequest>?) = apply { this.attachments = attachments }

    fun subject(subject: String?) = apply { this.subject = subject }

    fun body(body: String?) = apply { this.body = body }

    fun sendAt(sendAt: Long?) = apply { this.sendAt = sendAt }

    fun sendAt(sendAt: Int?) = apply { this.sendAt = sendAt?.toLong() }

    fun replyToMessageId(replyToMessageId: String?) = apply { this.replyToMessageId = replyToMessageId }

    fun trackingOptions(trackingOptions: TrackingOptions?) = apply { this.trackingOptions = trackingOptions }

    fun useDraft(useDraft: Boolean?) = apply { this.useDraft = useDraft }

    fun customHeaders(customHeaders: List<CustomHeader>?) = apply { this.customHeaders = customHeaders }

    fun isPlaintext(isPlaintext: Boolean?) = apply { this.isPlaintext = isPlaintext }

    fun build() =
      SendTransactionalEmailRequest(
        to,
        from,
        bcc,
        cc,
        replyTo,
        attachments,
        subject,
        body,
        sendAt,
        replyToMessageId,
        trackingOptions,
        useDraft,
        customHeaders,
        isPlaintext,
      )
  }
}
