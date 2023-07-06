package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.squareup.moshi.Types
import java.io.IOException

class Providers(
  private val client: NylasClient,
  private val clientId: String,
) : Resource<Provider>(client, Provider::class.java) {
  @Throws(IOException::class, NylasApiError::class)
  fun list(): ListResponse<Provider> {
    val path = "v3/grants/connect/providers/find?client_id=$clientId"
    return listResource(path)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun detect(params: ProviderDetectParams): Response<ProviderDetectResponse> {
    if (params.clientId == null) {
      params.clientId = clientId
    }
    val path = "v3/grants/connect/providers/detect"
    val responseType = Types.newParameterizedType(Response::class.java, ProviderDetectResponse::class.java)

    return client.executePost(path, responseType, queryParams = params)
  }
}
