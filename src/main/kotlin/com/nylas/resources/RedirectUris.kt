package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import java.io.IOException

class RedirectUris(client: NylasClient) : Resource<RedirectUri>(client, RedirectUri::class.java) {
  @Throws(IOException::class, NylasApiError::class)
  fun list(): ListResponse<RedirectUri> {
    val path = "v3/applications/redirect-uris"
    return listResource(path)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun find(redirectUriId: String): Response<RedirectUri> {
    val path = String.format("v3/grants/redirect-uris/%s", redirectUriId)
    return findResource(path)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun create(requestBody: CreateRedirectUriRequest): Response<RedirectUri> {
    val path = "v3/applications/redirect-uris"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateRedirectUriRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun update(redirectUriId: String, requestBody: UpdateRedirectUriRequest): Response<RedirectUri> {
    val path = String.format("v3/grants/redirect-uris/%s", redirectUriId)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateRedirectUriRequest::class.java)
      .toJson(requestBody)

    return patchResource(path, serializedRequestBody)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun destroy(redirectUriId: String): DeleteResponse {
    val path = String.format("v3/grants/redirect-uris/%s", redirectUriId)
    return destroyResource(path)
  }
}
