package com.nylas.models

import com.squareup.moshi.Json

data class CreateContactRequest(
  @Json(name = "display_name")
  val displayName: String? = null,
  @Json(name = "birthday")
  val birthday: String? = null,
  @Json(name = "company_name")
  val companyName: String? = null,
  @Json(name = "emails")
  val emails: List<ContactEmail>? = null,
  @Json(name = "given_name")
  val givenName: String? = null,
  @Json(name = "im_addresses")
  val imAddresses: List<InstantMessagingAddress>? = null,
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
  @Json(name = "phone_numbers")
  val phoneNumbers: List<PhoneNumber>? = null,
  @Json(name = "physical_addresses")
  val physicalAddresses: List<PhysicalAddress>? = null,
  @Json(name = "suffix")
  val suffix: String? = null,
  @Json(name = "surname")
  val surname: String? = null,
  @Json(name = "web_pages")
  val webPages: List<WebPage>? = null,
  @Json(name = "picture")
  val picture: String? = null,
  @Json(name = "source")
  val source: SourceType? = null,
  @Json(name = "groups")
  val groups: List<ContactGroup>? = null,
) {
  class Builder {
    private var displayName: String? = null
    private var birthday: String? = null
    private var companyName: String? = null
    private var emails: List<ContactEmail>? = null
    private var givenName: String? = null
    private var imAddresses: List<InstantMessagingAddress>? = null
    private var jobTitle: String? = null
    private var managerName: String? = null
    private var middleName: String? = null
    private var nickname: String? = null
    private var notes: String? = null
    private var officeLocation: String? = null
    private var phoneNumbers: List<PhoneNumber>? = null
    private var physicalAddresses: List<PhysicalAddress>? = null
    private var suffix: String? = null
    private var surname: String? = null
    private var webPages: List<WebPage>? = null
    private var picture: String? = null
    private var source: SourceType? = null
    private var groups: List<ContactGroup>? = null

    fun displayName(displayName: String?) = apply { this.displayName = displayName }
    fun birthday(birthday: String?) = apply { this.birthday = birthday }
    fun companyName(companyName: String?) = apply { this.companyName = companyName }
    fun emails(emails: List<ContactEmail>?) = apply { this.emails = emails }
    fun givenName(givenName: String?) = apply { this.givenName = givenName }
    fun imAddresses(imAddresses: List<InstantMessagingAddress>?) = apply { this.imAddresses = imAddresses }
    fun jobTitle(jobTitle: String?) = apply { this.jobTitle = jobTitle }
    fun managerName(managerName: String?) = apply { this.managerName = managerName }
    fun middleName(middleName: String?) = apply { this.middleName = middleName }
    fun nickname(nickname: String?) = apply { this.nickname = nickname }
    fun notes(notes: String?) = apply { this.notes = notes }
    fun officeLocation(officeLocation: String?) = apply { this.officeLocation = officeLocation }
    fun phoneNumbers(phoneNumbers: List<PhoneNumber>?) = apply { this.phoneNumbers = phoneNumbers }
    fun physicalAddresses(physicalAddresses: List<PhysicalAddress>?) = apply { this.physicalAddresses = physicalAddresses }
    fun suffix(suffix: String?) = apply { this.suffix = suffix }
    fun surname(surname: String?) = apply { this.surname = surname }
    fun webPages(webPages: List<WebPage>?) = apply { this.webPages = webPages }
    fun picture(picture: String?) = apply { this.picture = picture }
    fun source(source: SourceType?) = apply { this.source = source }
    fun groups(groups: List<ContactGroup>?) = apply { this.groups = groups }

    fun build() = CreateContactRequest(
      displayName = displayName,
      birthday = birthday,
      companyName = companyName,
      emails = emails,
      givenName = givenName,
      imAddresses = imAddresses,
      jobTitle = jobTitle,
      managerName = managerName,
      middleName = middleName,
      nickname = nickname,
      notes = notes,
      officeLocation = officeLocation,
      phoneNumbers = phoneNumbers,
      physicalAddresses = physicalAddresses,
      suffix = suffix,
      surname = surname,
      webPages = webPages,
      picture = picture,
      source = source,
      groups = groups,
    )
  }
}
