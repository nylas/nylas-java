package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
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

    @Throws(IOException::class, NylasApiError::class)
    protected fun listResource(path: String, queryParams: IQueryParams? = null): ListResponse<T> {
        var url = client.newUrlBuilder().addPathSegments(path)
        if (queryParams != null) {
            url = addQueryParams(url, queryParams.convertToMap())
        }

        return client.executeGet(url, listResponseType)
    }

    @Throws(IOException::class, NylasApiError::class)
    protected fun findResource(path: String, queryParams: IQueryParams? = null): Response<T> {
        var url = client.newUrlBuilder().addPathSegments(path)
        if (queryParams != null) {
            url = addQueryParams(url, queryParams.convertToMap())
        }

        return client.executeGet(url, responseType)
    }

    @Throws(IOException::class, NylasApiError::class)
    protected fun createResource(path: String, requestBody: String?, queryParams: IQueryParams? = null): Response<T> {
        var url = client.newUrlBuilder().addPathSegments(path)
        if (queryParams != null) {
            url = addQueryParams(url, queryParams.convertToMap())
        }

        return client.executePost(url, requestBody, responseType)
    }

    @Throws(IOException::class, NylasApiError::class)
    protected fun updateResource(path: String, requestBody: String?, queryParams: IQueryParams? = null): Response<T> {
        var url = client.newUrlBuilder().addPathSegments(path)
        if (queryParams != null) {
            url = addQueryParams(url, queryParams.convertToMap())
        }

        return client.executePut(url, requestBody, responseType)
    }

    @Throws(IOException::class, NylasApiError::class)
    protected fun destroyResource(path: String, queryParams: IQueryParams? = null): DeleteResponse {
        var url = client.newUrlBuilder().addPathSegments(path)
        if (queryParams != null) {
            url = addQueryParams(url, queryParams.convertToMap())
        }

        return client.executeDelete(url, DeleteResponse::class.java)
    }

    private fun addQueryParams(url: HttpUrl.Builder, params: Map<String, Any>): HttpUrl.Builder {
        for ((key, value) in params) {
            url.addQueryParameter(key, value.toString())
        }
        return url
    }
}
