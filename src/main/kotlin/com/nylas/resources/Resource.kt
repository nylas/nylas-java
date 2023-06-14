package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.DeleteResponse
import com.nylas.models.ListResponse
import com.nylas.models.RequestFailedException
import com.nylas.models.Response
import com.squareup.moshi.Types
import okhttp3.HttpUrl
import java.io.IOException
import java.lang.reflect.Type;

abstract class Resource<T> protected constructor(
    protected val client: NylasClient,
    modelClass: Class<T>
) {
    private val responseType: Type
    private val listResponseType: Type
    init {
        responseType = Types.newParameterizedType(Response::class.java, modelClass)
        listResponseType = Types.newParameterizedType(ListResponse::class.java, modelClass)
    }

    protected fun listResource(path: String, queryParams: Map<String, String>?): ListResponse<T> {
    @Throws(IOException::class, NylasApiError::class)
        var url = client.newUrlBuilder().addPathSegments(path)
        url = addQueryParams(url, queryParams)
        return client.executeGet(url, listResponseType)
    }

    protected fun findResource(path: String, queryParams: Map<String, String>?): Response<T> {
    @Throws(IOException::class, NylasApiError::class)
        var url = client.newUrlBuilder().addPathSegments(path)
        url = addQueryParams(url, queryParams)
        return client.executeGet(url, responseType)
    }

    protected fun createResource(path: String, requestBody: String?, queryParams: Map<String, String>?): Response<T> {
    @Throws(IOException::class, NylasApiError::class)
        var url = client.newUrlBuilder().addPathSegments(path)
        url = addQueryParams(url, queryParams)
        return client.executePost(url, requestBody, responseType)
    }

    protected fun updateResource(path: String, requestBody: String?, queryParams: Map<String, String>?): Response<T> {
    @Throws(IOException::class, NylasApiError::class)
        var url = client.newUrlBuilder().addPathSegments(path)
        url = addQueryParams(url, queryParams)
        return client.executePut(url, requestBody, responseType)
    }

    protected fun destroyResource(path: String, queryParams: Map<String, String>?): DeleteResponse {
    @Throws(IOException::class, NylasApiError::class)
        var url = client.newUrlBuilder().addPathSegments(path)
        url = addQueryParams(url, queryParams)
        return client.executeDelete(url, DeleteResponse::class.java)
    }

    private fun addQueryParams(url: HttpUrl.Builder, params: Map<String, String>?): HttpUrl.Builder {
        if (params != null) {
            for ((key, value) in params) {
                url.addQueryParameter(key, value)
            }
        }
        return url
    } //
}
