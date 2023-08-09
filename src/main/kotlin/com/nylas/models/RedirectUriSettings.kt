package com.nylas.models

import com.squareup.moshi.Json

/**
 * Configuration settings for a Redirect URI object
 */
data class RedirectUriSettings(
  /**
   * Related to JS platform
   */
  @Json(name = "origin")
  val origin: String?,
  /**
   * Related to iOS platform
   */
  @Json(name = "bundle_id")
  val bundleId: String?,
  /**
   * Related to iOS platform
   */
  @Json(name = "app_store_id")
  val appStoreId: String?,
  /**
   * Related to iOS platform
   */
  @Json(name = "team_id")
  val teamId: String?,
  /**
   * Related to Android platform
   */
  @Json(name = "package_name")
  val packageName: String?,
  /**
   * Related to Android platform
   */
  @Json(name = "sha1_certificate_fingerprint")
  val sha1CertificateFingerprint: String?,
)
