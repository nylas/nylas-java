package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.ComposeMessageRequest
import com.nylas.models.ComposeMessageResponse
import com.nylas.models.Draft
import com.nylas.models.Response
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types

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
   * @property identifier The identifier of the grant to act upon
   * @property requestBody The prompt that smart compose will use to generate a message suggestion
   * @return The generated message
   */
  fun composeMessage(identifier: String, requestBody: ComposeMessageRequest): Response<ComposeMessageResponse> {
    val path = "v3/grants/$identifier/messages/smart-compose"

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(ComposeMessageRequest::class.java)
      .toJson(requestBody)
    val responseType = Types.newParameterizedType(Response::class.java, ComposeMessageResponse::class.java)

    return client.executePost(path, responseType, serializedRequestBody)
  }

  /**
   * Compose a message reply
   * @property identifier The identifier of the grant to act upon
   * @property messageId The id of the message to reply to
   * @property requestBody The prompt that smart compose will use to generate a reply suggestion
   * @return The generated message reply
   */
  fun composeMessageReply(identifier: String, messageId: String, requestBody: ComposeMessageRequest): Response<ComposeMessageResponse> {
    val path = "v3/grants/$identifier/messages/$messageId/smart-compose"

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(ComposeMessageRequest::class.java)
      .toJson(requestBody)
    val responseType = Types.newParameterizedType(Response::class.java, ComposeMessageResponse::class.java)

    return client.executePost(path, responseType, serializedRequestBody)
  }
}
