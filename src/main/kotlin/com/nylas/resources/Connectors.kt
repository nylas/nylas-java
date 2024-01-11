package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

class Connectors(client: NylasClient) : Resource<Connector>(client, Connector::class.java) {
  /**
   * Access the Credentials API
   * @return The Credentials API
   */
  fun credentials(): Credentials {
    return Credentials(client)
  }

  /**
   * Return all Connectors
   * @param queryParams The query parameters to include in the request
   * @return The list of Connectors
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(queryParams: ListConnectorsQueryParams? = null): ListResponse<Connector> {
    val path = "v3/connectors"
    return listResource(path, queryParams)
  }

  /**
   * Return a Connector
   * @param provider The provider associated to the connector to retrieve.
   * @return The Connector
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(provider: AuthProvider): Response<Connector> {
    val path = String.format("v3/connectors/%s", provider.value)
    return findResource(path)
  }

  /**
   * Create a Connector
   * @param requestBody The values to create the Connector with
   * @return The created Connector
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(requestBody: CreateConnectorRequest): Response<Connector> {
    val path = "v3/connectors"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateConnectorRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody)
  }

  /**
   * Update a Connector
   * @param provider The provider associated to the connector to update.
   * @param requestBody The values to update the Connector with
   * @return The updated Connector
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(provider: AuthProvider, requestBody: UpdateConnectorRequest): Response<Connector> {
    val path = String.format("v3/connectors/%s", provider.value)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateConnectorRequest::class.java)
      .toJson(requestBody)

    return patchResource(path, serializedRequestBody)
  }

  /**
   * Delete a Connector
   * @param provider The provider associated to the connector to update.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(provider: AuthProvider): DeleteResponse {
    val path = String.format("v3/connectors/%s", provider.value)
    return destroyResource(path)
  }
}
