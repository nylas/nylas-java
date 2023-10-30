package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

class Messages(client: NylasClient) : Resource<Message>(client, Message::class.java) {
  /**
   * Return all Messages
   * @param identifier The identifier of the grant to act upon
   * @param queryParams The query parameters to include in the request
   * @return The list of Messages
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListMessagesQueryParams? = null): ListResponse<Message> {
    val path = String.format("v3/grants/%s/messages", identifier)
    return listResource(path, queryParams)
  }

  /**
   * Return a Message
   * @param identifier The identifier of the grant to act upon
   * @param messageId The id of the Message to retrieve. Use "primary" to refer to the primary message associated with grant.
   * @return The Message
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(identifier: String, messageId: String): Response<Message> {
    val path = String.format("v3/grants/%s/messages/%s", identifier, messageId)
    return findResource(path)
  }

  /**
   * Update a Message
   * @param identifier The identifier of the grant to act upon
   * @param messageId The id of the Message to update. Use "primary" to refer to the primary message associated with grant.
   * @param requestBody The values to update the Message with
   * @return The updated Message
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(identifier: String, messageId: String, requestBody: UpdateMessageRequest): Response<Message> {
    val path = String.format("v3/grants/%s/messages/%s", identifier, messageId)
    val adapter = JsonHelper.moshi().adapter(UpdateMessageRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody)
  }

  /**
   * Delete a Message
   * @param identifier The identifier of the grant to act upon
   * @param messageId The id of the Message to delete. Use "primary" to refer to the primary message associated with grant.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, messageId: String): DeleteResponse {
    val path = String.format("v3/grants/%s/messages/%s", identifier, messageId)
    return destroyResource(path)
  }
}
