package com.nylas.models

import com.squareup.moshi.Json

/**
 * Custom headers to be used when drafting or sending an email.
 */
data class CustomHeader(
  /**
   * The name of the custom header.
   */
  @Json(name = "name")
  val name: String,
  /**
   * The value of the custom header.
   */
  @Json(name = "value")
  val value: String,
) {
  /**
   * Builder for [CustomHeader].
   * @property name The name of the custom header.
   * @property value The value of the custom header.
   */
  data class Builder(
    private val name: String,
    private val value: String,
  ) {
    /**
     * Build the [CustomHeader] object.
     */
    fun build(): CustomHeader {
      return CustomHeader(name, value)
    }
  }
}
