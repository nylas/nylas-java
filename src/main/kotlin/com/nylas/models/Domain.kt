package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas managed email domain.
 */
data class Domain(
  @Json(name = "id")
  val id: String? = null,
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "domain_address")
  val domainAddress: String? = null,
  @Json(name = "organization_id")
  val organizationId: String? = null,
  @Json(name = "branded")
  val branded: Boolean? = null,
  @Json(name = "region")
  val region: String? = null,
  @Json(name = "verified_ownership")
  val verifiedOwnership: Boolean? = null,
  @Json(name = "verified_mx")
  val verifiedMx: Boolean? = null,
  @Json(name = "verified_spf")
  val verifiedSpf: Boolean? = null,
  @Json(name = "verified_feedback")
  val verifiedFeedback: Boolean? = null,
  @Json(name = "verified_dkim")
  val verifiedDkim: Boolean? = null,
  @Json(name = "verified_dmarc")
  val verifiedDmarc: Boolean? = null,
  @Json(name = "verified_arc")
  val verifiedArc: Boolean? = null,
  @Json(name = "created_at")
  val createdAt: Long? = null,
  @Json(name = "updated_at")
  val updatedAt: Long? = null,
)
