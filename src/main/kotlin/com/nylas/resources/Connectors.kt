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
   * @param overrides Optional request overrides to apply
   * @return The list of Connectors
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(queryParams: ListConnectorsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Connector> {
    val path = "v3/connectors"
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a Connector
   * @param provider The provider associated to the connector to retrieve.
   * @param overrides Optional request overrides to apply
   * @return The Connector
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(provider: AuthProvider, overrides: RequestOverrides? = null): Response<Connector> {
    val path = String.format("v3/connectors/%s", provider.value)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a Connector
   * @param requestBody The values to create the Connector with
   * @param overrides Optional request overrides to apply
   * @return The created Connector
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreateConnectorRequest, overrides: RequestOverrides? = null): Response<Connector> {
    val path = "v3/connectors"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateConnectorRequest::class.java)
      .toJson(requestBody)

    return createResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a Connector
   * @param provider The provider associated to the connector to update.
   * @param requestBody The values to update the Connector with
   * @param overrides Optional request overrides to apply
   * @return The updated Connector
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(provider: AuthProvider, requestBody: UpdateConnectorRequest, overrides: RequestOverrides? = null): Response<Connector> {
    val path = String.format("v3/connectors/%s", provider.value)
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(UpdateConnectorRequest::class.java)
      .toJson(requestBody)

    return patchResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Connector
   * @param provider The provider associated to the connector to update.
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(provider: AuthProvider, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/connectors/%s", provider.value)
    return destroyResource(path, overrides = overrides)
  }
}
