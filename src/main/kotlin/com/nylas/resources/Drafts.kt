package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.FileUtils
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types

class Drafts(client: NylasClient) : Resource<Draft>(client, Draft::class.java) {
  /**
   * Return all Drafts
   * @param identifier The identifier of the grant to act upon
   * @param queryParams The query parameters to include in the request
   * @return The list of Drafts
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListDraftsQueryParams? = null): ListResponse<Draft> {
    val path = String.format("v3/grants/%s/drafts", identifier)
    return listResource(path, queryParams)
  }

  /**
   * Return a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to retrieve.
   * @return The Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(identifier: String, draftId: String): Response<Draft> {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    return findResource(path)
  }

  /**
   * Create a Draft
   * @param identifier The identifier of the grant to act upon
   * @param requestBody The values to create the Draft with
   * @return The updated Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(identifier: String, requestBody: CreateDraftRequest): Response<Draft> {
    val path = String.format("v3/grants/%s/drafts", identifier)
    val responseType = Types.newParameterizedType(Response::class.java, Draft::class.java)
    val adapter = JsonHelper.moshi().adapter(CreateDraftRequest::class.java)

    // Use form data only if the attachment size is greater than 3mb
    val attachmentSize = requestBody.attachments?.sumOf { it.size } ?: 0

    return if (attachmentSize >= FileUtils.FORM_DATA_ATTACHMENT_SIZE) {
      val attachmentLessPayload = requestBody.copy(attachments = null)
      val serializedRequestBody = adapter.toJson(attachmentLessPayload)
      val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)

      client.executeFormRequest(path, NylasClient.HttpMethod.POST, multipart, responseType)
    } else {
      val serializedRequestBody = adapter.toJson(requestBody)
      createResource(path, serializedRequestBody)
    }
  }

  /**
   * Update a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to update.
   * @param requestBody The values to update the Draft with
   * @return The updated Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(identifier: String, draftId: String, requestBody: UpdateDraftRequest): Response<Draft> {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    val responseType = Types.newParameterizedType(Response::class.java, Draft::class.java)
    val adapter = JsonHelper.moshi().adapter(UpdateDraftRequest::class.java)

    // Use form data only if the attachment size is greater than 3mb
    val attachmentSize = requestBody.attachments?.sumOf { it.size } ?: 0

    return if (attachmentSize >= FileUtils.FORM_DATA_ATTACHMENT_SIZE) {
      val attachmentLessPayload = requestBody.copy(attachments = null)
      val serializedRequestBody = adapter.toJson(attachmentLessPayload)
      val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)

      client.executeFormRequest(path, NylasClient.HttpMethod.PUT, multipart, responseType)
    } else {
      val serializedRequestBody = adapter.toJson(requestBody)
      updateResource(path, serializedRequestBody)
    }
  }

  /**
   * Delete a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to delete.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, draftId: String): DeleteResponse {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    return destroyResource(path)
  }

  /**
   * Send a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to send.
   * @return The sent Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun send(identifier: String, draftId: String): Response<Message> {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    val responseType = Types.newParameterizedType(Response::class.java, Message::class.java)
    return client.executePost(path, responseType)
  }
}
