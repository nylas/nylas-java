package com.nylas.models

data class Thread(
  /**
   * The unique identifier for the thread.
   */
  val id: String,
  /**
   * Grant ID of the Nylas account.
   */
  val grantId: String,
  /**
   * The type of object.
   */
  val obj: String = "thread",
  /**
   * The latest message or draft in the thread.
   */
  val latestDraftOrMessage: IMessage? = null,
  /**
   * Whether or not a message in a thread has attachments.
   */
  val hasAttachments: Boolean? = null,
  /**
   * Whether or not a message in a thread has drafts.
   */
  val hasDrafts: Boolean? = null,
  /**
   * A boolean indicating whether the thread is starred or not.
   */
  val starred: Boolean? = null,
  /**
   * A boolean indicating if all messages within the thread are read or not.
   */
  val unread: Boolean? = null,
  /**
   * Unix timestamp of the earliest or first message in the thread.
   */
  val earliestMessageDate: Long? = null,
  /**
   * Unix timestamp of the most recent message received in the thread.
   */
  val latestMessageReceivedDate: Long? = null,
  /**
   * Unix timestamp of the most recent message sent in the thread.
   */
  val latestMessageSentDate: Long? = null,
  /**
   * An array of participants in the thread.
   */
  val participants: List<EmailName>? = null,
  /**
   * An array of message IDs in the thread.
   */
  val messageIds: List<String>? = null,
  /**
   * An array of draft IDs in the thread.
   */
  val draftIds: List<String>? = null,
  /**
   * An array of folder IDs the thread appears in.
   */
  val folders: List<String>? = null,
  /**
   * A short snippet of the last received message/draft body.
   * This is the first 100 characters of the message body, with any HTML tags removed.
   */
  val snippet: String? = null,
  /**
   * The subject line of the thread.
   */
  val subject: String? = null,
) {
  /**
   * Get the type of object.
   * @return The type of object.
   */
  fun getObject() = obj
}
