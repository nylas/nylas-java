package com.nylas.models

import com.squareup.moshi.Json

data class ApplicationDetails(
  @Json(name = "application_id")
  val applicationId: String,
  @Json(name = "organization_id")
  val organizationId: String,
  @Json(name = "region")
  val region: Region,
  @Json(name = "environment")
  val environment: Environment,
  @Json(name = "branding")
  val branding: Branding,
  @Json(name = "hosted_authentication")
  val hostedAuthentication: HostedAuthentication?,
  @Json(name = "redirect_uris")
  val redirectUris: List<RedirectUri>?,
) {
  data class Branding(
    @Json(name = "name")
    val name: String,
    @Json(name = "icon_url")
    val iconUrl: String?,
    @Json(name = "website_url")
    val websiteUrl: String?,
    @Json(name = "description")
    val description: String?,
  )

  data class HostedAuthentication(
    @Json(name = "background_image_url")
    val backgroundImageUrl: String?,
    @Json(name = "alignment")
    val alignment: String?,
    @Json(name = "color_primary")
    val colorPrimary: String?,
    @Json(name = "color_secondary")
    val colorSecondary: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "subtitle")
    val subtitle: String?,
    @Json(name = "background_color")
    val backgroundColor: String?,
    @Json(name = "spacing")
    val spacing: Int?,
  )
}
