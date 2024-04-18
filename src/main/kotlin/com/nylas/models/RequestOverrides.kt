package com.nylas.models

/**
 * Overrides to use for an outgoing request to the Nylas API.
 */
data class RequestOverrides(
  /**
   * The API key to use for the request.
   */
  val apiKey: String? = null,
  /**
   * The API URI to use for the request.
   */
  val apiUri: String? = null,
  /**
   * The timeout to use for the request.
   */
  val timeout: Long? = null,
  /**
   * Additional headers to include in the request.
   */
  val headers: Map<String, String>? = emptyMap(),
) {
  /**
   * Builder for [RequestOverrides].
   */
  class Builder {
    private var apiKey: String? = null
    private var apiUri: String? = null
    private var timeout: Long? = null
    private var headers: Map<String, String>? = null

    /**
     * Set the API key to use for the request.
     */
    fun apiKey(apiKey: String) = apply { this.apiKey = apiKey }

    /**
     * Set the API URI to use for the request.
     */
    fun apiUri(apiUri: String) = apply { this.apiUri = apiUri }

    /**
     * Set the timeout to use for the request.
     */
    fun timeout(timeout: Long) = apply { this.timeout = timeout }

    /**
     * Add additional headers to include in the request.
     */
    fun headers(headers: Map<String, String>) = apply { this.headers = headers }

    /**
     * Build the [RequestOverrides].
     */
    fun build() = RequestOverrides(apiKey, apiUri, timeout, headers)
  }
}
