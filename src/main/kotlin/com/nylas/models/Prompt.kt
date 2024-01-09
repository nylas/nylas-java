package com.nylas.models

import com.squareup.moshi.Json

enum class Prompt {
  @Json(name = "select_provider")
  SELECT_PROVIDER,
  @Json(name = "detect")
  DETECT,
  @Json(name = "select_provider,detect")
  SELECT_PROVIDER_DETECT,
  @Json(name = "detect,select_provider")
  DETECT_SELECT_PROVIDER,
}