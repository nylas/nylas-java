package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

class Credentials(client: NylasClient) : Resource<Credential>(client, Credential::class.java) {
  /**
   * Return all Credentials
   * @param provider The provider associated to the credential to list from
   * @param queryParams The query parameters to include in the request
   * @return The list of Credentials
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(provider: AuthProvider, queryParams: ListCredentialsQueryParams? = null): ListResponse<Credential> {
    val path = String.format("v3/connectors/%s/creds", provider.value)
    return listResource(path, queryParams)
  }

  /**
   * Return a Credential
   * @param provider The provider associated to the connector to retrieve.
   * @param credentialsId The id of the credentials to retrieve.
   * @return The Credential
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(provider: AuthProvider, credentialsId: String): Response<Credential> {
    val path = String.format("v3/connectors/%s/creds/%s", provider.value, credentialsId)
    return findResource(path)
  }

  /**
   * Create a Credential
   * @param provider The provider associated to the credential being created.
   * @param requestBody The values to create the Credential with
   * @return The created Credential
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(provider: AuthProvider, requestBody: CreateCredentialRequest): Response<Credential> {
    val path = String.format("v3/connectors/%s/creds", provider.value)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateCredentialRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody)
  }

  /**
   * Update a Credential
   * @param provider The provider associated to the connector to update from.
   * @param credentialsId The id of the credentials to retrieve.
   * @param requestBody The id of the credentials to update
   * @return The updated Credential
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(provider: AuthProvider, credentialsId: String, requestBody: UpdateCredentialRequest): Response<Credential> {
    val path = String.format("v3/connectors/%s/creds/%s", provider.value, credentialsId)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateCredentialRequest::class.java)
      .toJson(requestBody)

    return patchResource(path, serializedRequestBody)
  }

  /**
   * Delete a Credential
   * @param provider The provider associated to the connector to delete.
   * @param credentialsId The id of the credentials to delete.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(provider: AuthProvider, credentialsId: String): DeleteResponse {
    val path = String.format("v3/connectors/%s/creds/%s", provider.value, credentialsId)
    return destroyResource(path)
  }
}
