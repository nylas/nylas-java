package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.models.GetFreeBusyResponse.Companion.GET_FREE_BUSY_RESPONSE_ADAPTER
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types

/**
 * Nylas Calendar API
 *
 * The Nylas calendar API allows you to create new calendars or manage existing ones.
 * A calendar can be accessed by one, or several people, and can contain events.
 *
 * @param client The configured Nylas API client
 */
class Calendars(client: NylasClient) : Resource<Calendar>(client, Calendar::class.java) {
  /**
   * Return all Calendars
   * @param identifier Grant ID or email account to query.
   * @param queryParams The query parameters to include in the request
   * @return The list of Calendars
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListCalendersQueryParams? = null): ListResponse<Calendar> {
    val path = String.format("v3/grants/%s/calendars", identifier)
    return listResource(path, queryParams)
  }

  /**
   * Return a Calendar
   * @param identifier Grant ID or email account to query.
   * @param calendarId The id of the Calendar to retrieve. Use "primary" to refer to the primary calendar associated with grant.
   * @return The calendar
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(identifier: String, calendarId: String): Response<Calendar> {
    val path = String.format("v3/grants/%s/calendars/%s", identifier, calendarId)
    return findResource(path)
  }

  /**
   * Create a Calendar
   * @param identifier Grant ID or email account in which to create the object.
   * @param requestBody The values to create the calendar with
   * @return The created calendar
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(identifier: String, requestBody: CreateCalendarRequest): Response<Calendar> {
    val path = String.format("v3/grants/%s/calendars", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateCalendarRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody)
  }

  /**
   * Update a Calendar
   * @param identifier Grant ID or email account in which to update an object.
   * @param calendarId The id of the calendar to update. Use "primary" to refer to the primary calendar associated with grant.
   * @param requestBody The values to update the calendar with
   * @return The updated calendar
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(identifier: String, calendarId: String, requestBody: UpdateCalendarRequest): Response<Calendar> {
    val path = String.format("v3/grants/%s/calendars/%s", identifier, calendarId)
    val adapter = JsonHelper.moshi().adapter(UpdateCalendarRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody)
  }

  /**
   * Delete a Calendar
   * @param identifier Grant ID or email account from which to delete an object.
   * @param calendarId The id of the Calendar to delete. Use "primary" to refer to the primary calendar associated with grant.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, calendarId: String): DeleteResponse {
    val path = String.format("v3/grants/%s/calendars/%s", identifier, calendarId)
    return destroyResource(path)
  }

  /**
   * Get Availability for a given account / accounts
   * @param request The availability request
   * @return The availability response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun getAvailability(request: GetAvailabilityRequest): Response<GetAvailabilityResponse> {
    val path = "v3/calendars/availability"

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(GetAvailabilityRequest::class.java)
      .toJson(request)

    val responseType = Types.newParameterizedType(Response::class.java, GetAvailabilityResponse::class.java)

    return client.executePost(path, responseType, serializedRequestBody)
  }

  /**
   * Get the free/busy schedule for a list of email addresses
   * @param identifier The identifier of the grant to act upon
   * @param request The free/busy request
   * @return The free/busy response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun getFreeBusy(identifier: String, request: GetFreeBusyRequest): Response<List<GetFreeBusyResponse>> {
    val path = String.format("v3/grants/%s/calendars/free-busy", identifier)

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(GetFreeBusyRequest::class.java)
      .toJson(request)

    val responseType = Types.newParameterizedType(Response::class.java, GET_FREE_BUSY_RESPONSE_ADAPTER)

    return client.executePost(path, responseType, serializedRequestBody)
  }
}
