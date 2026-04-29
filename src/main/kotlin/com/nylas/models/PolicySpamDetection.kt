package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas policy spam detection configuration.
 */
data class PolicySpamDetection(
  /**
   * If true, enables DNS-based blocklist (DNSBL) checks.
   */
  @Json(name = "use_list_dnsbl")
  val useListDnsbl: Boolean? = null,
  /**
   * If true, enables header anomaly detection.
   */
  @Json(name = "use_header_anomaly_detection")
  val useHeaderAnomalyDetection: Boolean? = null,
  /**
   * Sensitivity threshold for spam scoring (higher = more aggressive).
   */
  @Json(name = "spam_sensitivity")
  val spamSensitivity: Double? = null,
)
