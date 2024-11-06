package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Json

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
    queryParams: ListConfigurationsParams? = null,
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

/**
 * Class representation of a request to update a configuration.
 */
data class UpdateConfigurationRequest(
  /**
   * List of participants included in the scheduled event.
   */
  @Json(name = "participants")
  val participants: List<ConfigurationParticipant>? = null,
  /**
   * Rules that determine available time slots for the event.
   */
  @Json(name = "availability")
  val availability: ConfigurationAvailability? = null,
  /**
   * Booking data for the event.
   */
  @Json(name = "event_booking")
  val eventBooking: ConfigurationEventBooking? = null,
  /**
   * Unique identifier for the Configuration object.
   */
  @Json(name = "slug")
  val slug: String? = null,
  /**
   * If true, scheduling Availability and Bookings endpoints require a valid session ID.
   */
  @Json(name = "requires_session_auth")
  val requiresSessionAuth: Boolean? = null,
  /**
   * Settings for the Scheduler UI.
   */
  @Json(name = "scheduler")
  val scheduler: ConfigurationSchedulerSettings? = null,
  /**
   * Appearance settings for the Scheduler UI.
   */
  @Json(name = "appearance")
  val appearance: Map<String, String>? = null,
)
