package com.nylas

import com.nylas.interceptors.AddVersionHeadersInterceptor
import com.nylas.interceptors.ContentHeadersInterceptor
import com.nylas.interceptors.HttpLoggingInterceptor
import com.nylas.models.*
import com.nylas.resources.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.JsonDataException
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
 * @param apiUri The URL to use for communicating with the Nylas API.
 */
class NylasClient(
  val apiKey: String,
  httpClientBuilder: OkHttpClient.Builder = defaultHttpClient(),
  apiUri: String = DEFAULT_BASE_URL,
) {
  private val apiUri: HttpUrl
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
    this.apiUri = HttpUrl.get(apiUri)
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
   * Access the Attachments API
   * @return The Attachments API
   */
  fun attachments(): Attachments {
    return Attachments(this)
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
   * Access the Connectors API
   * @return The Connectors API
   */
  fun connectors(): Connectors {
    return Connectors(this)
  }

  /**
   * Access the Drafts API
   * @return The Drafts API
   */
  fun drafts(): Drafts {
    return Drafts(this)
  }

  /**
   * Access the Events API
   * @return The Events API
   */
  fun events(): Events {
    return Events(this)
  }

  /**
   * Access the Folders API
   * @return The Folders API
   */
  fun folders(): Folders {
    return Folders(this)
  }

  /**
   * Access the Grants API
   * @return The Grants API
   */
  fun grants(): Grants {
    return Grants(this)
  }

  /**
   * Access the Messages API
   * @return The Messages API
   */
  fun messages(): Messages {
    return Messages(this)
  }

  /**
   * Access the Threads API
   * @return The Threads API
   */
  fun threads(): Threads {
    return Threads(this)
  }

  /**
   * Access the Webhooks API
   * @return The Webhooks API
   */
  fun webhooks(): Webhooks {
    return Webhooks(this)
  }

  /**
   * Access the Contacts API
   * @return The Contacts API
   */
  fun contacts(): Contacts {
    return Contacts(this)
  }

  /**
   * Get a URL builder instance for the Nylas API.
   */
  fun newUrlBuilder(): HttpUrl.Builder {
    return apiUri.newBuilder()
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
    resultType: Type,
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
    resultType: Type,
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
    resultType: Type,
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
    resultType: Type,
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
    resultType: Type,
    queryParams: IQueryParams? = null,
  ): T {
    val url = buildUrl(path, queryParams)
    return executeRequest(url, HttpMethod.DELETE, null, resultType)
  }

  /**
   * Execute a request with a form-body payload to the Nylas API.
   * @param path The path to request.
   * @param method The HTTP method to use.
   * @param requestBody The form-data request body.
   * @param resultType The type of the response body.
   * @param queryParams The query parameters.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  fun <T> executeFormRequest(
    path: String,
    method: HttpMethod,
    requestBody: RequestBody,
    resultType: Type,
    queryParams: IQueryParams? = null,
  ): T {
    val url = buildUrl(path, queryParams)
    return executeRequest(url, method, requestBody, resultType)
  }

  private fun buildRequest(
    url: HttpUrl.Builder,
    method: HttpMethod,
    body: RequestBody?,
    userHeaders: Map<String, String> = emptyMap(),
  ): Request {
    val builder = Request.Builder().url(url.build())
    builder.addHeader(HttpHeaders.AUTHORIZATION.headerName, "Bearer $apiKey")
    for ((key, value) in userHeaders) {
      builder.addHeader(key, value)
    }
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
    resultType: Type,
  ): T {
    val responseBody = this.executeRequestRawResponse(url, method, body)
    val adapter = JsonHelper.moshi().adapter<T>(resultType)
    val serializedObject = adapter?.fromJson(responseBody.source()) ?: throw Exception("Failed to deserialize response body")
    responseBody.close()
    return serializedObject
  }

  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  fun downloadResponse(
    path: String,
    queryParams: IQueryParams? = null,
  ): ResponseBody {
    val url = buildUrl(path, queryParams)
    return this.executeRequestRawResponse(url, HttpMethod.GET, null)
  }

  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  private fun executeRequestRawResponse(
    url: HttpUrl.Builder,
    method: HttpMethod,
    body: RequestBody?,
  ): ResponseBody {
    val request = buildRequest(url, method, body)
    val finalUrl = url.build()
    try {
      val response = httpClient.newCall(request).execute()
      throwAndCloseOnFailedRequest(finalUrl, response)
      return response.body() ?: throw Exception("Unexpected null response body")
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
      } catch (ex: Exception) {
        when (ex) {
          is IOException, is JsonDataException -> {
            throw NylasOAuthError(
              error = "unknown",
              errorDescription = "Unknown error received from the API: $responseBody",
              errorUri = "unknown",
              errorCode = "0",
              statusCode = response.code(),
            )
          }

          else -> throw ex
        }
      }
    } else {
      try {
        val errorResp = JsonHelper.moshi().adapter(NylasApiErrorResponse::class.java)
          .fromJson(responseBody)
        parsedError = errorResp?.error
        if (parsedError != null) {
          parsedError.requestId = errorResp?.requestId
        }
      } catch (ex: Exception) {
        when (ex) {
          is IOException, is JsonDataException -> {
            throw NylasApiError(
              type = "unknown",
              message = "Unknown error received from the API: $responseBody",
              statusCode = response.code(),
            )
          }
          else -> throw ex
        }
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
      when (value) {
        is List<*> -> {
          for (item in value) {
            url.addQueryParameter(key, item.toString())
          }
        }
        is Map<*, *> -> {
          for ((k, v) in value) {
            url.addQueryParameter(key, "$k:$v")
          }
        }
        is Double -> {
          url.addQueryParameter(key, value.toInt().toString())
        }
        else -> {
          url.addQueryParameter(key, value.toString())
        }
      }
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
    private var apiUri: String = DEFAULT_BASE_URL
    private var httpClient: OkHttpClient.Builder = defaultHttpClient()

    /**
     * Set the base url for the NylasClient.
     * @param apiUri The URL to use for communicating with the Nylas API.
     */
    fun apiUri(apiUri: String) = apply { this.apiUri = apiUri }

    /**
     * Set the OkHttpClient.Builder for the NylasClient.
     *
     * By default, the NylasClient configures it as follows:
     * .protocols(Arrays.asList(Protocol.HTTP_1_1))
     * .connectTimeout(90, TimeUnit.SECONDS)
     * .readTimeout(90, TimeUnit.SECONDS)
     * .writeTimeout(90,  TimeUnit.SECONDS)
     * .addNetworkInterceptor(new HttpLoggingInterceptor()
     *
     * @param httpClient The custom OkHttpClient.Builder to use.
     */
    fun httpClient(httpClient: OkHttpClient.Builder) = apply { this.httpClient = httpClient }

    /**
     * Build the [NylasClient] instance
     * @return a [NylasClient] instance
     */
    fun build() = NylasClient(apiKey, httpClient, apiUri)
  }

  /**
   * @suppress Not for public use.
   */
  companion object {
    val DEFAULT_BASE_URL = Region.US.nylasApiUrl
    private fun defaultHttpClient(): OkHttpClient.Builder {
      return OkHttpClient.Builder()
        .connectTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .protocols(listOf(Protocol.HTTP_1_1))
        .addNetworkInterceptor(HttpLoggingInterceptor())
    }
  }
}
