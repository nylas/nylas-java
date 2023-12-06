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
   * @return The list of events
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun list(identifier: String, queryParams: ListEventQueryParams): ListResponse<Event> {
    val path = String.format("v3/grants/%s/events", identifier)
    return listResource(path, queryParams)
  }

  /**
   * Return an Event
   * @param identifier Grant ID or email account to query
   * @param eventId The id of the event to retrieve.
   * @param queryParams The query parameters to include in the request
   * @return The event
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(identifier: String, eventId: String, queryParams: FindEventQueryParams): Response<Event> {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    return findResource(path, queryParams)
  }

  /**
   * Create an Event
   * @param identifier Grant ID or email account in which to create the object
   * @param requestBody The values to create the event with
   * @param queryParams The query parameters to include in the request
   * @return The created event
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(identifier: String, requestBody: CreateEventRequest, queryParams: CreateEventQueryParams): Response<Event> {
    val path = String.format("v3/grants/%s/events", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateEventRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, queryParams)
  }

  /**
   * Update an Event
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the event to update.
   * @param requestBody The values to update the Event with
   * @param queryParams The query parameters to include in the request
   * @return The updated event
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(identifier: String, eventId: String, requestBody: UpdateEventRequest, queryParams: UpdateEventQueryParams): Response<Event> {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, queryParams)
  }

  /**
   * Delete an Event
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the event to delete.
   * @param queryParams The query parameters to include in the request
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, eventId: String, queryParams: DestroyEventQueryParams): DeleteResponse {
    val path = String.format("v3/grants/%s/events/%s", identifier, eventId)
    return destroyResource(path, queryParams)
  }

  /**
   * Send RSVP. Allows users to respond to events they have been added to as an attendee.
   * @param identifier The identifier of the grant to act upon
   * @param eventId The id of the Event to update.
   * @param requestBody The values to send the RSVP with
   * @param queryParams The query parameters to include in the request
   * @return The send-rsvp response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun sendRsvp(identifier: String, requestBody: SendRsvpRequest, queryParams: SendRsvpQueryParams): Response<Event> {
    val path = String.format("v3/grants/%s/events/%s/send-rsvp", identifier, eventId)
    val adapter = JsonHelper.moshi().adapter(SendRsvpRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, queryParams)
  }

}
