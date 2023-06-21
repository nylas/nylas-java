package com.nylas.models

import com.squareup.moshi.Json

data class Provider(
  @Json(name = "name")
  val name: String,
  @Json(name = "provider")
  val provider: AuthProvider,
  @Json(name = "type")
  val type: String,
  @Json(name = "settings")
  val settings: Settings?,
) {
  data class Settings(
    @Json(name = "name")
    val name: String?,
    @Json(name = "imap_host")
    val imapHost: String?,
    @Json(name = "imap_port")
    val imapPort: Int?,
    @Json(name = "smtp_host")
    val smtpHost: String?,
    @Json(name = "smtp_port")
    val smtpPort: Int?,
    @Json(name = "password_link")
    val passwordLink: String?,
    @Json(name = "primary")
    val primary: Boolean?,
  )
}
