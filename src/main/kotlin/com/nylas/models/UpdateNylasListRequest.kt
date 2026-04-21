package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update list request.
 * Note: only [name] and [description] can be updated — [NylasList.type] is immutable.
 */
data class UpdateNylasListRequest(
  /**
   * Updated name of the list.
   */
  @Json(name = "name")
  val name: String? = null,
  /**
   * Updated description of the list.
   */
  @Json(name = "description")
  val description: String? = null,
) {
  /**
   * Builder for [UpdateNylasListRequest].
   */
  class Builder {
    private var name: String? = null
    private var description: String? = null

    /**
     * Set the name of the list.
     * @param name Updated name of the list.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Set the description of the list.
     * @param description Updated description of the list.
     * @return The builder.
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * Build the [UpdateNylasListRequest].
     * @return An [UpdateNylasListRequest] with the provided values.
     */
    fun build() = UpdateNylasListRequest(name, description)
  }
}
