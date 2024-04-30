package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

class Threads(client: NylasClient) : Resource<Thread>(client, Thread::class.java) {
  /**
   * Return all Threads
   * @param identifier The identifier of the grant to act upon
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The list of Threads
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListThreadsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Thread> {
    val path = String.format("v3/grants/%s/threads", identifier)
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a Thread
   * @param identifier The identifier of the grant to act upon
   * @param threadId The id of the Thread to retrieve.
   * @param overrides Optional request overrides to apply
   * @return The Thread
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(identifier: String, threadId: String, overrides: RequestOverrides? = null): Response<Thread> {
    val path = String.format("v3/grants/%s/threads/%s", identifier, threadId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Update a Thread
   * @param identifier The identifier of the grant to act upon
   * @param threadId The id of the Thread to update.
   * @param requestBody The values to update the Thread with
   * @param overrides Optional request overrides to apply
   * @return The updated Thread
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(identifier: String, threadId: String, requestBody: UpdateThreadRequest, overrides: RequestOverrides? = null): Response<Thread> {
    val path = String.format("v3/grants/%s/threads/%s", identifier, threadId)
    val adapter = JsonHelper.moshi().adapter(UpdateThreadRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Thread
   * @param identifier The identifier of the grant to act upon
   * @param threadId The id of the Thread to delete.
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(identifier: String, threadId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/grants/%s/threads/%s", identifier, threadId)
    return destroyResource(path, overrides = overrides)
  }
}
