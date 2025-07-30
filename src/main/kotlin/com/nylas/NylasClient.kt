package com.nylas

import com.nylas.interceptors.AddVersionHeadersInterceptor
import com.nylas.interceptors.ContentHeadersInterceptor
import com.nylas.interceptors.HttpLoggingInterceptor
import com.nylas.models.*
import com.nylas.resources.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.JsonDataException
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type
import java.net.SocketException
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
open class NylasClient(
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
    this.apiUri = apiUri.toHttpUrl()
    httpClient = httpClientBuilder
      .addInterceptor(AddVersionHeadersInterceptor()) // enforce user agent and build data
      .addInterceptor(ContentHeadersInterceptor()) // enforce Content-Type headers.
      .build()
  }

  /**
   * Access the Applications API
   * @return The Applications API
   */
  open fun applications(): Applications = Applications(this)

  /**
   * Access the Attachments API
   * @return The Attachments API
   */
  open fun attachments(): Attachments = Attachments(this)

  /**
   * Access the Auth API
   * @return The Auth API
   */
  open fun auth(): Auth = Auth(this)

  /**
   * Access the Calendars API
   * @return The Calendars API
   */
  open fun calendars(): Calendars = Calendars(this)

  /**
   * Access the Connectors API
   * @return The Connectors API
   */
  open fun connectors(): Connectors = Connectors(this)

  /**
   * Access the Drafts API
   * @return The Drafts API
   */
  open fun drafts(): Drafts = Drafts(this)

  /**
   * Access the Events API
   * @return The Events API
   */
  open fun events(): Events = Events(this)

  /**
   * Access the Folders API
   * @return The Folders API
   */
  open fun folders(): Folders = Folders(this)

  /**
   * Access the Grants API
   * @return The Grants API
   */
  open fun grants(): Grants = Grants(this)

  /**
   * Access the Messages API
   * @return The Messages API
   */
  open fun messages(): Messages = Messages(this)

  /**
   * Access the Threads API
   * @return The Threads API
   */
  open fun threads(): Threads = Threads(this)

  /**
   * Access the Webhooks API
   * @return The Webhooks API
   */
  open fun webhooks(): Webhooks = Webhooks(this)

  /**
   * Access the Contacts API
   * @return The Contacts API
   */
  open fun contacts(): Contacts = Contacts(this)

  /**
   * Access the Scheduler API
   * @return The Scheduler API
   */
  open fun scheduler(): Scheduler = Scheduler(this)

  /**
   * Access the Notetakers API
   * @return The Notetakers API
   */
  open fun notetakers(): Notetakers = Notetakers(this)

  /**
   * Get a URL builder instance for the Nylas API.
   */
  open fun newUrlBuilder(): HttpUrl.Builder = apiUri.newBuilder()

  /**
   * Execute a GET request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param queryParams The query parameters.
   * @param overrides The request overrides.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  open fun <T> executeGet(
    path: String,
    resultType: Type,
    queryParams: IQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): T {
    val url = buildUrl(path, queryParams, overrides)
    return executeRequest(url, HttpMethod.GET, null, resultType, overrides)
  }

  /**
   * Execute a PUT request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param requestBody The request body.
   * @param queryParams The query parameters.
   * @param overrides The request overrides.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  open fun <T> executePut(
    path: String,
    resultType: Type,
    requestBody: String? = null,
    queryParams: IQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): T {
    val url = buildUrl(path, queryParams, overrides)
    val jsonBody = if (requestBody != null) JsonHelper.jsonRequestBody(requestBody) else null
    return executeRequest(url, HttpMethod.PUT, jsonBody, resultType, overrides)
  }

  /**
   * Execute a PATCH request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param requestBody The request body.
   * @param queryParams The query parameters.
   * @param overrides The request overrides.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  open fun <T> executePatch(
    path: String,
    resultType: Type,
    requestBody: String? = null,
    queryParams: IQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): T {
    val url = buildUrl(path, queryParams, overrides)
    val jsonBody = if (requestBody != null) JsonHelper.jsonRequestBody(requestBody) else null
    return executeRequest(url, HttpMethod.PATCH, jsonBody, resultType, overrides)
  }

  /**
   * Execute a POST request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param requestBody The request body.
   * @param queryParams The query parameters.
   * @param overrides The request overrides.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  open fun <T> executePost(
    path: String,
    resultType: Type,
    requestBody: String? = null,
    queryParams: IQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): T {
    val url = buildUrl(path, queryParams, overrides)
    var jsonBody = RequestBody.create(null, ByteArray(0))
    if (requestBody != null) {
      jsonBody = JsonHelper.jsonRequestBody(requestBody)
    }
    return executeRequest(url, HttpMethod.POST, jsonBody, resultType, overrides)
  }

  /**
   * Execute a DELETE request to the Nylas API.
   * @param path The path to request.
   * @param resultType The type of the response body.
   * @param queryParams The query parameters.
   * @param overrides The request overrides.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  open fun <T> executeDelete(
    path: String,
    resultType: Type,
    queryParams: IQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): T {
    val url = buildUrl(path, queryParams, overrides)
    return executeRequest(url, HttpMethod.DELETE, null, resultType, overrides)
  }

  /**
   * Execute a request with a form-body payload to the Nylas API.
   * @param path The path to request.
   * @param method The HTTP method to use.
   * @param requestBody The form-data request body.
   * @param resultType The type of the response body.
   * @param queryParams The query parameters.
   * @param overrides The request overrides.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  open fun <T> executeFormRequest(
    path: String,
    method: HttpMethod,
    requestBody: RequestBody,
    resultType: Type,
    queryParams: IQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): T {
    val url = buildUrl(path, queryParams, overrides)
    return executeRequest(url, method, requestBody, resultType, overrides)
  }

  private fun buildRequest(
    url: HttpUrl.Builder,
    method: HttpMethod,
    body: RequestBody?,
    overrides: RequestOverrides?,
  ): Request {
    val builder = Request.Builder().url(url.build())

    // Override the API key if it is provided in the override
    val apiKey = overrides?.apiKey ?: this.apiKey
    builder.addHeader(HttpHeaders.AUTHORIZATION.headerName, "Bearer $apiKey")

    // Add additional headers
    if (overrides?.headers != null) {
      for ((key, value) in overrides.headers) {
        builder.addHeader(key, value)
      }
    }

    return builder.method(method.toString(), body).build()
  }

  /**
   * Execute a request to the Nylas API.
   * @param url The url to request.
   * @param method The HTTP method to use.
   * @param body The request body.
   * @param resultType The type of the response body.
   * @param overrides The request overrides.
   * @suppress Not for public use.
   */
  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  open fun <T> executeRequest(
    url: HttpUrl.Builder,
    method: HttpMethod,
    body: RequestBody?,
    resultType: Type,
    overrides: RequestOverrides? = null,
  ): T {
    val responseBody = this.executeRequestRawResponse(url, method, body, overrides)
    val adapter = JsonHelper.moshi().adapter<T>(resultType)
    val serializedObject = adapter?.fromJson(responseBody.source()) ?: throw Exception("Failed to deserialize response body")
    responseBody.close()
    return serializedObject
  }

  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  open fun downloadResponse(
    path: String,
    queryParams: IQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): ResponseBody {
    val url = buildUrl(path, queryParams, overrides)
    return this.executeRequestRawResponse(url, HttpMethod.GET, null, overrides)
  }

  @Throws(AbstractNylasApiError::class, NylasSdkTimeoutError::class)
  private fun executeRequestRawResponse(
    url: HttpUrl.Builder,
    method: HttpMethod,
    body: RequestBody?,
    overrides: RequestOverrides?,
  ): ResponseBody {
    val request = buildRequest(url, method, body, overrides)
    val finalUrl = url.build()
    try {
      // Use the provided timeout if it is set in the overrides.
      val httpClient = overrides?.timeout?.let { timeout ->
        httpClient.newBuilder()
          .callTimeout(timeout, TimeUnit.SECONDS)
          .readTimeout(timeout, TimeUnit.SECONDS)
          .writeTimeout(timeout, TimeUnit.SECONDS)
          .build()
      } ?: httpClient

      val response = httpClient.newCall(request).execute()
      throwAndCloseOnFailedRequest(finalUrl, response)
      return response.body ?: throw Exception("Unexpected null response body")
    } catch (e: SocketTimeoutException) {
      throw NylasSdkTimeoutError(finalUrl.toString(), httpClient.callTimeoutMillis)
    } catch (e: SocketException) {
      throw NylasSdkRemoteClosedError(finalUrl.toString(), e.message ?: "Unknown error")
    } catch (e: AbstractNylasApiError) {
      throw e
    } catch (e: Exception) {
      throw NylasApiError(
        type = "unknown",
        message = "Unknown error occurred: ${e.message}",
        statusCode = 0,
      )
    }
  }

  @Throws(AbstractNylasApiError::class)
  private fun throwAndCloseOnFailedRequest(url: HttpUrl, response: Response) {
    if (response.isSuccessful) {
      return
    }

    val responseBody = response.body!!.string()
    val parsedError: AbstractNylasApiError?
    response.close()

    if (url.encodedPath.equals("/v3/connect/token") || url.encodedPath.equals("/v3/connect/revoke")) {
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
              statusCode = response.code,
              headers = response.headers.toMultimap(),
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
              statusCode = response.code,
              headers = response.headers.toMultimap(),
            )
          }
          else -> throw ex
        }
      }
    }

    if (parsedError != null) {
      parsedError.statusCode = response.code
      parsedError.headers = response.headers.toMultimap()
      throw parsedError
    }

    throw NylasApiError(
      type = "unknown",
      message = "Unknown error received from the API: $responseBody",
      statusCode = response.code,
      headers = response.headers.toMultimap(),
    )
  }

  private fun buildUrl(path: String, queryParams: IQueryParams?, overrides: RequestOverrides?): HttpUrl.Builder {
    // Sets the API URI if it is provided in the overrides.
    var url = if (overrides?.apiUri != null) {
      overrides.apiUri.toHttpUrl().newBuilder().addPathSegments(path)
    } else {
      newUrlBuilder().addPathSegments(path)
    }

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
    private fun defaultHttpClient(): OkHttpClient.Builder = OkHttpClient.Builder()
      .connectTimeout(90, TimeUnit.SECONDS)
      .readTimeout(90, TimeUnit.SECONDS)
      .writeTimeout(90, TimeUnit.SECONDS)
      .protocols(listOf(Protocol.HTTP_1_1))
      .addNetworkInterceptor(HttpLoggingInterceptor())
  }
}
