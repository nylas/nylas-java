package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.FileUtils
import com.nylas.util.JsonHelper
import com.nylas.util.PathEncoder
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

  /**
   * Return all managed domains for the caller's organization.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The list of managed domains
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(queryParams: ListDomainsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Domain> {
    val responseType = Types.newParameterizedType(ListResponse::class.java, Domain::class.java)
    return client.executeGet("v3/admin/domains", responseType, queryParams, overrides)
  }

  /**
   * Return a managed domain.
   * @param domainId The ID or domain address of the domain to retrieve
   * @param overrides Optional request overrides to apply
   * @return The managed domain
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(domainId: String, overrides: RequestOverrides? = null): Response<Domain> {
    val path = String.format("v3/admin/domains/%s", PathEncoder.encode(domainId))
    val responseType = Types.newParameterizedType(Response::class.java, Domain::class.java)
    return client.executeGet(path, responseType, overrides = overrides)
  }

  /**
   * Create a managed domain.
   * @param requestBody The values to create the domain with
   * @param overrides Optional request overrides to apply
   * @return The created managed domain
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreateDomainRequest, overrides: RequestOverrides? = null): Response<Domain> {
    val path = "v3/admin/domains"
    val responseType = Types.newParameterizedType(Response::class.java, Domain::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(CreateDomainRequest::class.java).toJson(requestBody)
    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a managed domain.
   * @param domainId The ID or domain address of the domain to update
   * @param requestBody The values to update the domain with
   * @param overrides Optional request overrides to apply
   * @return The updated managed domain fields
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(domainId: String, requestBody: UpdateDomainRequest, overrides: RequestOverrides? = null): Response<Domain> {
    val path = String.format("v3/admin/domains/%s", PathEncoder.encode(domainId))
    val responseType = Types.newParameterizedType(Response::class.java, Domain::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(UpdateDomainRequest::class.java).toJson(requestBody)
    return client.executePut(path, responseType, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a managed domain.
   * @param domainId The ID or domain address of the domain to delete
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(domainId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/admin/domains/%s", PathEncoder.encode(domainId))
    return client.executeDelete(path, DeleteResponse::class.java, overrides = overrides)
  }

  /**
   * Get DNS record information for a domain verification type.
   * @param domainId The ID or domain address of the domain
   * @param requestBody The verification type to inspect
   * @param overrides Optional request overrides to apply
   * @return The domain verification result
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun info(
    domainId: String,
    requestBody: DomainVerificationRequest,
    overrides: RequestOverrides? = null,
  ): Response<DomainVerificationResult> {
    val path = String.format("v3/admin/domains/%s/info", PathEncoder.encode(domainId))
    val responseType = Types.newParameterizedType(Response::class.java, DomainVerificationResult::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(DomainVerificationRequest::class.java).toJson(requestBody)
    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }

  /**
   * Trigger a DNS verification check for a domain verification type.
   * @param domainId The ID or domain address of the domain
   * @param requestBody The verification type to verify
   * @param overrides Optional request overrides to apply
   * @return The domain verification result
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun verify(
    domainId: String,
    requestBody: DomainVerificationRequest,
    overrides: RequestOverrides? = null,
  ): Response<DomainVerificationResult> {
    val path = String.format("v3/admin/domains/%s/verify", PathEncoder.encode(domainId))
    val responseType = Types.newParameterizedType(Response::class.java, DomainVerificationResult::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(DomainVerificationRequest::class.java).toJson(requestBody)
    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }
}
