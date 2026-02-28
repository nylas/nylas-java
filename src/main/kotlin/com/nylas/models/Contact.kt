package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas contact object
 */
data class Contact(
  @Json(name = "id")
  val id: String? = null,
  @Json(name = "grant_id")
  val grantId: String? = null,
  @Json(name = "object")
  private val obj: String = "contact",
  @Json(name = "birthday")
  val birthday: String? = null,
  @Json(name = "company_name")
  val companyName: String? = null,
  @Json(name = "display_name")
  val displayName: String? = null,
  @Json(name = "emails")
  val emails: List<ContactEmail>? = null,
  @Json(name = "im_addresses")
  val imAddresses: List<InstantMessagingAddress>? = null,
  @Json(name = "given_name")
  val givenName: String? = null,
  @Json(name = "job_title")
  val jobTitle: String? = null,
  @Json(name = "manager_name")
  val managerName: String? = null,
  @Json(name = "middle_name")
  val middleName: String? = null,
  @Json(name = "nickname")
  val nickname: String? = null,
  @Json(name = "notes")
  val notes: String? = null,
  @Json(name = "office_location")
  val officeLocation: String? = null,
  @Json(name = "picture_url")
  val pictureUrl: String? = null,
  @Json(name = "picture")
  val picture: String? = null,
  @Json(name = "suffix")
  val suffix: String? = null,
  @Json(name = "surname")
  val surname: String? = null,
  @Json(name = "source")
  val source: SourceType? = null,
  @Json(name = "phone_numbers")
  val phoneNumbers: List<PhoneNumber>? = null,
  @Json(name = "physical_addresses")
  val physicalAddresses: List<PhysicalAddress>? = null,
  @Json(name = "web_pages")
  val webPages: List<WebPage>? = null,
  @Json(name = "groups")
  val groups: List<ContactGroupId>? = null,
) {
  fun getObject() = obj
}
