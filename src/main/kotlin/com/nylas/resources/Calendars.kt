package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import java.io.IOException

/**
 * [https://docs.nylas.com/reference#calendars](https://docs.nylas.com/reference#calendars)
 */
class Calendars(client: NylasClient) : Resource<Calendar>(client, Calendar::class.java) {
    @Throws(IOException::class, RequestFailedException::class)
    @JvmOverloads
    fun list(identifier: String, queryParams: Map<String, String>? = null): ListResponse<Calendar> {
        val path = String.format("/v3/grants/%s/calendars", identifier)
        return listResource(path, queryParams)
    }

    @Throws(IOException::class, RequestFailedException::class)
    @JvmOverloads
    fun find(identifier: String, calendarId: String, queryParams: Map<String, String>? = null): Response<Calendar> {
        val path = String.format("/v3/grants/%s/calendars/%s", identifier, calendarId)
        return findResource(path, queryParams)
    }

    @Throws(IOException::class, RequestFailedException::class)
    @JvmOverloads
    fun create(identifier: String, requestBody: CreateCalendarRequest, queryParams: Map<String, String>? = null): Response<Calendar> {
        val path = String.format("/v3/grants/%s/calendars", identifier)
        val adapter = JsonHelper.moshi().adapter(CreateCalendarRequest::class.java)
        val serializedRequestBody = adapter.toJson(requestBody)
        return createResource(path, serializedRequestBody, queryParams)
    }

    @Throws(IOException::class, RequestFailedException::class)
    @JvmOverloads
    fun update(identifier: String, calendarId: String, requestBody: UpdateCalendarRequest, queryParams: Map<String, String>? = null): Response<Calendar> {
        val path = String.format("/v3/grants/%s/calendars/%s", identifier, calendarId)
        val adapter = JsonHelper.moshi().adapter(UpdateCalendarRequest::class.java)
        val serializedRequestBody = adapter.toJson(requestBody)
        return updateResource(path, serializedRequestBody, queryParams)
    }

    @Throws(IOException::class, RequestFailedException::class)
    @JvmOverloads
    fun destroy(identifier: String, calendarId: String, queryParams: Map<String, String>? = null): DeleteResponse {
        val path = String.format("/v3/grants/%s/calendars/%s", identifier, calendarId)
        return destroyResource(path, queryParams)
    }
}
