package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

class Threads(client: NylasClient) : Resource<Thread>(client, Thread::class.java) {
  /**
   * Return all Threads
   * @param identifier The identifier of the grant to act upon
   * @param queryParams The query parameters to include in the request
   * @return The list of Threads
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListThreadsQueryParams? = null): ListResponse<Thread> {
    val path = String.format("v3/grants/%s/threads", identifier)
    return listResource(path, queryParams)
  }

  /**
   * Return a Thread
   * @param identifier The identifier of the grant to act upon
   * @param threadId The id of the Thread to retrieve. Use "primary" to refer to the primary thread associated with grant.
   * @return The Thread
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(identifier: String, threadId: String): Response<Thread> {
    val path = String.format("v3/grants/%s/threads/%s", identifier, threadId)
    return findResource(path)
  }

  /**
   * Update a Thread
   * @param identifier The identifier of the grant to act upon
   * @param threadId The id of the Thread to update. Use "primary" to refer to the primary thread associated with grant.
   * @param requestBody The values to update the Thread with
   * @return The updated Thread
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(identifier: String, threadId: String, requestBody: UpdateThreadRequest): Response<Thread> {
    val path = String.format("v3/grants/%s/threads/%s", identifier, threadId)
    val adapter = JsonHelper.moshi().adapter(UpdateThreadRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody)
  }

  /**
   * Delete a Thread
   * @param identifier The identifier of the grant to act upon
   * @param threadId The id of the Thread to delete. Use "primary" to refer to the primary thread associated with grant.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, threadId: String): DeleteResponse {
    val path = String.format("v3/grants/%s/threads/%s", identifier, threadId)
    return destroyResource(path)
  }
}
