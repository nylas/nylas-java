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
   * @param overrides Optional request overrides to apply
   * @return The list of Drafts
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListDraftsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Draft> {
    val path = String.format("v3/grants/%s/drafts", identifier)
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to retrieve.
   * @param overrides Optional request overrides to apply
   * @return The Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(identifier: String, draftId: String, overrides: RequestOverrides? = null): Response<Draft> {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a Draft
   * @param identifier The identifier of the grant to act upon
   * @param requestBody The values to create the Draft with
   * @param overrides Optional request overrides to apply
   * @return The updated Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(identifier: String, requestBody: CreateDraftRequest, overrides: RequestOverrides? = null): Response<Draft> {
    val path = String.format("v3/grants/%s/drafts", identifier)
    val responseType = Types.newParameterizedType(Response::class.java, Draft::class.java)
    val adapter = JsonHelper.moshi().adapter(CreateDraftRequest::class.java)

    // Use form data only if the attachment size is greater than 3mb
    val attachmentSize = requestBody.attachments?.sumOf { it.size } ?: 0

    return if (attachmentSize >= FileUtils.MAXIMUM_JSON_ATTACHMENT_SIZE) {
      val attachmentLessPayload = requestBody.copy(attachments = null)
      val serializedRequestBody = adapter.toJson(attachmentLessPayload)
      val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)

      client.executeFormRequest(path, NylasClient.HttpMethod.POST, multipart, responseType, overrides = overrides)
    } else {
      val serializedRequestBody = adapter.toJson(requestBody)
      createResource(path, serializedRequestBody, overrides = overrides)
    }
  }

  /**
   * Update a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to update.
   * @param requestBody The values to update the Draft with
   * @param overrides Optional request overrides to apply
   * @return The updated Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(identifier: String, draftId: String, requestBody: UpdateDraftRequest, overrides: RequestOverrides? = null): Response<Draft> {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    val responseType = Types.newParameterizedType(Response::class.java, Draft::class.java)
    val adapter = JsonHelper.moshi().adapter(UpdateDraftRequest::class.java)

    // Use form data only if the attachment size is greater than 3mb
    val attachmentSize = requestBody.attachments?.sumOf { it.size } ?: 0

    return if (attachmentSize >= FileUtils.MAXIMUM_JSON_ATTACHMENT_SIZE) {
      val attachmentLessPayload = requestBody.copy(attachments = null)
      val serializedRequestBody = adapter.toJson(attachmentLessPayload)
      val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)

      client.executeFormRequest(path, NylasClient.HttpMethod.PUT, multipart, responseType, overrides = overrides)
    } else {
      val serializedRequestBody = adapter.toJson(requestBody)
      updateResource(path, serializedRequestBody, overrides = overrides)
    }
  }

  /**
   * Delete a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to delete.
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(identifier: String, draftId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    return destroyResource(path, overrides = overrides)
  }

  /**
   * Send a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to send.
   * @param overrides Optional request overrides to apply
   * @return The sent Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun send(identifier: String, draftId: String, overrides: RequestOverrides? = null): Response<Message> {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    val responseType = Types.newParameterizedType(Response::class.java, Message::class.java)
    return client.executePost(path, responseType, overrides = overrides)
  }
}
