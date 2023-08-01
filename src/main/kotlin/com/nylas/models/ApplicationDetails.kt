package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas application details object
 */
data class ApplicationDetails(
  /**
   * Public Application ID
   */
  @Json(name = "application_id")
  val applicationId: String,
  /**
   * ID of organization
   */
  @Json(name = "organization_id")
  val organizationId: String,
  /**
   * Region identifier
   */
  @Json(name = "region")
  val region: Region,
  /**
   * Environment identifier
   */
  @Json(name = "environment")
  val environment: Environment,
  /**
   * Branding details for the application
   */
  @Json(name = "branding")
  val branding: Branding,
  /**
   * Hosted authentication branding details
   */
  @Json(name = "hosted_authentication")
  val hostedAuthentication: HostedAuthentication?,
  /**
   * List of redirect URIs
   */
  @Json(name = "redirect_uris")
  val redirectUris: List<RedirectUri>?,
) {
  /**
   * Class representation of branding details for the application
   */
  data class Branding(
    /**
     * Name of the application
     */
    @Json(name = "name")
    val name: String,
    /**
     * URL points to application icon
     */
    @Json(name = "icon_url")
    val iconUrl: String?,
    /**
     * Application / publisher website URL
     */
    @Json(name = "website_url")
    val websiteUrl: String?,
    /**
     * Description of the application
     */
    @Json(name = "description")
    val description: String?,
  )

  /**
   * Class representation of hosted authentication branding details
   */
  data class HostedAuthentication(
    /**
     * URL of the background image
     */
    @Json(name = "background_image_url")
    val backgroundImageUrl: String?,
    /**
     * Alignment of background image
     */
    @Json(name = "alignment")
    val alignment: String?,
    /**
     * Primary color
     */
    @Json(name = "color_primary")
    val colorPrimary: String?,
    /**
     * Secondary color
     */
    @Json(name = "color_secondary")
    val colorSecondary: String?,
    /**
     * Title
     */
    @Json(name = "title")
    val title: String?,
    /**
     * Subtitle
     */
    @Json(name = "subtitle")
    val subtitle: String?,
    /**
     * Background color
     */
    @Json(name = "background_color")
    val backgroundColor: String?,
    /**
     * CSS spacing attribute in px
     */
    @Json(name = "spacing")
    val spacing: Int?,
  )
}
