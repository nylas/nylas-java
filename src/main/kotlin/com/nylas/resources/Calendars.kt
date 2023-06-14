package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import java.io.IOException

/**
 * [https://docs.nylas.com/reference#calendars](https://docs.nylas.com/reference#calendars)
 */
class Calendars(client: NylasClient) : Resource<Calendar>(client, Calendar::class.java) {
    @Throws(IOException::class, NylasApiError::class)
    @JvmOverloads
    fun list(identifier: String, queryParams: Map<String, String>? = null): ListResponse<Calendar> {
        val path = String.format("/v3/grants/%s/calendars", identifier)
        return listResource(path, queryParams)
    }

    @JvmOverloads
    fun find(identifier: String, calendarId: String, queryParams: Map<String, String>? = null): Response<Calendar> {
    @Throws(IOException::class, NylasApiError::class)
        val path = String.format("/v3/grants/%s/calendars/%s", identifier, calendarId)
        return findResource(path, queryParams)
    }

    @JvmOverloads
    fun create(identifier: String, requestBody: CreateCalendarRequest, queryParams: Map<String, String>? = null): Response<Calendar> {
    @Throws(IOException::class, NylasApiError::class)
        val path = String.format("/v3/grants/%s/calendars", identifier)
        val adapter = JsonHelper.moshi().adapter(CreateCalendarRequest::class.java)
        val serializedRequestBody = adapter.toJson(requestBody)
        return createResource(path, serializedRequestBody, queryParams)
    }

    @JvmOverloads
    fun update(identifier: String, calendarId: String, requestBody: UpdateCalendarRequest, queryParams: Map<String, String>? = null): Response<Calendar> {
    @Throws(IOException::class, NylasApiError::class)
        val path = String.format("/v3/grants/%s/calendars/%s", identifier, calendarId)
        val adapter = JsonHelper.moshi().adapter(UpdateCalendarRequest::class.java)
        val serializedRequestBody = adapter.toJson(requestBody)
        return updateResource(path, serializedRequestBody, queryParams)
    }

    @JvmOverloads
    fun destroy(identifier: String, calendarId: String, queryParams: Map<String, String>? = null): DeleteResponse {
    @Throws(IOException::class, NylasApiError::class)
        val path = String.format("/v3/grants/%s/calendars/%s", identifier, calendarId)
        return destroyResource(path, queryParams)
    }
}
