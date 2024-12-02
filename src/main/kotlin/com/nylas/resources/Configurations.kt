package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

/**
 * Nylas Configurations API
 *
 * The Nylas configurations API allows you to create new configurations or manage existing ones, as well as getting
 * configurations details for a user.
 *
 * Nylas Scheduler stores Configuration objects in the Scheduler database and loads
 * them as Scheduling Pages in the Scheduler UI.
 *
 * @param client The configured Nylas API client
 */
class Configurations(client: NylasClient) : Resource<Configuration>(client, Configuration::class.java) {

  /**
   * Return all Configurations
   * @param identifier The identifier of the Grant to act upon.
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The list of Configurations
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(
    identifier: String,
    queryParams: ListConfigurationsQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): ListResponse<Configuration> {
    val path = String.format("v3/grants/%s/scheduling/configurations", identifier)
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a Configuration
   * @param identifier The identifier of the Grant to act upon.
   * @param configId The identifier of the Configuration to get.
   * @param overrides Optional request overrides to apply
   * @return The Configuration object
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(
    identifier: String,
    configId: String,
    overrides: RequestOverrides? = null,
  ): Response<Configuration> {
    val path = String.format("v3/grants/%s/scheduling/configurations/%s", identifier, configId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a new Configuration
   * @param identifier The identifier of the Grant to act upon.
   * @param requestBody The data to create the Configuration with.
   * @param overrides Optional request overrides to apply
   * @return The Configuration object
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(
    identifier: String,
    requestBody: CreateConfigurationRequest,
    overrides: RequestOverrides? = null,
  ): Response<Configuration> {
    val path = String.format("v3/grants/%s/scheduling/configurations", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateConfigurationRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a Configuration
   * @param identifier The identifier of the Grant to act upon.
   * @param configId The identifier of the Configuration to update.
   * @param requestBody The data to update the Configuration with.
   * @param overrides Optional request overrides to apply
   * @return The Configuration object
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(
    identifier: String,
    configId: String,
    requestBody: UpdateConfigurationRequest,
    overrides: RequestOverrides? = null,
  ): Response<Configuration> {
    val path = String.format("v3/grants/%s/scheduling/configurations/%s", identifier, configId)
    val adapter = JsonHelper.moshi().adapter(UpdateConfigurationRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Configuration
   * @param identifier The identifier of the Grant to act upon.
   * @param configId The identifier of the Configuration to delete.
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(
    identifier: String,
    configId: String,
    overrides: RequestOverrides? = null,
  ): DeleteResponse {
    val path = String.format("v3/grants/%s/scheduling/configurations/%s", identifier, configId)
    return destroyResource(path, overrides = overrides)
  }
}
