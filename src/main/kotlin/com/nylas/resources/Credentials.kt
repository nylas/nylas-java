package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

class Credentials(client: NylasClient) : Resource<Credential>(client, Credential::class.java) {
  /**
   * Return all Credentials
   * @param provider The provider associated to the credential to list from
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The list of Credentials
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(provider: AuthProvider, queryParams: ListCredentialsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Credential> {
    val path = String.format("v3/connectors/%s/creds", provider.value)
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a Credential
   * @param provider The provider associated to the connector to retrieve.
   * @param credentialsId The id of the credentials to retrieve.
   * @param overrides Optional request overrides to apply
   * @return The Credential
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(provider: AuthProvider, credentialsId: String, overrides: RequestOverrides? = null): Response<Credential> {
    val path = String.format("v3/connectors/%s/creds/%s", provider.value, credentialsId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a Credential
   * @param provider The provider associated to the credential being created.
   * @param requestBody The values to create the Credential with
   * @param overrides Optional request overrides to apply
   * @return The created Credential
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(provider: AuthProvider, requestBody: CreateCredentialRequest, overrides: RequestOverrides? = null): Response<Credential> {
    val path = String.format("v3/connectors/%s/creds", provider.value)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateCredentialRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a Credential
   * @param provider The provider associated to the connector to update from.
   * @param credentialsId The id of the credentials to retrieve.
   * @param requestBody The id of the credentials to update
   * @param overrides Optional request overrides to apply
   * @return The updated Credential
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(provider: AuthProvider, credentialsId: String, requestBody: UpdateCredentialRequest, overrides: RequestOverrides? = null): Response<Credential> {
    val path = String.format("v3/connectors/%s/creds/%s", provider.value, credentialsId)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateCredentialRequest::class.java)
      .toJson(requestBody)

    return patchResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Credential
   * @param provider The provider associated to the connector to delete.
   * @param credentialsId The id of the credentials to delete.
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(provider: AuthProvider, credentialsId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/connectors/%s/creds/%s", provider.value, credentialsId)
    return destroyResource(path, overrides = overrides)
  }
}
