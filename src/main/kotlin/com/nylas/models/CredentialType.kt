package com.nylas.models

import com.squareup.moshi.Json

enum class CredentialType {
  @Json(name = "adminconsent")
  ADMINCONSENT,

  @Json(name = "serviceaccount")
  SERVICEACCOUNT,

  @Json(name = "connector")
  CONNECTOR,
}
