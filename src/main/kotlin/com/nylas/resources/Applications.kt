package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.ApplicationDetails
import com.nylas.models.NylasApiError
import com.nylas.models.Response
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
   * @return The application details
   */
  @Throws(NylasApiError::class)
  fun getDetails(): Response<ApplicationDetails> {
    val path = "v3/applications"
    val responseType = Types.newParameterizedType(Response::class.java, ApplicationDetails::class.java)
    return client.executeGet(path, responseType)
  }
}
