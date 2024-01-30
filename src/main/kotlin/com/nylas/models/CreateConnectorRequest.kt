package com.nylas.models

import com.squareup.moshi.Json
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

/**
 * This sealed class represents the base Nylas connector creation request.
 */
sealed class CreateConnectorRequest(
  /**
   * The provider type
   */
  @Json(name = "provider")
  val provider: AuthProvider,
) {
  /**
   * Class representing a Google connector creation request.
   */
  data class Google(
    /**
     * The Google OAuth provider credentials and settings
     */
    @Json(name = "settings")
    val settings: GoogleCreateConnectorSettings,
    /**
     * The Google OAuth scopes
     */
    @Json(name = "scope")
    val scope: List<String>? = null,
  ) : CreateConnectorRequest(AuthProvider.GOOGLE) {
    /**
     * Builder for Google connector creation requests.
     * @param settings The Google OAuth provider credentials and settings
     */
    data class Builder(
      private val settings: GoogleCreateConnectorSettings,
    ) {
      private var scope: List<String>? = null

      /**
       * Set the Google OAuth scopes
       * @param scope The Google OAuth scopes
       * @return The builder
       */
      fun scope(scope: List<String>) = apply { this.scope = scope }

      /**
       * Build the Google connector creation request
       * @return The Google connector creation request
       */
      fun build() = Google(settings, scope)
    }
  }

  /**
   * Class representing a Microsoft connector creation request.
   */
  data class Microsoft(
    /**
     * The Microsoft OAuth provider credentials and settings
     */
    @Json(name = "settings")
    val settings: MicrosoftCreateConnectorSettings,
    /**
     * The Microsoft OAuth scopes
     */
    @Json(name = "scope")
    val scope: List<String>? = null,
  ) : CreateConnectorRequest(AuthProvider.MICROSOFT) {
    /**
     * Builder for Microsoft connector creation requests.
     * @param settings The Microsoft OAuth provider credentials and settings
     */
    data class Builder(
      private val settings: MicrosoftCreateConnectorSettings,
    ) {
      private var scope: List<String>? = null

      /**
       * Set the Microsoft OAuth scopes
       * @param scope The Microsoft OAuth scopes
       * @return The builder
       */
      fun scope(scope: List<String>) = apply { this.scope = scope }

      /**
       * Build the Microsoft connector creation request
       * @return The Microsoft connector creation request
       */
      fun build() = Microsoft(settings, scope)
    }
  }

  /**
   * Class representing an IMAP connector creation request.
   */
  class Imap : CreateConnectorRequest(AuthProvider.IMAP)

  /**
   * Class representing a virtual calendar connector creation request.
   */
  class VirtualCalendar : CreateConnectorRequest(AuthProvider.VIRTUAL_CALENDAR)

  companion object {
    @JvmStatic
    val CREATE_CONNECTOR_JSON_ADAPTER_FACTORY: PolymorphicJsonAdapterFactory<CreateConnectorRequest> = PolymorphicJsonAdapterFactory.of(CreateConnectorRequest::class.java, "provider")
      .withSubtype(Google::class.java, AuthProvider.GOOGLE.value)
      .withSubtype(Microsoft::class.java, AuthProvider.MICROSOFT.value)
      .withSubtype(Imap::class.java, AuthProvider.IMAP.value)
      .withSubtype(VirtualCalendar::class.java, AuthProvider.VIRTUAL_CALENDAR.value)
  }
}
