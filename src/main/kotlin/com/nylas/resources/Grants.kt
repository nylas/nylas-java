package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import java.io.IOException

class Grants(client: NylasClient) : Resource<Grant>(client, Grant::class.java) {
  @Throws(IOException::class, NylasApiError::class)
  @JvmOverloads
  fun list(queryParams: ListGrantsQueryParams? = null): ListResponse<Grant> {
    val path = "/v3/grants"
    return listResource(path, queryParams)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun find(grantId: String): Response<Grant> {
    val path = String.format("/v3/grants/%s", grantId)
    return findResource(path)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun create(requestBody: CreateGrantRequest): Response<Grant> {
    val path = "/v3/grants"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateGrantRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun update(calendarId: String, requestBody: UpdateGrantRequest): Response<Grant> {
    val path = String.format("/v3/grants/%s", calendarId)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateGrantRequest::class.java)
      .toJson(requestBody)

    return updateResource(path, serializedRequestBody)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun destroy(grantId: String): DeleteResponse {
    val path = String.format("/v3/grants/%s", grantId)
    return destroyResource(path)
  }
}