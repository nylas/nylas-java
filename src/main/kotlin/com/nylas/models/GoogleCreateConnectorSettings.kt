package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Google connector creation request.
 */
data class GoogleCreateConnectorSettings(
  /**
   * The Google Client ID
   */
  @Json(name = "client_id")
  val clientId: String,
  /**
   * The Google Client Secret
   */
  @Json(name = "client_secret")
  val clientSecret: String,
  /**
   * The Google Pub/Sub topic name
   */
  @Json(name = "topic_name")
  val topicName: String? = null,
) {
  /**
   * Builder for [GoogleCreateConnectorSettings].
   * @property clientId The Google Client ID
   * @property clientSecret The Google Client Secret
   */
  data class Builder(
    private val clientId: String,
    private var clientSecret: String,
  ) {
    private var topicName: String? = null

    /**
     * Set the Google Pub/Sub topic name
     * @param topicName The Google Pub/Sub topic name
     * @return The builder
     */
    fun topicName(topicName: String) = apply { this.topicName = topicName }

    /**
     * Build the [GoogleCreateConnectorSettings] object
     * @return The [GoogleCreateConnectorSettings] object
     */
    fun build() = GoogleCreateConnectorSettings(
      clientId = clientId,
      clientSecret = clientSecret,
      topicName = topicName,
    )
  }
}
