package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.HttpUrl
import java.io.IOException
import java.security.MessageDigest
import java.util.*

/**
 * A collection of authentication related API endpoints
 *
 * These endpoints allow for various functionality related to authentication.
 * Also contains the Grants API and collection of provider API endpoints.
 *
 * @param client The configured Nylas API client
 * @param clientId The client ID for your Nylas Application
 * @param clientSecret The client secret for your Nylas Application
 */
class Auth(
  private val client: NylasClient,
  private val clientId: String,
  private val clientSecret: String,
) {

  /**
   * Access the Grants API
   * @return The Grants API
   */
  fun grants(): Grants {
    return Grants(client)
  }

  /**
   * Access the collection of provider related API endpoints
   * @return The collection of provider related API endpoints
   */
  fun providers(): Providers {
    return Providers(client, clientId)
  }

  /**
   * Exchange an authorization code for an access token
   * @param request The code exchange request
   * @return The response containing the access token
   */
  @Throws(IOException::class, NylasApiError::class)
  fun exchangeCodeForToken(request: CodeExchangeRequest): Response<CodeExchangeResponse> {
    val path = "v3/connect/token"

    if (request.clientId == null) {
      request.clientId = clientId
    }
    if (request.clientSecret == null) {
      request.clientSecret = clientSecret
    }

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CodeExchangeRequest::class.java)
      .toJson(request)
    val responseType = Types.newParameterizedType(Response::class.java, CodeExchangeResponse::class.java)

    return client.executePost(path, responseType, serializedRequestBody)
  }

  /**
   * Refresh an access token
   * @param request The refresh token request
   * @return The response containing the new access token
   */
  @Throws(IOException::class, NylasApiError::class)
  fun refreshAccessToken(request: TokenExchangeRequest): Response<CodeExchangeResponse> {
    val path = "v3/connect/token"

    if (request.clientId == null) {
      request.clientId = clientId
    }
    if (request.clientSecret == null) {
      request.clientSecret = clientSecret
    }

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(TokenExchangeRequest::class.java)
      .toJson(request)
    val responseType = Types.newParameterizedType(Response::class.java, CodeExchangeResponse::class.java)

    return client.executePost(path, responseType, serializedRequestBody)
  }

  /**
   * Validate and retrieve information about an ID token
   * @param token The ID token to validate
   * @return The response containing the ID token information
   */
  @Throws(IOException::class, NylasApiError::class)
  fun validateIDToken(token: String): Response<OpenIDResponse> {
    val url = "v3/connect/tokeninfo?id_token=$token"
    val responseType = Types.newParameterizedType(Response::class.java, OpenIDResponse::class.java)

    return client.executeGet(url, responseType)
  }

  /**
   * Validate and retrieve information about an access token
   * @param token The access token to validate
   * @return The response containing the access token information
   */
  @Throws(IOException::class, NylasApiError::class)
  fun validateAccessToken(token: String): String {
    val url = "v3/connect/tokeninfo?access_token=$token"
    val responseType = Types.newParameterizedType(Response::class.java, OpenIDResponse::class.java)

    return client.executeGet(url, responseType)
  }

  /**
   * Build the URL for authenticating users to your application via Hosted Authentication
   * @param config The configuration for building the URL
   * @return The URL for hosted authentication
   */
  @Throws(IOException::class, NylasApiError::class)
  fun urlForAuthentication(config: UrlForAuthenticationConfig): String {
    return urlAuthBuilder(config).build().toString()
  }

  /**
   * Build the URL for authenticating users to your application via Hosted Authentication with PKCE
   *
   * IMPORTANT: YOU WILL NEED TO STORE THE 'secret' returned to use it inside the CodeExchange flow
   *
   * @param config The configuration for building the URL
   * @return The URL for hosted authentication with secret & hashed secret
   */
  @Throws(IOException::class, NylasApiError::class)
  fun urlForAuthenticationPKCE(config: UrlForAuthenticationConfig): PKCEAuthURL {
    val urlBuilder = urlAuthBuilder(config)
    val secret = UUID.randomUUID().toString()

    val sha256Digest = MessageDigest.getInstance("SHA-256").digest(secret.toByteArray())
    val secretHash = Base64.getEncoder().encodeToString(sha256Digest)

    urlBuilder
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
  @Throws(IOException::class, NylasApiError::class)
  fun urlForAdminConsent(config: UrlForAuthenticationConfig, credentialId: String): String {
    val urlBuilder = urlAuthBuilder(config)

    urlBuilder
      .addQueryParameter("response_type", "adminconsent")
      .addQueryParameter("credential_id", credentialId)

    return urlBuilder.build().toString()
  }

  /**
   * Create a new authorization request and get a new unique login url.
   * Used only for hosted authentication.
   * This is the initial step requested from the server side to issue a new login url.
   * @param request The server side hosted auth request
   * @return The response containing the new login url
   */
  @Throws(IOException::class, NylasApiError::class)
  fun serverSideHostedAuth(request: ServerSideHostedAuthRequest): Response<ServerSideHostedAuthResponse> {
    val path = "v3/connect/auth"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(ServerSideHostedAuthRequest::class.java)
      .toJson(request)
    val responseType = Types.newParameterizedType(Response::class.java, ServerSideHostedAuthResponse::class.java)

    return client.executePost(path, responseType, serializedRequestBody)
  }

  /**
   * Revoke an access token
   * @param accessToken The access token to revoke
   * @return True if the access token was revoked successfully
   */
  @Throws(IOException::class, NylasApiError::class)
  fun revoke(accessToken: String): Boolean {
    val path = "v3/connect/revoke?access_token=$accessToken"
    client.executePost<Any>(path)

    return true
  }

  /**
   * Underlying function to build the Hosted Authentication URL
   * @param config The configuration for building the URL
   * @return The URL for hosted authentication
   */
  @Throws(IOException::class, NylasApiError::class)
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
