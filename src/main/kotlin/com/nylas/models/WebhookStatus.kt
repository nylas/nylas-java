package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the possible webhook statuses.
 */
enum class WebhookStatus {
  @Json(name = "active")
  ACTIVE,
  @Json(name = "failing")
  FAILING,
  @Json(name = "failed")
  FAILED,
  @Json(name = "paused")
  PAUSE,
}