package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import okhttp3.HttpUrl
import java.security.MessageDigest
import java.util.*

/**
 * A collection of authentication related API endpoints
 *
 * These endpoints allow for various functionality related to authentication.
 * Also contains the Grants API and collection of provider API endpoints.
 *
 * @param client The configured Nylas API client
 */
class Auth(private val client: NylasClient) {

  /**
   * Access the Grants API
   * @return The Grants API
   */
  fun grants(): Grants {
    return Grants(client)
  }

  /**
   * Access the collection of provider related API endpoints
   * @param clientId The client ID for your Nylas Application
   * @return The collection of provider related API endpoints
   */
  fun providers(clientId: String): Providers {
    return Providers(client, clientId)
  }

  /**
   * Build the URL for authenticating users to your application with OAuth 2.0
   * @param config The configuration for building the URL
   * @return The URL for hosted authentication
   */
  fun urlForOAuth2(config: UrlForAuthenticationConfig): String {
    val urlBuilder = urlAuthBuilder(config)

    urlBuilder.addQueryParameter("response_type", "code")

    return urlBuilder.build().toString()
  }

  /**
   * Exchange an authorization code for an access token
   * @param request The code exchange request
   * @return The response containing the access token
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  fun exchangeCodeForToken(request: CodeExchangeRequest): CodeExchangeResponse {
    val path = "v3/connect/token"

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CodeExchangeRequest::class.java)
      .toJson(request)

    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody)
  }

  /**
   * Build the URL for authenticating users to your application with OAuth 2.0 and PKCE
   *
   * IMPORTANT: YOU WILL NEED TO STORE THE 'secret' returned to use it inside the CodeExchange flow
   *
   * @param config The configuration for building the URL
   * @return The URL for hosted authentication with secret & hashed secret
   */
  fun urlForOAuth2PKCE(config: UrlForAuthenticationConfig): PKCEAuthURL {
    val urlBuilder = urlAuthBuilder(config)
    val secret = UUID.randomUUID().toString()

    val sha256Digest = MessageDigest.getInstance("SHA-256").digest(secret.toByteArray())
    val secretHash = Base64.getEncoder().encodeToString(sha256Digest)

    urlBuilder
      .addQueryParameter("response_type", "code")
      .addQueryParameter("code_challenge_method", "s256")
      .addQueryParameter("code_challenge", secretHash)

    return PKCEAuthURL(urlBuilder.build().toString(), secret, secretHash)
  }

  /**
   * Build the URL for admin consent authentication for Microsoft
   * @param config The configuration for building the URL
   * @param credentialId The credential ID for the Microsoft account
   * @return The URL for admin consent authentication
   */
  fun urlForAdminConsent(config: UrlForAuthenticationConfig, credentialId: String): String {
    val urlBuilder = urlAuthBuilder(config)

    urlBuilder
      .addQueryParameter("response_type", "adminconsent")
      .addQueryParameter("credential_id", credentialId)

    return urlBuilder.build().toString()
  }

  /**
   * Refresh an access token
   * @param request The refresh token request
   * @return The response containing the new access token
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  fun refreshAccessToken(request: TokenExchangeRequest): CodeExchangeResponse {
    val path = "v3/connect/token"

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(TokenExchangeRequest::class.java)
      .toJson(request)

    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody)
  }

  /**
   * Revoke a token (and the grant attached to the token)
   * @param token The token to revoke
   * @return True if the token was revoked successfully
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  fun revoke(token: String): Boolean {
    val path = "v3/connect/revoke?token=$token"
    client.executePost<Any>(path)

    return true
  }

  /**
   * Underlying function to build the Hosted Authentication URL
   * @param config The configuration for building the URL
   * @return The URL for hosted authentication
   */
  private fun urlAuthBuilder(config: UrlForAuthenticationConfig): HttpUrl.Builder {
    val url = this.client.newUrlBuilder().addPathSegments("v3/connect/auth")
    val json = JsonHelper.moshi()
      .adapter(UrlForAuthenticationConfig::class.java)
      .toJson(config)
    JsonHelper.jsonToMap(json).forEach { (key, value) ->
      url.addQueryParameter(key, value.toString())
    }

    return url
  }
}
