package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update domain request.
 */
data class UpdateDomainRequest(
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "region")
  val region: String? = null,
  @Json(name = "tenant_key")
  val tenantKey: String? = null,
  @Json(name = "dkim_public_key")
  val dkimPublicKey: String? = null,
  @Json(name = "dkim_submitted_at")
  val dkimSubmittedAt: Long? = null,
  @Json(name = "verified_feedback")
  val verifiedFeedback: Boolean? = null,
) {
  /**
   * Builder for [UpdateDomainRequest].
   */
  class Builder {
    private var name: String? = null
    private var region: String? = null
    private var tenantKey: String? = null
    private var dkimPublicKey: String? = null
    private var dkimSubmittedAt: Long? = null
    private var verifiedFeedback: Boolean? = null

    fun name(name: String?) = apply { this.name = name }
    fun region(region: String?) = apply { this.region = region }
    fun tenantKey(tenantKey: String?) = apply { this.tenantKey = tenantKey }
    fun dkimPublicKey(dkimPublicKey: String?) = apply { this.dkimPublicKey = dkimPublicKey }
    fun dkimSubmittedAt(dkimSubmittedAt: Long?) = apply { this.dkimSubmittedAt = dkimSubmittedAt }
    fun verifiedFeedback(verifiedFeedback: Boolean?) = apply { this.verifiedFeedback = verifiedFeedback }

    fun build() = UpdateDomainRequest(name, region, tenantKey, dkimPublicKey, dkimSubmittedAt, verifiedFeedback)
  }
}
