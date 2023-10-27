package com.nylas.models

/**
 * Class representing a request to update a draft.
 */
data class UpdateDraftRequest(
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
  val attachments: List<CreateFileRequest>?,
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
)
