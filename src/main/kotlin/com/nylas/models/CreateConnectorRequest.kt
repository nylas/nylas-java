package com.nylas.models

import com.squareup.moshi.Json

/**
 * This sealed class represents the base Nylas connector creation request.
 */
sealed class CreateConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  open val name: String,
  /**
   * The provider type
   */
  @Json(name = "provider")
  val provider: AuthProvider,
)

/**
 * Class representing a Google connector creation request.
 */
data class CreateGoogleConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  override val name: String,
  /**
   * The Google OAuth provider credentials and settings
   */
  @Json(name = "settings")
  val settings: GoogleCreateConnectorSettings,
  /**
   * The Google OAuth scopes
   */
  @Json(name = "scope")
  val scope: List<String>?,
) : CreateConnectorRequest(name, AuthProvider.GOOGLE)

/**
 * Class representing a Microsoft connector creation request.
 */
data class CreateMicrosoftConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  override val name: String,
  /**
   * The Microsoft OAuth provider credentials and settings
   */
  @Json(name = "settings")
  val settings: MicrosoftCreateConnectorSettings,
  /**
   * The Microsoft OAuth scopes
   */
  @Json(name = "scope")
  val scope: List<String>?,
) : CreateConnectorRequest(name, AuthProvider.MICROSOFT)

/**
 * Class representing an IMAP connector creation request.
 */
data class CreateImapConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  override val name: String,
) : CreateConnectorRequest(name, AuthProvider.IMAP)

/**
 * Class representing a virtual calendar connector creation request.
 */
data class CreateVirtualCalendarConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  override val name: String,
) : CreateConnectorRequest(name, AuthProvider.VIRTUAL_CALENDAR)
