package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing Google connector setting.
 */
data class GoogleConnectorSettings(
  /**
   * The Google Pub/Sub topic name
   */
  @Json(name = "topic_name")
  val topicName: String? = null,
)
