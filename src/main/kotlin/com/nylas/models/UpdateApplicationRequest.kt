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
  val branding: Branding? = null,
  /**
   * Hosted authentication branding details.
   */
  @Json(name = "hosted_authentication")
  val hostedAuthentication: ApplicationDetails.HostedAuthentication? = null,
  /**
   * List of callback URIs for the application.
   */
  @Json(name = "callback_uris")
  val callbackUris: List<UpdateApplicationRedirectUriRequest>? = null,
) {
  /**
   * Builder for [UpdateApplicationRequest].
   */
  class Builder {
    private var branding: Branding? = null
    private var hostedAuthentication: ApplicationDetails.HostedAuthentication? = null
    private var callbackUris: List<UpdateApplicationRedirectUriRequest>? = null

    /**
     * Set branding details for the application.
     * @param branding Branding details.
     * @return This builder.
     */
    fun branding(branding: Branding?) = apply { this.branding = branding }

    /**
     * Set hosted authentication branding details.
     * @param hostedAuthentication Hosted authentication branding details.
     * @return This builder.
     */
    fun hostedAuthentication(hostedAuthentication: ApplicationDetails.HostedAuthentication?) =
      apply { this.hostedAuthentication = hostedAuthentication }

    /**
     * Set callback URIs for the application.
     * @param callbackUris List of callback URIs.
     * @return This builder.
     */
    fun callbackUris(callbackUris: List<UpdateApplicationRedirectUriRequest>?) = apply { this.callbackUris = callbackUris }

    /**
     * Build the [UpdateApplicationRequest].
     * @return The built [UpdateApplicationRequest].
     */
    fun build() = UpdateApplicationRequest(branding, hostedAuthentication, callbackUris)
  }

  /**
   * Branding fields accepted by application updates. All fields are optional
   * because PATCH /v3/applications accepts sparse branding changes.
   */
  data class Branding(
    /**
     * Name of the application.
     */
    @Json(name = "name")
    val name: String? = null,
    /**
     * URL points to application icon.
     */
    @Json(name = "icon_url")
    val iconUrl: String? = null,
    /**
     * Application / publisher website URL.
     */
    @Json(name = "website_url")
    val websiteUrl: String? = null,
    /**
     * Description of the application.
     */
    @Json(name = "description")
    val description: String? = null,
  )
}

/**
 * Callback URI shape accepted by application updates.
 */
data class UpdateApplicationRedirectUriRequest(
  /**
   * Existing callback URI ID. Include this when preserving or updating an existing URI.
   */
  @Json(name = "id")
  val id: String? = null,
  /**
   * Redirect URL.
   */
  @Json(name = "url")
  val url: String,
  /**
   * Platform identifier.
   */
  @Json(name = "platform")
  val platform: Platform,
  /**
   * Optional settings for the redirect URI.
   */
  @Json(name = "settings")
  val settings: RedirectUriSettings? = null,
)
