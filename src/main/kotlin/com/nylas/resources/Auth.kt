package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import okhttp3.HttpUrl
import java.io.IOException
import java.security.MessageDigest
import java.util.*

class Auth(
  private val client: NylasClient,
  private val clientId: String,
  private val clientSecret: String
) {

  fun grants(): Grants {
    return Grants(client)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun exchangeCodeForToken(request: CodeExchangeRequest): Response<CodeExchangeResponse> {
    val path = "/v3/connect/token"

    if(request.clientId == null) {
      request.clientId = clientId
    }
    if(request.clientSecret == null) {
      request.clientSecret = clientSecret
    }

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(CodeExchangeRequest::class.java)
      .toJson(request)

    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun refreshAccessToken(request: TokenExchangeRequest): Response<CodeExchangeResponse> {
    val path = "/v3/connect/token"

    if(request.clientId == null) {
      request.clientId = clientId
    }
    if(request.clientSecret == null) {
      request.clientSecret = clientSecret
    }

    val serializedRequestBody = JsonHelper.moshi()
      .adapter(TokenExchangeRequest::class.java)
      .toJson(request)

    return client.executePost(path, CodeExchangeResponse::class.java, serializedRequestBody)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun validateIDToken(token: String): Response<OpenIDResponse> {
    val url = "/v3/connect/tokeninfo?id_token=$token"

    return client.executeGet(url, OpenIDResponse::class.java)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun validateAccessToken(token: String): String {
    val url = "/v3/connect/tokeninfo?access_token=$token"

    return client.executeGet(url, OpenIDResponse::class.java)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun urlForAuthentication(config: UrlForAuthenticationConfig): String {
    return urlAuthBuilder(config).build().toString()
  }

  @Throws(IOException::class, NylasApiError::class)
  fun urlForAuthenticationPKCE(config: UrlForAuthenticationConfig): PKCEAuthURL {
    val urlBuilder = urlAuthBuilder(config)
    val secret = UUID.randomUUID().toString();

    val sha256Digest = MessageDigest.getInstance("SHA-256").digest(secret.toByteArray())
    val secretHash = Base64.getEncoder().encodeToString(sha256Digest)

    urlBuilder
      .addQueryParameter("code_challenge_method", "s256")
      .addQueryParameter("code_challenge", secretHash)

    return PKCEAuthURL(urlBuilder.build().toString(), secret, secretHash)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun urlForAdminConsent(config: UrlForAuthenticationConfig, credentialId: String): String {
    val urlBuilder = urlAuthBuilder(config)

    urlBuilder
      .addQueryParameter("response_type", "adminconsent")
      .addQueryParameter("credential_id", credentialId)

    return urlBuilder.build().toString()
  }

  @Throws(IOException::class, NylasApiError::class)
  fun hostedAuth(request: HostedAuthRequest): Response<HostedAuthResponse> {
    val path = "/v3/connect/auth"
    val serializedRequestBody = JsonHelper.moshi()
      .adapter(HostedAuthRequest::class.java)
      .toJson(request)

    return client.executePost(path, HostedAuthResponse::class.java, serializedRequestBody)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun revoke(accessToken: String): Boolean {
    val path = "/v3/connect/revoke?access_token=$accessToken"
    client.executePost<Any>(path)

    return true
  }

  @Throws(IOException::class, NylasApiError::class)
  private fun urlAuthBuilder(config: UrlForAuthenticationConfig): HttpUrl.Builder {
    val url = this.client.newUrlBuilder().addPathSegments("/v3/connect/auth")
    val json = JsonHelper.moshi()
      .adapter(UrlForAuthenticationConfig::class.java)
      .toJson(config)
    JsonHelper.jsonToMap(json).forEach { (key, value) ->
      url.addQueryParameter(key, value.toString())
    }

    return url
  }
}