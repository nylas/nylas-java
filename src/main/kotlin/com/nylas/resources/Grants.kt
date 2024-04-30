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
   * @param overrides Optional request overrides to apply
   * @return The list of Grants
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(queryParams: ListGrantsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Grant> {
    val path = "v3/grants"
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a Grant
   * @param grantId The id of the Grant to retrieve.
   * @param overrides Optional request overrides to apply
   * @return The Grant
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(grantId: String, overrides: RequestOverrides? = null): Response<Grant> {
    val path = String.format("v3/grants/%s", grantId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Update a Grant
   * @param grantId The id of the Grant to update.
   * @param requestBody The values to update the Grant with
   * @param overrides Optional request overrides to apply
   * @return The updated Grant
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(grantId: String, requestBody: UpdateGrantRequest, overrides: RequestOverrides? = null): Response<Grant> {
    val path = String.format("v3/grants/%s", grantId)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateGrantRequest::class.java)
      .toJson(requestBody)

    return patchResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Grant
   * @param grantId The id of the Grant to delete.
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(grantId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/grants/%s", grantId)
    return destroyResource(path, overrides = overrides)
  }
}
