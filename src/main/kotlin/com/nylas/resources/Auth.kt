package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import java.io.IOException

class Auth(
  private val client: NylasClient,
  private val clientId: String,
  private val clientSecret: String
) {
  @Throws(IOException::class, NylasApiError::class)
  fun exchangeCodeForToken(request: CodeExchangeRequest): Response<CodeExchangeResponse> {
    val path = "/v3/connect/token"

    if(request.clientId == null) {
      request.clientId = clientId
    }
    if(request.clientSecret == null) {
      request.clientSecret = clientSecret
    }

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CodeExchangeRequest::class.java)
      .toJson(request)

    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun refreshAccessToken(request: TokenExchangeRequest): Response<CodeExchangeResponse> {
    val path = "/v3/connect/token"

    if(request.clientId == null) {
      request.clientId = clientId
    }
    if(request.clientSecret == null) {
      request.clientSecret = clientSecret
    }

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(TokenExchangeRequest::class.java)
      .toJson(request)

    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody)
  }
}