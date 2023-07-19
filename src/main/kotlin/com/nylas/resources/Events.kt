package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import java.io.IOException

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
   * @param identifier The identifier of the grant to act upon
   * @param queryParams The query parameters to include in the request
   * @return The list of Events
   */
  @Throws(IOException::class, NylasApiError::class)
  fun list(identifier: String, queryParams: ListEventQueryParams): ListResponse<Event> {
    val path = String.format("v3/grants/%s/events", identifier)
    return listResource(path, queryParams)
  }

  /**
   * Return an Event
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the Event to retrieve.
   * @param queryParams The query parameters to include in the request
   * @return The Event
   */
  @Throws(IOException::class, NylasApiError::class)
  fun find(identifier: String, eventId: String, queryParams: FindEventQueryParams): Response<Event> {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    return findResource(path, queryParams)
  }

  /**
   * Create an Event
   * @param identifier The identifier of the grant to act upon
   * @param requestBody The values to create the Event with
   * @param queryParams The query parameters to include in the request
   * @return The created Event
   */
  @Throws(IOException::class, NylasApiError::class)
  fun create(identifier: String, requestBody: CreateEventRequest, queryParams: CreateEventQueryParams): Response<Event> {
    val path = String.format("v3/grants/%s/events", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateEventRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, queryParams)
  }

  /**
   * Update an Event
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the Event to update.
   * @param requestBody The values to update the Event with
   * @param queryParams The query parameters to include in the request
   * @return The updated Event
   */
  @Throws(IOException::class, NylasApiError::class)
  fun update(identifier: String, eventId: String, requestBody: UpdateEventRequest, queryParams: UpdateEventQueryParams): Response<Event> {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, queryParams)
  }

  /**
   * Delete an Event
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the Event to delete.
   * @param queryParams The query parameters to include in the request
   * @return The deletion response
   */
  @Throws(IOException::class, NylasApiError::class)
  fun destroy(identifier: String, eventId: String, queryParams: DestroyEventQueryParams): DeleteResponse {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    return destroyResource(path, queryParams)
  }
}
