package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import java.io.IOException

class Events(client: NylasClient) : Resource<Event>(client, Event::class.java) {
  @Throws(IOException::class, NylasApiError::class)
  fun list(identifier: String, queryParams: ListEventQueryParams): ListResponse<Event> {
    val path = String.format("/v3/grants/%s/events", identifier)
    return listResource(path, queryParams)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun find(identifier: String, eventId: String, queryParams: FindEventQueryParams): Response<Event> {
    val path = String.format("/v3/grants/%s/events/%s", identifier, eventId)
    return findResource(path, queryParams)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun create(identifier: String, requestBody: CreateEventRequest, queryParams: CreateEventQueryParams): Response<Event> {
    val path = String.format("/v3/grants/%s/events", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateEventRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, queryParams)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun update(identifier: String, eventId: String, requestBody: UpdateEventRequest, queryParams: UpdateEventQueryParams): Response<Event> {
    val path = String.format("/v3/grants/%s/events/%s", identifier, eventId)
    val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, queryParams)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun destroy(identifier: String, eventId: String, queryParams: DestroyEventQueryParams): DeleteResponse {
    val path = String.format("/v3/grants/%s/events/%s", identifier, eventId)
    return destroyResource(path, queryParams)
  }
}
