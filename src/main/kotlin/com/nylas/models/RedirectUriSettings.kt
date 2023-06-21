package com.nylas.models

import com.squareup.moshi.Json

data class RedirectUriSettings(
  @Json(name = "origin")
  val origin: String?,
  @Json(name = "bundle_id")
  val bundleId: String?,
  @Json(name = "package_name")
  val packageName: String?,
  @Json(name = "sha1_certificate_fingerprint")
  val sha1CertificateFingerprint: String?,
)