package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

/**
 * Nylas Grants API
 *
 * The Nylas Grants API allows for the management of grants.
 *
 * @param client The configured Nylas API client
 */
class Grants(client: NylasClient) : Resource<Grant>(client, Grant::class.java) {
  /**
   * Return all Grants
   * @param queryParams The query parameters to include in the request
   * @return The list of Grants
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(queryParams: ListGrantsQueryParams? = null): ListResponse<Grant> {
    val path = "v3/grants"
    return listResource(path, queryParams)
  }

  /**
   * Return a Grant
   * @param grantId The id of the Grant to retrieve.
   * @return The Grant
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(grantId: String): Response<Grant> {
    val path = String.format("v3/grants/%s", grantId)
    return findResource(path)
  }

  /**
   * Create a Grant via Custom Authentication
   * @param requestBody The values to create the Grant with
   * @return The created Grant
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(requestBody: CreateGrantRequest): Response<Grant> {
    val path = "v3/connect/custom"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateGrantRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody)
  }

  /**
   * Update a Grant
   * @param grantId The id of the Grant to update.
   * @param requestBody The values to update the Grant with
   * @return The updated Grant
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(grantId: String, requestBody: UpdateGrantRequest): Response<Grant> {
    val path = String.format("v3/grants/%s", grantId)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateGrantRequest::class.java)
      .toJson(requestBody)

    return updateResource(path, serializedRequestBody)
  }

  /**
   * Delete a Grant
   * @param grantId The id of the Grant to delete.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(grantId: String): DeleteResponse {
    val path = String.format("v3/grants/%s", grantId)
    return destroyResource(path)
  }
}
