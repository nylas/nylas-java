package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing an object that points to a contact group ID.
 */
data class ContactGroupId(
  /**
   * The ID of the contact group.
   */
  @Json(name = "id")
  val id: String,
)
