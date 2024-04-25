package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.FileUtils
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types

class Messages(client: NylasClient) : Resource<Message>(client, Message::class.java) {
  /**
   * Access the Smart Compose collection of endpoints
   * @return The Smart Compose collection of endpoints
   */
  fun smartCompose(): SmartCompose {
    return SmartCompose(client)
  }

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
   * @param messageId The id of the Message to retrieve.
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
   * @param messageId The id of the Message to update.
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
   * @param messageId The id of the Message to delete.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, messageId: String): DeleteResponse {
    val path = String.format("v3/grants/%s/messages/%s", identifier, messageId)
    return destroyResource(path)
  }

  /**
   * Send an email
   * @param identifier The identifier of the grant to act upon
   * @param requestBody The values to send the email with
   * @return The sent email
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun send(identifier: String, requestBody: SendMessageRequest): Response<Message> {
    val path = String.format("v3/grants/%s/messages/send", identifier)
    val responseType = Types.newParameterizedType(Response::class.java, Message::class.java)
    val adapter = JsonHelper.moshi().adapter(SendMessageRequest::class.java)

    // Use form data only if the attachment size is greater than 3mb
    val attachmentSize = requestBody.attachments?.sumOf { it.size } ?: 0

    return if (attachmentSize >= FileUtils.MAXIMUM_JSON_ATTACHMENT_SIZE) {
      val attachmentLessPayload = requestBody.copy(attachments = null)
      val serializedRequestBody = adapter.toJson(attachmentLessPayload)
      val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)

      client.executeFormRequest(path, NylasClient.HttpMethod.POST, multipart, responseType)
    } else {
      val serializedRequestBody = adapter.toJson(requestBody)
      createResource(path, serializedRequestBody)
    }
  }

  /**
   * Retrieve your scheduled messages
   * @param identifier The identifier of the grant to act upon
   * @return The list of scheduled messages
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun listScheduledMessages(identifier: String): Response<ScheduledMessagesList> {
    val path = String.format("v3/grants/%s/messages/schedules", identifier)
    val responseType = Types.newParameterizedType(Response::class.java, ScheduledMessagesList::class.java)
    return client.executeGet(path, responseType)
  }

  /**
   * Retrieve a scheduled message
   * @param identifier The identifier of the grant to act upon
   * @param scheduleId The id of the scheduled message to retrieve
   * @return The scheduled message
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun findScheduledMessage(identifier: String, scheduleId: String): Response<ScheduledMessage> {
    val path = String.format("v3/grants/%s/messages/schedules/%s", identifier, scheduleId)
    val responseType = Types.newParameterizedType(Response::class.java, ScheduledMessage::class.java)
    return client.executeGet(path, responseType)
  }

  /**
   * Stop a scheduled message
   * @param identifier The identifier of the grant to act upon
   * @param scheduleId The id of the scheduled message to stop
   * @return The confirmation of the stopped scheduled message
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun stopScheduledMessage(identifier: String, scheduleId: String): Response<StopScheduledMessageResponse> {
    val path = String.format("v3/grants/%s/messages/schedules/%s", identifier, scheduleId)
    val responseType = Types.newParameterizedType(Response::class.java, StopScheduledMessageResponse::class.java)
    return client.executeDelete(path, responseType)
  }

  /**
   * Clean messages
   * @param identifier The identifier of the grant to act upon
   * @param requestBody The values to clean the message with
   * @return Clean multiple messages
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun cleanConversation(identifier: String, requestBody: CleanMessageRequest): Response<CleanMessageResponse> {
    val path = String.format("v3/grants/%s/messages/clean", identifier)
    val adapter = JsonHelper.moshi().adapter(CleanMessageRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    val responseType = Types.newParameterizedType(Response::class.java, CleanMessageResponse::class.java)
    return client.executePut(path, responseType, serializedRequestBody)
  }
}
