package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.FileUtils
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types

class Domains(client: NylasClient) : Resource<Message>(client, Message::class.java) {

  /**
   * Send a transactional email from a verified domain.
   * @param domainName The verified domain name to send from
   * @param requestBody The values to send the email with
   * @param overrides Optional request overrides to apply
   * @return The sent message
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun sendTransactionalEmail(
    domainName: String,
    requestBody: SendTransactionalEmailRequest,
    overrides: RequestOverrides? = null,
  ): Response<Message> {
    val path = String.format("v3/domains/%s/messages/send", domainName)
    val responseType = Types.newParameterizedType(Response::class.java, Message::class.java)
    val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)

    val attachmentSize = requestBody.attachments?.sumOf { it.size } ?: 0
    return if (attachmentSize >= FileUtils.MAXIMUM_JSON_ATTACHMENT_SIZE) {
      val attachmentLessPayload = requestBody.copy(attachments = null)
      val serializedRequestBody = adapter.toJson(attachmentLessPayload)
      val multipart = FileUtils.buildFormRequest(requestBody, serializedRequestBody)
      client.executeFormRequest(path, NylasClient.HttpMethod.POST, multipart, responseType, overrides = overrides)
    } else {
      val serializedRequestBody = adapter.toJson(requestBody)
      createResource(path, serializedRequestBody, overrides = overrides)
    }
  }
}
