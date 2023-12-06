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
)
