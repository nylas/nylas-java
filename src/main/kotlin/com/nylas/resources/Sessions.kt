package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
/**
 * Nylas Sessions API
 *
 * The Nylas Sessions API allows you to create and delete sessions on user Schedulerconfigurations.
 *
 * @param client The configured Nylas API client
 */
class Sessions(client: NylasClient) : Resource<Session>(client, Session::class.java) {
  /**
   * Create a Session
   * @param requestBody The values to create the session with
   * @param overrides Optional request overrides to apply
   * @return The created session
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreateSessionRequest, overrides: RequestOverrides? = null): Response<Session> {
    val path = "v3/scheduling/sessions"
    val adapter = JsonHelper.moshi().adapter(CreateSessionRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Session
   * @param sessionId The ID of the session to delete
   * @param overrides Optional request overrides to apply
   * @return The deleted response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(sessionId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = "v3/scheduling/sessions/$sessionId"
    return destroyResource(path, overrides = overrides)
  }
}
