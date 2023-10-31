package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.FileUtils
import com.nylas.util.JsonHelper

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
   * @param draftId The id of the Draft to retrieve. Use "primary" to refer to the primary draft associated with grant.
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
   * @param draftId The id of the Draft to update. Use "primary" to refer to the primary draft associated with grant.
   * @param requestBody The values to create the Draft with
   * @return The updated Draft
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(identifier: String, draftId: String, requestBody: CreateDraftRequest): Response<Draft> {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)

    val attachmentLessPayload = requestBody.copy(attachments = null)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateDraftRequest::class.java)
      .toJson(attachmentLessPayload)
    val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)

    return client.executeFormRequest(path, NylasClient.HttpMethod.POST, multipart, Draft::class.java)
  }

  /**
   * Update a Draft
   * @param identifier The identifier of the grant to act upon
   * @param draftId The id of the Draft to update. Use "primary" to refer to the primary draft associated with grant.
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
   * @param draftId The id of the Draft to delete. Use "primary" to refer to the primary draft associated with grant.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, draftId: String): DeleteResponse {
    val path = String.format("v3/grants/%s/drafts/%s", identifier, draftId)
    return destroyResource(path)
  }
}
