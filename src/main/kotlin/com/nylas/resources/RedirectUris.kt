package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

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
   * @param overrides Optional request overrides to apply
   * @return The list of Redirect URIs
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(overrides: RequestOverrides? = null): ListResponse<RedirectUri> {
    val path = "v3/applications/redirect-uris"
    return listResource(path, overrides = overrides)
  }

  /**
   * Return a Redirect URI
   * @param redirectUriId The id of the Redirect URI to retrieve.
   * @param overrides Optional request overrides to apply
   * @return The Redirect URI
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(redirectUriId: String, overrides: RequestOverrides? = null): Response<RedirectUri> {
    val path = String.format("v3/applications/redirect-uris/%s", redirectUriId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a Redirect URI
   * @param requestBody The values to create the Redirect URI with
   * @param overrides Optional request overrides to apply
   * @return The created Redirect URI
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreateRedirectUriRequest, overrides: RequestOverrides? = null): Response<RedirectUri> {
    val path = "v3/applications/redirect-uris"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateRedirectUriRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a Redirect URI
   * @param redirectUriId The id of the Redirect URI to update.
   * @param requestBody The values to update the Redirect URI with
   * @param overrides Optional request overrides to apply
   * @return The updated Redirect URI
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(redirectUriId: String, requestBody: UpdateRedirectUriRequest, overrides: RequestOverrides? = null): Response<RedirectUri> {
    val path = String.format("v3/applications/redirect-uris/%s", redirectUriId)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateRedirectUriRequest::class.java)
      .toJson(requestBody)

    return patchResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Redirect URI
   * @param redirectUriId The id of the Redirect URI to delete.
   * @param overrides Optional request overrides to apply
   * @return The deleted Redirect URI
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(redirectUriId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/applications/redirect-uris/%s", redirectUriId)
    return destroyResource(path, overrides = overrides)
  }
}
