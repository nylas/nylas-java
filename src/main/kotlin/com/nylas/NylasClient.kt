package com.nylas

import com.nylas.models.NylasApiError
import com.nylas.models.NylasApiErrorResponse
import com.nylas.resources.Calendars
import com.nylas.resources.Events
import com.nylas.util.JsonHelper
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * The NylasClient is the entry point to the Java SDK's API.
 * An instance holds a configured http client pointing to a base URL and is intended to be reused and shared
 * across threads and time.
 */
class NylasClient private constructor(val apiKey: String, httpClientBuilder: OkHttpClient.Builder, baseUrl: String) {
    private val baseUrl: HttpUrl
    private val httpClient: OkHttpClient

    enum class HttpMethod {
        GET,
        PUT,
        POST,
        DELETE,
        PATCH
    }

    enum class MediaType(val mediaType: String) {
        APPLICATION_JSON("application/json"),
        MESSAGE_RFC822("message/rfc822")

    }

    internal enum class HttpHeaders(val headerName: String) {
        ACCEPT("Accept"),
        AUTHORIZATION("Authorization"),
        CONTENT_TYPE("Content-Type")

    }

    /**
     * Internal constructor for builder.
     *
     * Only adds required version / build info interceptor
     */
    init {
        this.baseUrl = HttpUrl.get(baseUrl)
        httpClient = httpClientBuilder
//            .addInterceptor(AddVersionHeadersInterceptor()) // enforce user agent and build data
//            .addInterceptor(ContentHeadersInterceptor()) // enforce Content-Type headers.
            .build()
    }

    fun calendars(): Calendars {
        return Calendars(this)
    }

    fun events(): Events {
        return Events(this)
    }

    fun newUrlBuilder(): HttpUrl.Builder {
        return baseUrl.newBuilder()
    }

    @Throws(IOException::class, NylasApiError::class)
    fun <T> executeGet(url: HttpUrl.Builder, resultType: Type?): T {
        return executeRequest(url, HttpMethod.GET, null, resultType)
    }

    @Throws(IOException::class, NylasApiError::class)
    fun <T> executePut(
        url: HttpUrl.Builder,
        requestBody: String?,
        resultType: Type?,
    ): T {
        val jsonBody = if(requestBody != null) JsonHelper.jsonRequestBody(requestBody) else null
        return executeRequest(url, HttpMethod.PUT, jsonBody, resultType)
    }

    @Throws(IOException::class, NylasApiError::class)
    fun <T> executePatch(
        url: HttpUrl.Builder,
        params: Map<String, Any>?,
        resultType: Type?,
    ): T {
        val jsonBody = if(params != null) JsonHelper.jsonRequestBody(params) else null
        return executeRequest(url, HttpMethod.PATCH, jsonBody, resultType)
    }

    @Throws(IOException::class, NylasApiError::class)
    fun <T> executePost(
        url: HttpUrl.Builder,
        requestBody: String?,
        resultType: Type?,
    ): T {
        var jsonBody = RequestBody.create(null, ByteArray(0))
        if (requestBody != null) {
            jsonBody = JsonHelper.jsonRequestBody(requestBody)
        }
        return executeRequest(url, HttpMethod.POST, jsonBody, resultType)
    }

    @Throws(IOException::class, NylasApiError::class)
    fun <T> executeDelete(url: HttpUrl.Builder, resultType: Type?): T {
        return executeRequest(url, HttpMethod.DELETE, null, resultType)
    }

    /**
     * Download the given url. If the request is successful, returns the raw response body, exposing useful headers
     * such as Content-Type and Content-Length.
     */
    @Throws(IOException::class, NylasApiError::class)
    fun download(url: HttpUrl.Builder): ResponseBody? {
        val request = buildRequest(url, HttpMethod.GET, null)
        val response = httpClient.newCall(request).execute()
        throwAndCloseOnFailedRequest(response)
        return response.body()
    }

    private fun buildRequest(
        url: HttpUrl.Builder,
        method: HttpMethod,
        body: RequestBody?,
    ): Request {
        val builder = Request.Builder().url(url.build())
        builder.addHeader(HttpHeaders.AUTHORIZATION.headerName, "Bearer $apiKey")
        return builder.method(method.toString(), body).build()
    }

    @Throws(IOException::class, NylasApiError::class)
    fun <T> executeRequest(url: HttpUrl.Builder, method: HttpMethod, body: RequestBody?,
                           resultType: Type?): T {
        val request = buildRequest(url, method, body)
        httpClient.newCall(request).execute().use { response ->
            throwAndCloseOnFailedRequest(response)
            val responseBody = response.body()
            if (resultType == null || responseBody == null) {
                throw Exception("Unexpected null response body")
            }

            val adapter = JsonHelper.moshi().adapter<T>(resultType)

            return adapter?.fromJson(responseBody.source()) ?: throw Exception("Failed to deserialize response body")
        }
    }

    @Throws(IOException::class, NylasApiError::class)
    private fun throwAndCloseOnFailedRequest(response: Response) {
        if (!response.isSuccessful) {
            val responseBody = response.body()!!.string()
            response.close()
            val parsedError = JsonHelper.moshi().adapter(NylasApiErrorResponse::class.java)
                .fromJson(responseBody)
            if (parsedError?.error != null) {
                throw parsedError.error
            } else {
                throw NylasApiError("unknown", "Unknown error received from the API: $responseBody")
            }
        }
    }

    /**
     * A NylasClient.Builder allows applications to customize the Nylas http access
     * by choosing a different base url or modifying http client options.
     */
    data class Builder(
        private val apiKey: String,
    ) {
        //TODO::is this good?
        private var baseUrl: String = DEFAULT_BASE_URL
        private var httpClient: OkHttpClient.Builder = defaultHttpClient()
        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        /**
         * Access the OkHttpClient.Builder to allow customization.
         * By default, the NylasClient configures it as follows:
         * .protocols(Arrays.asList(Protocol.HTTP_1_1))
         * .connectTimeout(60, TimeUnit.SECONDS)
         * .readTimeout(60, TimeUnit.SECONDS)
         * .writeTimeout(60,  TimeUnit.SECONDS)
         * .addNetworkInterceptor(new HttpLoggingInterceptor()
         */
        fun httpClient(httpClient: OkHttpClient.Builder) = apply { this.httpClient = httpClient }

        fun build() = NylasClient(apiKey, httpClient, baseUrl)
    }

    companion object {
        const val DEFAULT_BASE_URL = "https://api-staging.us.nylas.com/"
        const val EU_BASE_URL = "https://ireland.api.nylas.com/"
        private fun defaultHttpClient(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .protocols(listOf(Protocol.HTTP_1_1))
//                .addNetworkInterceptor(HttpLoggingInterceptor())
        }
    }
}
