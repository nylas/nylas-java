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

    val attachmentLessPayload = requestBody.copy(attachments = null)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateDraftRequest::class.java)
      .toJson(attachmentLessPayload)
    val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)
    val responseType = Types.newParameterizedType(Response::class.java, Draft::class.java)

    return client.executeFormRequest(path, NylasClient.HttpMethod.POST, multipart, responseType)
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

    val attachmentLessPayload = requestBody.copy(attachments = null)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateDraftRequest::class.java)
      .toJson(attachmentLessPayload)
    val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)

    return client.executeFormRequest(path, NylasClient.HttpMethod.PUT, multipart, Draft::class.java)
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
}
