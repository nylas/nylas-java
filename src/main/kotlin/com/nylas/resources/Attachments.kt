package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*

/**
 * Nylas Attachments API
 *
 * The Nylas Attachments API allows you to fetch attachment metadata and download data.
 *
 * @param client The configured Nylas API client
 */
class Attachments(client: NylasClient) : Resource<Attachment>(client, Attachment::class.java) {
  /**
   * Return metadata of an attachment
   * @param identifier Grant ID or email account to query
   * @param attachmentId The id of the attachment to retrieve.
   * @param queryParams The query parameters to include in the request
   * @return The attachment metadata
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  fun find(identifier: String, attachmentId: String, queryParams: FindAttachmentQueryParams): Response<Attachment> {
    val path = String.format("v3/grants/%s/attachments/%s", identifier, attachmentId)
    return findResource(path, queryParams)
  }
}
