package com.nylas.models

import com.squareup.moshi.Json

/**
 * Configuration for generating a URL for OAuth 2.0 authentication.
 */
data class UrlForAuthenticationConfig(
  /**
   * The client ID of your application.
   */
  @Json(name = "client_id")
  val clientId: String,
  /**
   * Redirect URI of the integration.
   */
  @Json(name = "redirect_uri")
  val redirectUri: String,
  /**
   * If the exchange token should return a refresh token too. Not suitable for client side or JavaScript apps.
   */
  @Json(name = "access_type")
  val accessType: AccessType = AccessType.ONLINE,
  /**
   * The integration provider type that you already had set up with Nylas for this application.
   * If not set, the user is directed to the Hosted Login screen and prompted to select a provider.
   */
  @Json(name = "provider")
  val provider: AuthProvider? = null,
  /**
   * The prompt parameter is used to force the consent screen to be displayed even if the user has already given consent to your application.
   */
  @Json(name = "prompt")
  val prompt: Prompt? = null,
  /**
   * A space-delimited list of scopes that identify the resources that your application could access on the user's behalf.
   * If no scope is given, all of the default integration's scopes are used.
   */
  @Json(name = "scope")
  val scope: List<String>? = null,
  /**
   * If set to true, the scopes granted to the application will be included in the response.
   */
  @Json(name = "include_grant_scopes")
  val includeGrantScopes: Boolean? = null,
  /**
   * Optional state to be returned after authentication
   */
  @Json(name = "state")
  val state: String? = null,
  /**
   * Prefill the login name (usually email) during authorization flow.
   * If a Grant for the provided email already exists, a Grant's re-auth will automatically be initiated.
   */
  @Json(name = "login_hint")
  val loginHint: String? = null,
  /**
   * The credential ID to use for authentication (for multi-credential setups).
   * Allowed when response_type is "code".
   */
  @Json(name = "credential_id")
  val credentialId: String? = null,
  /**
   * If set to true, the user will be required to enter their SMTP settings during authentication.
   * This is useful for IMAP-based providers to ensure SMTP credentials are collected for sending email.
   */
  @Json(name = "smtp_required")
  val smtpRequired: Boolean? = null,
) {
  /**
   * Builder for [UrlForAuthenticationConfig].
   * @property clientId The client ID of your application.
   * @property redirectUri Redirect URI of the integration.
   */
  data class Builder(
    private val clientId: String,
    private val redirectUri: String,
  ) {
    private var accessType: AccessType = AccessType.ONLINE
    private var provider: AuthProvider? = null
    private var prompt: Prompt? = null
    private var scope: List<String>? = null
    private var includeGrantScopes: Boolean? = null
    private var state: String? = null
    private var loginHint: String? = null
    private var credentialId: String? = null
    private var smtpRequired: Boolean? = null

    /**
     * Set the integration provider type that you already had set up with Nylas for this application.
     * If not set, the user is directed to the Hosted Login screen and prompted to select a provider.
     * @param provider The integration provider.
     * @return This builder.
     */
    fun provider(provider: AuthProvider) = apply { this.provider = provider }

    /**
     * Set the access type.
     * @param accessType The access type.
     * @return This builder.
     */
    fun accessType(accessType: AccessType) = apply { this.accessType = accessType }

    /**
     * Set the prompt parameter to force the consent screen to be displayed even if the user has already given consent to your application.
     * @param prompt The prompt parameter.
     * @return This builder.
     */
    fun prompt(prompt: Prompt) = apply { this.prompt = prompt }

    /**
     * Set the scopes that identify the resources that your application could access on the user's behalf.
     * If no scope is given, all of the default integration's scopes are used.
     * @param scope The scopes.
     * @return This builder.
     */
    fun scope(scope: List<String>) = apply { this.scope = scope }

    /**
     * Set whether the scopes granted to the application will be included in the response.
     * @param includeGrantScopes Whether the scopes granted to the application will be included in the response.
     * @return This builder.
     */
    fun includeGrantScopes(includeGrantScopes: Boolean) = apply { this.includeGrantScopes = includeGrantScopes }

    /**
     * Set the state to be returned after authentication.
     * @param state The state.
     * @return This builder.
     */
    fun state(state: String) = apply { this.state = state }

    /**
     * Set the login name (usually email) to prefill during authorization flow.
     * If a Grant for the provided email already exists, a Grant's re-auth will automatically be initiated.
     * @param loginHint The login name.
     * @return This builder.
     */
    fun loginHint(loginHint: String) = apply { this.loginHint = loginHint }

    /**
     * Set the credential ID to use for authentication (for multi-credential setups).
     * @param credentialId The credential ID.
     * @return This builder.
     */
    fun credentialId(credentialId: String) = apply { this.credentialId = credentialId }

    /**
     * Set whether the user is required to enter their SMTP settings during authentication.
     * This is useful for IMAP-based providers to ensure SMTP credentials are collected for sending email.
     * @param smtpRequired Whether SMTP settings are required.
     * @return This builder.
     */
    fun smtpRequired(smtpRequired: Boolean) = apply { this.smtpRequired = smtpRequired }

    /**
     * Build the [UrlForAuthenticationConfig].
     * @return The [UrlForAuthenticationConfig].
     */
    fun build() = UrlForAuthenticationConfig(
      clientId,
      redirectUri,
      accessType,
      provider,
      prompt,
      scope,
      includeGrantScopes,
      state,
      loginHint,
      credentialId,
      smtpRequired,
    )
  }
}
