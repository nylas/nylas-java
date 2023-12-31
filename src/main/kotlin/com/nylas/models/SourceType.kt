package com.nylas.models

import com.squareup.moshi.Json

enum class SourceType {
  @Json(name = "address_book")
  ADDRESS_BOOK,

  @Json(name = "inbox")
  INBOX,

  @Json(name = "domain")
  DOMAIN,
}
