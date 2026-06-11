package com.nylas.models

import com.squareup.moshi.Json

/**
 * DNS verification types accepted by the Manage Domains API.
 */
enum class DomainVerificationType {
  @Json(name = "ownership")
  OWNERSHIP,

  @Json(name = "mx")
  MX,

  @Json(name = "spf")
  SPF,

  @Json(name = "dkim")
  DKIM,

  @Json(name = "feedback")
  FEEDBACK,

  @Json(name = "dmarc")
  DMARC,

  @Json(name = "arc")
  ARC,
}

/**
 * Status values returned by domain DNS verification attempts.
 */
enum class DomainVerificationStatus {
  @Json(name = "pending")
  PENDING,

  @Json(name = "done")
  DONE,

  @Json(name = "failed")
  FAILED,
}

/**
 * Class representation of a domain DNS verification request.
 */
data class DomainVerificationRequest(
  @Json(name = "type")
  val type: DomainVerificationType,
  @Json(name = "options")
  val options: Map<String, Any>? = null,
) {
  /**
   * Builder for [DomainVerificationRequest].
   */
  data class Builder(private val type: DomainVerificationType) {
    private var options: Map<String, Any>? = null

    /**
     * Set verification options.
     * @param options Verification options.
     * @return This builder.
     */
    fun options(options: Map<String, Any>?) = apply { this.options = options }

    /**
     * Build the [DomainVerificationRequest].
     * @return The built [DomainVerificationRequest].
     */
    fun build() = DomainVerificationRequest(type, options)
  }
}

/**
 * Class representation of a verification attempt returned by the API.
 */
data class DomainVerificationAttempt(
  @Json(name = "type")
  val type: DomainVerificationType? = null,
  @Json(name = "options")
  val options: Map<String, Any>? = null,
)

/**
 * Class representation of a domain verification result.
 */
data class DomainVerificationResult(
  @Json(name = "domain_id")
  val domainId: String? = null,
  @Json(name = "attempt")
  val attempt: DomainVerificationAttempt? = null,
  @Json(name = "status")
  val status: DomainVerificationStatus? = null,
  @Json(name = "created_at")
  val createdAt: Long? = null,
  @Json(name = "expires_at")
  val expiresAt: Long? = null,
  @Json(name = "details")
  val details: Map<String, Any>? = null,
  @Json(name = "message")
  val message: String? = null,
)
