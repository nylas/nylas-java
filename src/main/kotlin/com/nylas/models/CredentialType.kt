package com.nylas.models

import com.squareup.moshi.Json

enum class CredentialType(val value: String) {
  @Json(name = "adminconsent")
  ADMINCONSENT("adminconsent"),

  @Json(name = "serviceaccount")
  SERVICEACCOUNT("serviceaccount"),

  @Json(name = "connector")
  CONNECTOR("connector"),
}
