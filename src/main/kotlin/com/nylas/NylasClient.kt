package com.nylas

import com.nylas.interceptors.AddVersionHeadersInterceptor
import com.nylas.interceptors.ContentHeadersInterceptor
import com.nylas.interceptors.HttpLoggingInterceptor
import com.nylas.models.*
import com.nylas.resources.*
import com.nylas.util.JsonHelper
import okhttp3.*
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * The NylasClient is the entry point to the Java SDK.
 *
 * An instance holds a configured http client pointing to a base URL and is intended to be reused and shared
 * across threads and time.
 *
 * @param apiKey The Nylas API key to use for authentication.
 * @param httpClientBuilder The builder to use for creating the http client.
 * @param baseUrl The URL to use for communicating with the Nylas API.
 */
class NylasClient(
  val apiKey: String,
  httpClientBuilder: OkHttpClient.Builder = defaultHttpClient(),
  baseUrl: String = DEFAULT_BASE_URL,
) {
  private val baseUrl: HttpUrl
  private val httpClient: OkHttpClient

  /**
   * Different HTTP methods supported by the Nylas API.
   * @suppress Not for public use.
   */
  enum class HttpMethod {
    GET,
    PUT,
    POST,
    DELETE,
    PATCH,
  }

  /**
   * Different media types that can be set to the "Content-Type" header.
   * @suppress Not for public use.
   */
  enum class MediaType(val mediaType: String) {
    APPLICATION_JSON("application/json"),
    MESSAGE_RFC822("message/rfc822"),
  }

  /**
   * Different HTTP headers that can be set for an API request.
   * @suppress Not for public use.
   */
  internal enum class HttpHeaders(val headerName: String) {
    ACCEPT("Accept"),
    AUTHORIZATION("Authorization"),
    CONTENT_TYPE("Content-Type"),
  }

  init {
    this.baseUrl = HttpUrl.get(baseUrl)
    httpClient = httpClientBuilder
      .addInterceptor(AddVersionHeadersInterceptor()) // enforce user agent and build data
      .addInterceptor(ContentHeadersInterceptor()) // enforce Content-Type headers.
      .build()
  }

  /**
   * Access the Applications API
   * @return The Applications API
   */
  fun applications(): Applications {
    return Applications(this)
  }

  /**
   * Access the Auth API
   * @return The Auth API
   */
  fun auth(): Auth {
    return Auth(this)
  }

  /**
   * Access the Calendars API
   * @return The Calendars API
   */
  fun calendars(): Calendars {
    return Calendars(this)
  }

  /**
   * Access the Events API
   * @return The Events API
   */
  fun events(): Events {
    return Events(this)
  }

  /**
   * Access the Webhooks API
   * @return The Webhooks API
   */
  fun webhooks(): Webhooks {
    return Webhooks(this)
  }

  /**
   * Get a URL builder instance for the Nylas API.
   */
  fun newUrlBuilder(): HttpUrl.Builder {
    return baseUrl.newBuilder()
  }

  /**
   * Execute a GET request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param queryParams The query parameters.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  fun <T> executeGet(
    path: String,
    resultType: Type? = null,
    queryParams: IQueryParams? = null,
  ): T {
    val url = buildUrl(path, queryParams)
    return executeRequest(url, HttpMethod.GET, null, resultType)
  }

  /**
   * Execute a PUT request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param requestBody The request body.
   * @param queryParams The query parameters.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  fun <T> executePut(
    path: String,
    resultType: Type? = null,
    requestBody: String? = null,
    queryParams: IQueryParams? = null,
  ): T {
    val url = buildUrl(path, queryParams)
    val jsonBody = if (requestBody != null) JsonHelper.jsonRequestBody(requestBody) else null
    return executeRequest(url, HttpMethod.PUT, jsonBody, resultType)
  }

  /**
   * Execute a PATCH request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param requestBody The request body.
   * @param queryParams The query parameters.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  fun <T> executePatch(
    path: String,
    resultType: Type? = null,
    requestBody: String? = null,
    queryParams: IQueryParams? = null,
  ): T {
    val url = buildUrl(path, queryParams)
    val jsonBody = if (requestBody != null) JsonHelper.jsonRequestBody(requestBody) else null
    return executeRequest(url, HttpMethod.PATCH, jsonBody, resultType)
  }

  /**
   * Execute a POST request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param requestBody The request body.
   * @param queryParams The query parameters.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  fun <T> executePost(
    path: String,
    resultType: Type? = null,
    requestBody: String? = null,
    queryParams: IQueryParams? = null,
  ): T {
    val url = buildUrl(path, queryParams)
    var jsonBody = RequestBody.create(null, ByteArray(0))
    if (requestBody != null) {
      jsonBody = JsonHelper.jsonRequestBody(requestBody)
    }
    return executeRequest(url, HttpMethod.POST, jsonBody, resultType)
  }

  /**
   * Execute a DELETE request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param queryParams The query parameters.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  fun <T> executeDelete(
    path: String,
    resultType: Type? = null,
    queryParams: IQueryParams? = null,
  ): T {
    val url = buildUrl(path, queryParams)
    return executeRequest(url, HttpMethod.DELETE, null, resultType)
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

  /**
   * Execute a request to the Nylas API.
   * @param url The url to request.
   * @param method The HTTP method to use.
   * @param body The request body.
   * @param resultType The type of the response body.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  fun <T> executeRequest(
    url: HttpUrl.Builder,
    method: HttpMethod,
    body: RequestBody?,
    resultType: Type?,
  ): T {
    val request = buildRequest(url, method, body)
    val finalUrl = url.build()
    try {
      httpClient.newCall(request).execute().use { response ->
        throwAndCloseOnFailedRequest(finalUrl, response)
        val responseBody = response.body()
        if (resultType == null || responseBody == null) {
          throw Exception("Unexpected null response body")
        }

        val adapter = JsonHelper.moshi().adapter<T>(resultType)

        return adapter?.fromJson(responseBody.source()) ?: throw Exception("Failed to deserialize response body")
      }
    } catch (e: SocketTimeoutException) {
      throw NylasSdkTimeoutError(finalUrl.toString(), httpClient.callTimeoutMillis())
    }
  }

  @Throws(AbstractNylasApiError::class)
  private fun throwAndCloseOnFailedRequest(url: HttpUrl, response: Response) {
    if (response.isSuccessful) {
      return
    }

    val responseBody = response.body()!!.string()
    val parsedError: AbstractNylasApiError?
    response.close()

    if (url.encodedPath().equals("/v3/connect/token") || url.encodedPath().equals("/v3/connect/revoke")) {
      try {
        parsedError = JsonHelper.moshi().adapter(NylasOAuthError::class.java)
          .fromJson(responseBody)
      } catch (e: IOException) {
        throw NylasOAuthError(
          error = "unknown",
          errorDescription = "Unknown error received from the API: $responseBody",
          errorUri = "unknown",
          errorCode = "0",
          statusCode = response.code(),
        )
      }
    } else {
      try {
        val errorResp = JsonHelper.moshi().adapter(NylasApiErrorResponse::class.java)
          .fromJson(responseBody)
        parsedError = errorResp?.error
        if (parsedError != null) {
          parsedError.requestId = errorResp?.requestId
        }
      } catch (e: IOException) {
        throw NylasApiError(
          type = "unknown",
          message = "Unknown error received from the API: $responseBody",
          statusCode = response.code(),
        )
      }
    }

    if (parsedError != null) {
      parsedError.statusCode = response.code()
      throw parsedError
    }

    throw NylasApiError(
      type = "unknown",
      message = "Unknown error received from the API: $responseBody",
      statusCode = response.code(),
    )
  }

  private fun buildUrl(path: String, queryParams: IQueryParams?): HttpUrl.Builder {
    var url = newUrlBuilder().addPathSegments(path)
    if (queryParams != null) {
      url = addQueryParams(url, queryParams.convertToMap())
    }
    return url
  }

  private fun addQueryParams(url: HttpUrl.Builder, params: Map<String, Any>): HttpUrl.Builder {
    for ((key, value) in params) {
      url.addQueryParameter(key, value.toString())
    }
    return url
  }

  /**
   * A builder for creating [NylasClient]. Allows applications to customize the Nylas
   * http access by choosing a different base url or modifying http client options.
   *
   * @param apiKey The Nylas API key to use for authentication.
   */
  data class Builder(
    private val apiKey: String,
  ) {
    private var baseUrl: String = DEFAULT_BASE_URL
    private var httpClient: OkHttpClient.Builder = defaultHttpClient()

    /**
     * Set the base url for the NylasClient.
     * @param baseUrl The URL to use for communicating with the Nylas API.
     */
    fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

    /**
     * Set the OkHttpClient.Builder for the NylasClient.
     *
     * By default, the NylasClient configures it as follows:
     * .protocols(Arrays.asList(Protocol.HTTP_1_1))
     * .connectTimeout(60, TimeUnit.SECONDS)
     * .readTimeout(60, TimeUnit.SECONDS)
     * .writeTimeout(60,  TimeUnit.SECONDS)
     * .addNetworkInterceptor(new HttpLoggingInterceptor()
     *
     * @param httpClient The custom OkHttpClient.Builder to use.
     */
    fun httpClient(httpClient: OkHttpClient.Builder) = apply { this.httpClient = httpClient }

    /**
     * Build the [NylasClient] instance
     * @return a [NylasClient] instance
     */
    fun build() = NylasClient(apiKey, httpClient, baseUrl)
  }

  /**
   * @suppress Not for public use.
   */
  companion object {
    val DEFAULT_BASE_URL = Region.US.nylasApiUrl
    private fun defaultHttpClient(): OkHttpClient.Builder {
      return OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .protocols(listOf(Protocol.HTTP_1_1))
        .addNetworkInterceptor(HttpLoggingInterceptor())
    }
  }
}
