package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import kotlin.jvm.Throws

/**
 * A collection of Smart Compose related API endpoints.
 *
 * These endpoints allow for the generation of message suggestions.
 *
 * @param client The configured Nylas API client
 */
class SmartCompose(private val client: NylasClient) {
  /**
   * Compose a message
   * @param identifier The identifier of the grant to act upon
   * @param requestBody The prompt that smart compose will use to generate a message suggestion
   * @param overrides Optional request overrides to apply
   * @return The generated message
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun composeMessage(identifier: String, requestBody: ComposeMessageRequest, overrides: RequestOverrides? = null): Response<ComposeMessageResponse> {
    val path = "v3/grants/$identifier/messages/smart-compose"

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(ComposeMessageRequest::class.java)
      .toJson(requestBody)
    val responseType = Types.newParameterizedType(Response::class.java, ComposeMessageResponse::class.java)

    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }

  /**
   * Compose a message reply
   * @param identifier The identifier of the grant to act upon
   * @param messageId The id of the message to reply to
   * @param requestBody The prompt that smart compose will use to generate a reply suggestion
   * @param overrides Optional request overrides to apply
   * @return The generated message reply
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun composeMessageReply(identifier: String, messageId: String, requestBody: ComposeMessageRequest, overrides: RequestOverrides? = null): Response<ComposeMessageResponse> {
    val path = "v3/grants/$identifier/messages/$messageId/smart-compose"

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(ComposeMessageRequest::class.java)
      .toJson(requestBody)
    val responseType = Types.newParameterizedType(Response::class.java, ComposeMessageResponse::class.java)

    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }
}
