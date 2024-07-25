package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
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
   * @param overrides Optional request overrides to apply
   * @return The response containing the access token
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun exchangeCodeForToken(request: CodeExchangeRequest, overrides: RequestOverrides? = null): CodeExchangeResponse {
    val path = "v3/connect/token"
    if (request.clientSecret == null) {
      request.clientSecret = client.apiKey
    }

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CodeExchangeRequest::class.java)
      .toJson(request)

    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody, overrides = overrides)
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

    val secretHash = hashPkceSecret(secret)

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
   * Create a grant via custom authentication
   * @param requestBody The values to create the grant with
   * @param overrides Optional request overrides to apply
   * @return The created grant
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun customAuthentication(requestBody: CreateGrantRequest, overrides: RequestOverrides? = null): Response<Grant> {
    val path = "v3/connect/custom"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CreateGrantRequest::class.java)
      .toJson(requestBody)
    val responseType = Types.newParameterizedType(Response::class.java, Grant::class.java)

    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }

  /**
   * Refresh an access token
   * @param request The refresh token request
   * @param overrides Optional request overrides to apply
   * @return The response containing the new access token
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun refreshAccessToken(request: TokenExchangeRequest, overrides: RequestOverrides? = null): CodeExchangeResponse {
    val path = "v3/connect/token"
    if (request.clientSecret == null) {
      request.clientSecret = client.apiKey
    }

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(TokenExchangeRequest::class.java)
      .toJson(request)

    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody, overrides = overrides)
  }

  /**
   * Revoke a token (and the grant attached to the token)
   * @param token The token to revoke
   * @param overrides Optional request overrides to apply
   * @return True if the token was revoked successfully
   */
  @Throws(NylasOAuthError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun revoke(token: TokenParams, overrides: RequestOverrides? = null): Boolean {
    val path = "v3/connect/revoke"
    client.executePost<Any>(path, MutableMap::class.java, queryParams = token, overrides = overrides)

    return true
  }

  /**
   * Detect provider from email address
   * @param params The parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The detected provider, if found
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun detectProvider(params: ProviderDetectParams, overrides: RequestOverrides? = null): Response<ProviderDetectResponse> {
    val path = "v3/providers/detect"
    val responseType = Types.newParameterizedType(Response::class.java, ProviderDetectResponse::class.java)

    return client.executePost(path, responseType, queryParams = params, overrides = overrides)
  }

  /**
   * Get info about an ID token
   * @param idToken The ID token to query
   * @param overrides Optional request overrides to apply
   * @return The token information
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun idTokenInfo(idToken: String, overrides: RequestOverrides? = null): Response<TokenInfoResponse> {
    val params = TokenInfoRequest(idToken = idToken)
    return getTokenInfo(params, overrides)
  }

  /**
   * Get info about an access token
   * @param accessToken The access token to query
   * @param overrides Optional request overrides to apply
   * @return The token information
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun accessTokenInfo(accessToken: String, overrides: RequestOverrides? = null): Response<TokenInfoResponse> {
    val params = TokenInfoRequest(accessToken = accessToken)
    return getTokenInfo(params, overrides)
  }

  /**
   * Hash a plain text secret for use in PKCE
   * @param secret The plain text secret to hash
   * @return The hashed secret with base64 encoding (without padding)
   */
  private fun hashPkceSecret(secret: String): String {
    val sha256Digest = MessageDigest.getInstance("SHA-256")
    sha256Digest.update(secret.toByteArray())
    val hexString = sha256Digest.digest().joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
    return Base64.getEncoder().withoutPadding().encodeToString(hexString.toByteArray())
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
      if (key == "scope") {
        url.addQueryParameter("scope", config.scope?.joinToString(separator = " "))
      } else {
        url.addQueryParameter(key, value.toString())
      }
    }

    return url
  }

  private fun getTokenInfo(params: TokenInfoRequest, overrides: RequestOverrides?): Response<TokenInfoResponse> {
    val path = "v3/connect/tokeninfo"
    val responseType = Types.newParameterizedType(Response::class.java, TokenInfoResponse::class.java)
    return client.executeGet(path, responseType, queryParams = params, overrides = overrides)
  }
}
