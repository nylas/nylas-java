package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

/**
 * Nylas Events API
 *
 * The Nylas Events API allows you to create, update, and delete events on user calendars.
 *
 * @param client The configured Nylas API client
 */
class Events(client: NylasClient) : Resource<Event>(client, Event::class.java) {
  /**
   * Return all Events
   * @param identifier Grant ID or email account to query
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The list of events
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListEventQueryParams, overrides: RequestOverrides? = null): ListResponse<Event> {
    val path = String.format("v3/grants/%s/events", identifier)
    return listResource(path, queryParams, overrides)
  }

  /**
   * Returns a list of recurring events, recurring event exceptions,
   * and single events from the specified calendar within a given time frame.
   * This is useful when you want to import, store, and synchronize events
   * from the time frame to your application
   * @param identifier Grant ID or email account to query
   * @param queryParams The query parameters to include in the request (must include calendar_id)
   * @param overrides Optional request overrides to apply
   * @return The list of import events
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun listImportEvents(identifier: String, queryParams: ListImportEventQueryParams, overrides: RequestOverrides? = null): ListResponse<Event> {
    val path = String.format("v3/grants/%s/events/import", identifier)
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return an Event
   * @param identifier Grant ID or email account to query
   * @param eventId The id of the event to retrieve.
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The event
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(identifier: String, eventId: String, queryParams: FindEventQueryParams, overrides: RequestOverrides? = null): Response<Event> {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    return findResource(path, queryParams, overrides)
  }

  /**
   * Create an Event
   * @param identifier Grant ID or email account in which to create the object
   * @param requestBody The values to create the event with
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The created event
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(identifier: String, requestBody: CreateEventRequest, queryParams: CreateEventQueryParams, overrides: RequestOverrides? = null): Response<Event> {
    val path = String.format("v3/grants/%s/events", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateEventRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, queryParams, overrides)
  }

  /**
   * Update an Event
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the event to update.
   * @param requestBody The values to update the Event with
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The updated event
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(identifier: String, eventId: String, requestBody: UpdateEventRequest, queryParams: UpdateEventQueryParams, overrides: RequestOverrides? = null): Response<Event> {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, queryParams, overrides)
  }

  /**
   * Delete an Event
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the event to delete.
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(identifier: String, eventId: String, queryParams: DestroyEventQueryParams, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    return destroyResource(path, queryParams, overrides)
  }

  /**
   * Send RSVP. Allows users to respond to events they have been added to as an attendee.
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the Event to update.
   * @param requestBody The values to send the RSVP with
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The send-rsvp response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun sendRsvp(identifier: String, eventId: String, requestBody: SendRsvpRequest, queryParams: SendRsvpQueryParams, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/grants/%s/events/%s/send-rsvp", identifier, eventId)
    val adapter = JsonHelper.moshi().adapter(SendRsvpRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)

    return client.executePost(path, DeleteResponse::class.java, serializedRequestBody, queryParams, overrides)
  }
}
