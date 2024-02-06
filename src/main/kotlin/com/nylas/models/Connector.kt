package com.nylas.models

import com.squareup.moshi.Json
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

/**
 * Class representation of the Nylas connector response.
 */
sealed class Connector(
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
    val settings: GoogleConnectorSettings,
    /**
     * The Google OAuth scopes
     */
    @Json(name = "scope")
    val scope: List<String>? = null,
  ) : Connector(AuthProvider.GOOGLE)

  /**
   * Class representing a Microsoft connector creation request.
   */
  data class Microsoft(
    /**
     * The Microsoft OAuth provider credentials and settings
     */
    @Json(name = "settings")
    val settings: MicrosoftConnectorSettings,
    /**
     * The Microsoft OAuth scopes
     */
    @Json(name = "scope")
    val scope: List<String>? = null,
  ) : Connector(AuthProvider.MICROSOFT)

  /**
   * Class representing an IMAP connector creation request.
   */
  class Imap : Connector(AuthProvider.IMAP)

  /**
   * Class representing a virtual calendar connector creation request.
   */
  class VirtualCalendar : Connector(AuthProvider.VIRTUAL_CALENDAR)

  companion object {
    @JvmStatic
    val CONNECTOR_JSON_ADAPTER_FACTORY: PolymorphicJsonAdapterFactory<Connector> =
      PolymorphicJsonAdapterFactory.of(Connector::class.java, "provider")
        .withSubtype(Google::class.java, AuthProvider.GOOGLE.value)
        .withSubtype(Microsoft::class.java, AuthProvider.MICROSOFT.value)
        .withSubtype(Imap::class.java, AuthProvider.IMAP.value)
        .withSubtype(VirtualCalendar::class.java, AuthProvider.VIRTUAL_CALENDAR.value)
  }
}
