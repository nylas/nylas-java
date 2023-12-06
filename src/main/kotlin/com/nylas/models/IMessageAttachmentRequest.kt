package com.nylas.models

/**
 * Represents a message-related request that uses an attachment
 */
interface IMessageAttachmentRequest {
  /**
   * An array of files to attach to the message.
   */
  val attachments: List<CreateAttachmentRequest>?
}
