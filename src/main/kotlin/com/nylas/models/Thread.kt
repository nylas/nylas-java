package com.nylas.models

import com.squareup.moshi.Json

data class Thread(
  /**
   * The unique identifier for the thread.
   */
  @Json(name = "id")
  val id: String,
  /**
   * Grant ID of the Nylas account.
   */
  @Json(name = "grant_id")
  val grantId: String,
  /**
   * The type of object.
   */
  @Json(name = "object")
  val obj: String = "thread",
  /**
   * The latest message or draft in the thread.
   */
  @Json(name = "latest_draft_or_message")
  val latestDraftOrMessage: IMessage? = null,
  /**
   * Whether or not a message in a thread has attachments.
   */
  @Json(name = "has_attachments")
  val hasAttachments: Boolean? = null,
  /**
   * Whether or not a message in a thread has drafts.
   */
  @Json(name = "has_drafts")
  val hasDrafts: Boolean? = null,
  /**
   * A boolean indicating whether the thread is starred or not.
   */
  @Json(name = "starred")
  val starred: Boolean? = null,
  /**
   * A boolean indicating if all messages within the thread are read or not.
   */
  @Json(name = "unread")
  val unread: Boolean? = null,
  /**
   * Unix timestamp of the earliest or first message in the thread.
   */
  @Json(name = "earliest_message_date")
  val earliestMessageDate: Long? = null,
  /**
   * Unix timestamp of the most recent message received in the thread.
   */
  @Json(name = "latest_message_received_date")
  val latestMessageReceivedDate: Long? = null,
  /**
   * Unix timestamp of the most recent message sent in the thread.
   */
  @Json(name = "latest_message_sent_date")
  val latestMessageSentDate: Long? = null,
  /**
   * An array of participants in the thread.
   */
  @Json(name = "participants")
  val participants: List<EmailName>? = null,
  /**
   * An array of message IDs in the thread.
   */
  @Json(name = "message_ids")
  val messageIds: List<String>? = null,
  /**
   * An array of draft IDs in the thread.
   */
  @Json(name = "draft_ids")
  val draftIds: List<String>? = null,
  /**
   * An array of folder IDs the thread appears in.
   */
  @Json(name = "folders")
  val folders: List<String>? = null,
  /**
   * A short snippet of the last received message/draft body.
   * This is the first 100 characters of the message body, with any HTML tags removed.
   */
  @Json(name = "snippet")
  val snippet: String? = null,
  /**
   * The subject line of the thread.
   */
  @Json(name = "subject")
  val subject: String? = null,
) {
  /**
   * Get the type of object.
   * @return The type of object.
   */
  fun getObject() = obj
}
