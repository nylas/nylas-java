package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import okhttp3.ResponseBody

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
   * @param overrides Optional request overrides to apply
   * @return The attachment metadata
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(identifier: String, attachmentId: String, queryParams: FindAttachmentQueryParams, overrides: RequestOverrides? = null): Response<Attachment> {
    val path = String.format("v3/grants/%s/attachments/%s", identifier, attachmentId)
    return findResource(path, queryParams, overrides = overrides)
  }

  /**
   * Download the attachment data
   *
   * This method returns a [ResponseBody] which can be used to stream the attachment data,
   * and exposes useful headers such as the content type and content length.
   * **NOTE**: The caller is responsible for closing the response body.
   *
   * Alternatively, you can use [downloadBytes] to download the attachment as a byte array.
   *
   * @param identifier Grant ID or email account to query
   * @param attachmentId The id of the attachment to download.
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The [ResponseBody] containing the file data
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun download(identifier: String, attachmentId: String, queryParams: FindAttachmentQueryParams, overrides: RequestOverrides? = null): ResponseBody {
    val path = String.format("v3/grants/%s/attachments/%s/download", identifier, attachmentId)

    return client.downloadResponse(path, queryParams, overrides = overrides)
  }

  /**
   * Download the attachment as a byte array
   * @param identifier Grant ID or email account to query
   * @param attachmentId The id of the attachment to download.
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The raw file data
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun downloadBytes(identifier: String, attachmentId: String, queryParams: FindAttachmentQueryParams, overrides: RequestOverrides? = null): ByteArray {
    val download = download(identifier, attachmentId, queryParams, overrides)
    val fileBytes = download.bytes()
    download.close()
    return fileBytes
  }
}
