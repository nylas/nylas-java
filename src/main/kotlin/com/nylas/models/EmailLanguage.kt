package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for email language options.
 */
enum class EmailLanguage {
  @Json(name = "en")
  EN,

  @Json(name = "es")
  ES,

  @Json(name = "fr")
  FR,

  @Json(name = "de")
  DE,

  @Json(name = "nl")
  NL,

  @Json(name = "sv")
  SV,

  @Json(name = "ja")
  JA,

  @Json(name = "zh")
  ZH,
}
