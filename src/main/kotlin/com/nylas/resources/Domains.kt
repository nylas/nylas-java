package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.FileUtils
import com.nylas.util.JsonHelper
import com.nylas.util.PathEncoder
import com.squareup.moshi.Types

class Domains(client: NylasClient) : Resource<Message>(client, Message::class.java) {
  private fun requireServiceAccountSigning(overrides: RequestOverrides): RequestOverrides {
    val headers = overrides.headers.orEmpty()
    val normalizedHeaders = headers.entries.associate { it.key.lowercase() to it.value }
    val missingHeaders = SERVICE_ACCOUNT_SIGNING_HEADERS.filter {
      normalizedHeaders[it.lowercase()].isNullOrBlank()
    }

    require(missingHeaders.isEmpty()) {
      "Manage Domains endpoints require Nylas Service Account signing headers: ${missingHeaders.joinToString()}"
    }

    return overrides
  }

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
   * @param overrides Request overrides containing Nylas Service Account signing headers
   * @return The list of managed domains
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun list(overrides: RequestOverrides): ListResponse<Domain> {
    return list(null, overrides)
  }

  /**
   * Return all managed domains for the caller's organization.
   * @param queryParams Optional query parameters to apply
   * @param overrides Request overrides containing Nylas Service Account signing headers
   * @return The list of managed domains
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun list(queryParams: ListDomainsQueryParams? = null, overrides: RequestOverrides): ListResponse<Domain> {
    val signedOverrides = requireServiceAccountSigning(overrides)
    val responseType = Types.newParameterizedType(ListResponse::class.java, Domain::class.java)
    return client.executeGet("v3/admin/domains", responseType, queryParams, signedOverrides)
  }

  /**
   * Return a managed domain.
   * @param domainId The ID or domain address of the domain to retrieve
   * @param overrides Request overrides containing Nylas Service Account signing headers
   * @return The managed domain
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(domainId: String, overrides: RequestOverrides): Response<Domain> {
    val signedOverrides = requireServiceAccountSigning(overrides)
    val path = String.format("v3/admin/domains/%s", PathEncoder.encode(domainId))
    val responseType = Types.newParameterizedType(Response::class.java, Domain::class.java)
    return client.executeGet(path, responseType, overrides = signedOverrides)
  }

  /**
   * Create a managed domain.
   * @param requestBody The values to create the domain with
   * @param overrides Request overrides containing Nylas Service Account signing headers
   * @return The created managed domain
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(requestBody: CreateDomainRequest, overrides: RequestOverrides): Response<Domain> {
    val signedOverrides = requireServiceAccountSigning(overrides)
    val path = "v3/admin/domains"
    val responseType = Types.newParameterizedType(Response::class.java, Domain::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(CreateDomainRequest::class.java).toJson(requestBody)
    return client.executePost(path, responseType, serializedRequestBody, overrides = signedOverrides)
  }

  /**
   * Update a managed domain.
   * @param domainId The ID or domain address of the domain to update
   * @param requestBody The values to update the domain with
   * @param overrides Request overrides containing Nylas Service Account signing headers
   * @return The updated managed domain fields
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(domainId: String, requestBody: UpdateDomainRequest, overrides: RequestOverrides): Response<Domain> {
    val signedOverrides = requireServiceAccountSigning(overrides)
    val path = String.format("v3/admin/domains/%s", PathEncoder.encode(domainId))
    val responseType = Types.newParameterizedType(Response::class.java, Domain::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(UpdateDomainRequest::class.java).toJson(requestBody)
    return client.executePut(path, responseType, serializedRequestBody, overrides = signedOverrides)
  }

  /**
   * Delete a managed domain.
   * @param domainId The ID or domain address of the domain to delete
   * @param overrides Request overrides containing Nylas Service Account signing headers
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(domainId: String, overrides: RequestOverrides): DeleteResponse {
    val signedOverrides = requireServiceAccountSigning(overrides)
    val path = String.format("v3/admin/domains/%s", PathEncoder.encode(domainId))
    return client.executeDelete(path, DeleteResponse::class.java, overrides = signedOverrides)
  }

  /**
   * Get DNS record information for a domain verification type.
   * @param domainId The ID or domain address of the domain
   * @param requestBody The verification type to inspect
   * @param overrides Request overrides containing Nylas Service Account signing headers
   * @return The domain verification result
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun info(
    domainId: String,
    requestBody: DomainVerificationRequest,
    overrides: RequestOverrides,
  ): Response<DomainVerificationResult> {
    val signedOverrides = requireServiceAccountSigning(overrides)
    val path = String.format("v3/admin/domains/%s/info", PathEncoder.encode(domainId))
    val responseType = Types.newParameterizedType(Response::class.java, DomainVerificationResult::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(DomainVerificationRequest::class.java).toJson(requestBody)
    return client.executePost(path, responseType, serializedRequestBody, overrides = signedOverrides)
  }

  /**
   * Trigger a DNS verification check for a domain verification type.
   * @param domainId The ID or domain address of the domain
   * @param requestBody The verification type to verify
   * @param overrides Request overrides containing Nylas Service Account signing headers
   * @return The domain verification result
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun verify(
    domainId: String,
    requestBody: DomainVerificationRequest,
    overrides: RequestOverrides,
  ): Response<DomainVerificationResult> {
    val signedOverrides = requireServiceAccountSigning(overrides)
    val path = String.format("v3/admin/domains/%s/verify", PathEncoder.encode(domainId))
    val responseType = Types.newParameterizedType(Response::class.java, DomainVerificationResult::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(DomainVerificationRequest::class.java).toJson(requestBody)
    return client.executePost(path, responseType, serializedRequestBody, overrides = signedOverrides)
  }

  companion object {
    private val SERVICE_ACCOUNT_SIGNING_HEADERS = listOf(
      "X-Nylas-Kid",
      "X-Nylas-Timestamp",
      "X-Nylas-Nonce",
      "X-Nylas-Signature",
    )
  }
}
