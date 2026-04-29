package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas create list request.
 */
data class CreateNylasListRequest(
  /**
   * Name of the list (1–256 characters).
   */
  @Json(name = "name")
  val name: String,
  /**
   * The type of values this list will hold. Immutable after creation.
   */
  @Json(name = "type")
  val type: NylasListType,
  /**
   * Optional description of the list.
   */
  @Json(name = "description")
  val description: String? = null,
) {
  /**
   * Builder for [CreateNylasListRequest].
   * @param name Name of the list.
   * @param type The type of values this list will hold.
   */
  data class Builder(
    private val name: String,
    private val type: NylasListType,
  ) {
    private var description: String? = null

    /**
     * Set the description of the list.
     * @param description Optional description of the list.
     * @return The builder.
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * Build the [CreateNylasListRequest].
     * @return A [CreateNylasListRequest] with the provided values.
     */
    fun build() = CreateNylasListRequest(name, type, description)
  }
}
