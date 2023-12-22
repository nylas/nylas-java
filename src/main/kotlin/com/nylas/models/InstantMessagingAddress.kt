package com.nylas.models

/**
 * Class representation for an IM address in a contact.
 */
data class InstantMessagingAddress(
  val imAddress: String? = null,
  val type: ContactType? = null,
) {
  class Builder {
    private var imAddress: String? = null
    private var type: ContactType? = null

    fun imAddress(imAddress: String) = apply { this.imAddress = imAddress }
    fun type(type: ContactType) = apply { this.type = type }
    fun build() = InstantMessagingAddress(imAddress, type)
  }
}
