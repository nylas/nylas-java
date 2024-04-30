package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.squareup.moshi.Types

/**
 * Nylas Applications API
 *
 * This endpoint allows for getting application details as well as redirect URI operations.
 */
class Applications(private val client: NylasClient) {
  /**
   * Access the collection of redirect URI related API endpoints.
   * @return The collection of redirect URI related API endpoints.
   */
  fun redirectUris(): RedirectUris {
    return RedirectUris(client)
  }

  /**
   * Get application details
   * @param overrides Optional request overrides to apply
   * @return The application details
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun getDetails(overrides: RequestOverrides? = null): Response<ApplicationDetails> {
    val path = "v3/applications"
    val responseType = Types.newParameterizedType(Response::class.java, ApplicationDetails::class.java)
    return client.executeGet(path, responseType, overrides = overrides)
  }
}
