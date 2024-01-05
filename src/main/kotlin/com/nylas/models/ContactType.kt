package com.nylas.models

import com.squareup.moshi.Json

enum class ContactType {
  @Json(name = "work")
  WORK,

  @Json(name = "home")
  HOME,

  @Json(name = "other")
  OTHER,

  @Json(name = "mobile")
  MOBILE,
}
