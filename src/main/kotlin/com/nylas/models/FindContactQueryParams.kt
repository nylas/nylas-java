package com.nylas.models

import com.squareup.moshi.Json

data class FindContactQueryParams(
  @Json(name = "profile_picture")
  val profilePicture: Boolean? = null,
)
