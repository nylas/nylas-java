package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum class for the different authentication providers.
 */
enum class AuthProvider(val value: String) {
  @Json(name = "google")
  GOOGLE("google"),

  @Json(name = "yahoo")
  YAHOO("yahoo"),

  @Json(name = "microsoft")
  MICROSOFT("microsoft"),

  @Json(name = "imap")
  IMAP("imap"),
}
