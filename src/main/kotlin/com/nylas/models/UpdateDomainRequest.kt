package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update domain request.
 */
data class UpdateDomainRequest(
  @Json(name = "name")
  val name: String? = null,
) {
  /**
   * Builder for [UpdateDomainRequest].
   */
  class Builder {
    private var name: String? = null

    fun name(name: String?) = apply { this.name = name }

    fun build() = UpdateDomainRequest(name)
  }
}
