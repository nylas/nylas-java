package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.squareup.moshi.Types

/**
 * A collection of provider related API endpoints.
 *
 * These endpoints allow for the listing and detection of providers.
 *
 * @param client The configured Nylas API client
 */
class Providers(
  private val client: NylasClient,
  private val clientId: String,
) : Resource<Provider>(client, Provider::class.java) {
  /**
   * List your available OAuth providers
   * @return The list of providers
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun list(): ListResponse<Provider> {
    val path = "v3/grants/connect/providers/find?client_id=$clientId"
    return listResource(path)
  }

  /**
   * Detect provider from email address
   * @param params The parameters to include in the request
   * @return The detected provider, if found
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun detect(params: ProviderDetectParams): Response<ProviderDetectResponse> {
    if (params.clientId == null) {
      params.clientId = clientId
    }
    val path = "v3/grants/connect/providers/detect"
    val responseType = Types.newParameterizedType(Response::class.java, ProviderDetectResponse::class.java)

    return client.executePost(path, responseType, queryParams = params)
  }
}
