package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.CodeExchangeResponse
import com.nylas.models.ExchangeCodeRequest
import com.nylas.models.NylasApiError
import com.nylas.models.Response
import com.nylas.util.JsonHelper
import java.io.IOException

class Auth(
  private val client: NylasClient,
  private val clientId: String,
  private val clientSecret: String
) {
  @Throws(IOException::class, NylasApiError::class)
  fun exchangeCodeForToken(request: ExchangeCodeRequest): Response<CodeExchangeResponse> {
    val path = "/v3/connect/token"
    val adapter = JsonHelper.moshi().adapter(ExchangeCodeRequest::class.java)
    val serializedRequestBody = adapter.toJson(request)
    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody)
  }
}