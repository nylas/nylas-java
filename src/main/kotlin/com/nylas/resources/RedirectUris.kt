package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import java.io.IOException

/**
 * A collection of redirect URI related API endpoints.
 *
 * These endpoints allows for the management of redirect URIs.
 *
 * @param client The configured Nylas API client
 */
class RedirectUris(client: NylasClient) : Resource<RedirectUri>(client, RedirectUri::class.java) {
  /**
   * Return all Redirect URIs
   * @return The list of Redirect URIs
   */
  @Throws(IOException::class, NylasApiError::class)
  fun list(): ListResponse<RedirectUri> {
    val path = "v3/applications/redirect-uris"
    return listResource(path)
  }

  /**
   * Return a Redirect URI
   * @param redirectUriId The id of the Redirect URI to retrieve.
   * @return The Redirect URI
   */
  @Throws(IOException::class, NylasApiError::class)
  fun find(redirectUriId: String): Response<RedirectUri> {
    val path = String.format("v3/grants/redirect-uris/%s", redirectUriId)
    return findResource(path)
  }

  /**
   * Create a Redirect URI
   * @param requestBody The values to create the Redirect URI with
   * @return The created Redirect URI
   */
  @Throws(IOException::class, NylasApiError::class)
  fun create(requestBody: CreateRedirectUriRequest): Response<RedirectUri> {
    val path = "v3/applications/redirect-uris"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateRedirectUriRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody)
  }

  /**
   * Update a Redirect URI
   * @param redirectUriId The id of the Redirect URI to update.
   * @param requestBody The values to update the Redirect URI with
   * @return The updated Redirect URI
   */
  @Throws(IOException::class, NylasApiError::class)
  fun update(redirectUriId: String, requestBody: UpdateRedirectUriRequest): Response<RedirectUri> {
    val path = String.format("v3/grants/redirect-uris/%s", redirectUriId)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateRedirectUriRequest::class.java)
      .toJson(requestBody)

    return patchResource(path, serializedRequestBody)
  }

  /**
   * Delete a Redirect URI
   * @param redirectUriId The id of the Redirect URI to delete.
   * @return The deleted Redirect URI
   */
  @Throws(IOException::class, NylasApiError::class)
  fun destroy(redirectUriId: String): DeleteResponse {
    val path = String.format("v3/grants/redirect-uris/%s", redirectUriId)
    return destroyResource(path)
  }
}
