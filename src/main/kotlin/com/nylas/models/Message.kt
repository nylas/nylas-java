package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Nylas Message object.
 */
data class Message(
  /**
   * The unique identifier for the message.
   */
  @Json(name = "id")
  val id: String,
  /**
   * Grant ID of the Nylas account.
   */
  @Json(name = "grant_id")
  val grantId: String,
  /**
   * An array of message recipients.
   */
  @Json(name = "to")
  val to: List<EmailName>,
  /**
   * Unix timestamp of when the message was received by the mail server.
   * This may be different from the unverified Date header in raw message object.
   */
  @Json(name = "date")
  val date: Long,
  /**
   * Unix timestamp of when the message was created.
   */
  @Json(name = "created_at")
  val createdAt: Long,
  /**
   * The type of object.
   */
  @Json(name = "object")
  private val obj: String = "message",
  /**
   * An array of bcc recipients.
   */
  @Json(name = "bcc")
  val bcc: List<EmailName>?,
  /**
   * An array of cc recipients.
   */
  @Json(name = "cc")
  val cc: List<EmailName>?,
  /**
   * An array of name and email pairs that override the sent reply-to headers.
   */
  @Json(name = "reply_to")
  val replyTo: List<EmailName>?,
  /**
   * A short snippet of the message body.
   * This is the first 100 characters of the message body, with any HTML tags removed.
   */
  @Json(name = "snippet")
  val snippet: String?,
  /**
   * The message subject.
   */
  @Json(name = "subject")
  val subject: String?,
  /**
   * A reference to the parent thread object.
   * If this is a new draft, the thread will be empty.
   */
  @Json(name = "thread_id")
  val threadId: String?,
  /**
   * The full HTML message body.
   * Messages with only plain-text representations are up-converted to HTML.
   */
  @Json(name = "body")
  val body: String?,
  /**
   * Whether or not the message has been starred by the user.
   */
  @Json(name = "starred")
  val starred: Boolean?,
  /**
   * Whether or not the message has been read by the user.
   */
  @Json(name = "unread")
  val unread: Boolean?,
  /**
   * The ID of the folder(s) the message appears in.
   */
  @Json(name = "folders")
  val folders: List<String>?,
  /**
   * An array of message senders.
   */
  @Json(name = "from")
  val from: List<EmailName>?,
  /**
   * An array of files attached to the message.
   */
  @Json(name = "attachments")
  val attachments: List<Attachment>?,
  /**
   * The message headers.
   * Only present if the 'fields' query parameter is set to includeHeaders.
   */
  @Json(name = "metadata")
  val headers: List<MessageHeaders>?,
  /**
   * A list of key-value pairs storing additional data.
   */
  @Json(name = "metadata")
  val metadata: Map<String, Any>?,
) : IMessage {
  /**
   * Get the type of object.
   * @return The type of object.
   */
  fun getObject(): String = obj
}
