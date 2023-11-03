package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Nylas Draft object.
 */
class Draft(
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
   * An array of message senders.
   */
  @Json(name = "from")
  val from: List<EmailName>,
  /**
   * Unix timestamp of when the message was received by the mail server.
   * This may be different from the unverified Date header in raw message object.
   */
  @Json(name = "date")
  val date: Long,
  /**
   * The type of object.
   */
  @Json(name = "object")
  private val obj: String = "draft",
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
   * The ID of the folder(s) the message appears in.
   */
  @Json(name = "folders")
  val folders: List<String>? = null,
  /**
   * An array of message recipients.
   */
  @Json(name = "to")
  val to: List<EmailName>? = null,
  /**
   * Unix timestamp of when the message was created.
   */
  @Json(name = "created_at")
  val createdAt: Long? = null,
  /**
   * An array of files attached to the message.
   */
  @Json(name = "attachments")
  val attachments: List<Attachment>? = null,
) : IMessage {
  /**
   * Get the type of object.
   * @return The type of object.
   */
  fun getObject(): String = obj
}
