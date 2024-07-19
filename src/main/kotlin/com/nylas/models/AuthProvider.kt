package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum class for the different authentication providers.
 */
enum class AuthProvider(val value: String) {
  @Json(name = "google")
  GOOGLE("google"),

  @Json(name = "microsoft")
  MICROSOFT("microsoft"),

  @Json(name = "imap")
  IMAP("imap"),

  @Json(name = "virtual-calendar")
  VIRTUAL_CALENDAR("virtual-calendar"),

  @Json(name = "icloud")
  ICLOUD("icloud"),

  @Json(name = "ews")
  EWS("ews"),
}
