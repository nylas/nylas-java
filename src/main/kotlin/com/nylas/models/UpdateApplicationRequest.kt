package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update application request.
 */
data class UpdateApplicationRequest(
  /**
   * Branding details for the application.
   */
  @Json(name = "branding")
  val branding: ApplicationDetails.Branding? = null,
  /**
   * Hosted authentication branding details.
   */
  @Json(name = "hosted_authentication")
  val hostedAuthentication: ApplicationDetails.HostedAuthentication? = null,
) {
  /**
   * Builder for [UpdateApplicationRequest].
   */
  class Builder {
    private var branding: ApplicationDetails.Branding? = null
    private var hostedAuthentication: ApplicationDetails.HostedAuthentication? = null

    /**
     * Set branding details for the application.
     * @param branding Branding details.
     * @return This builder.
     */
    fun branding(branding: ApplicationDetails.Branding?) = apply { this.branding = branding }

    /**
     * Set hosted authentication branding details.
     * @param hostedAuthentication Hosted authentication branding details.
     * @return This builder.
     */
    fun hostedAuthentication(hostedAuthentication: ApplicationDetails.HostedAuthentication?) =
      apply { this.hostedAuthentication = hostedAuthentication }

    /**
     * Build the [UpdateApplicationRequest].
     * @return The built [UpdateApplicationRequest].
     */
    fun build() = UpdateApplicationRequest(branding, hostedAuthentication)
  }
}
