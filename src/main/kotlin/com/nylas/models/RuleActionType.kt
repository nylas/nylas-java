package com.nylas.models

import com.squareup.moshi.Json

/**
 * The action type for a Nylas rule.
 * Note: [BLOCK] is terminal and cannot be combined with other actions.
 */
enum class RuleActionType {
  /**
   * Rejects the message at the SMTP level (inbound) or the send request with HTTP 403 (outbound).
   * Terminal — cannot be combined with other actions.
   */
  @Json(name = "block")
  BLOCK,

  /**
   * Delivers the message to the spam/junk folder.
   */
  @Json(name = "mark_as_spam")
  MARK_AS_SPAM,

  /**
   * Moves the message to a folder. Requires [RuleAction.value] to be set to the folder ID.
   */
  @Json(name = "assign_to_folder")
  ASSIGN_TO_FOLDER,

  /**
   * Marks the message as read.
   */
  @Json(name = "mark_as_read")
  MARK_AS_READ,

  /**
   * Marks the message as starred/flagged.
   */
  @Json(name = "mark_as_starred")
  MARK_AS_STARRED,

  /**
   * Moves the message to the archive folder.
   */
  @Json(name = "archive")
  ARCHIVE,

  /**
   * Moves the message to the trash folder.
   */
  @Json(name = "trash")
  TRASH,
}
