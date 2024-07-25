package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.reflect.Type
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthTests {
  private val mockHttpClient: OkHttpClient = Mockito.mock(OkHttpClient::class.java)
  private val mockCall: Call = Mockito.mock(Call::class.java)
  private val mockResponse: okhttp3.Response = Mockito.mock(okhttp3.Response::class.java)
  private val mockResponseBody: ResponseBody = Mockito.mock(ResponseBody::class.java)
  private val mockOkHttpClientBuilder: OkHttpClient.Builder = Mockito.mock()
  private val baseUrl = "https://api.test.nylas.com"
  private lateinit var grantId: String
  private lateinit var mockNylasClient: NylasClient
  private lateinit var auth: Auth

  @BeforeEach
  fun setup() {
    MockitoAnnotations.openMocks(this)
    whenever(mockOkHttpClientBuilder.addInterceptor(any())).thenReturn(mockOkHttpClientBuilder)
    whenever(mockOkHttpClientBuilder.build()).thenReturn(mockHttpClient)
    whenever(mockHttpClient.newCall(any())).thenReturn(mockCall)
    whenever(mockCall.execute()).thenReturn(mockResponse)
    whenever(mockResponse.isSuccessful).thenReturn(true)
    whenever(mockResponse.body()).thenReturn(mockResponseBody)
    grantId = "abc-123-grant-id"
    mockNylasClient = Mockito.mock(NylasClient::class.java)
    whenever(mockNylasClient.newUrlBuilder()).thenReturn(HttpUrl.get(baseUrl).newBuilder())
    whenever(mockNylasClient.apiKey).thenReturn("test-api-key")
    auth = Auth(mockNylasClient)
  }

  @Nested
  inner class OAuthTests {
    private val config = UrlForAuthenticationConfig(
      clientId = "abc-123",
      redirectUri = "https://example.com/oauth/callback",
      scope = listOf("email.read_only", "calendar", "contacts"),
      loginHint = "test@gmail.com",
      provider = AuthProvider.GOOGLE,
      prompt = Prompt.SELECT_PROVIDER_DETECT,
      state = "abc-123-state",
    )

    @Test
    fun `urlForOAuth2 should return the correct URL`() {
      val url = auth.urlForOAuth2(config)

      assert(url == "https://api.test.nylas.com/v3/connect/auth?client_id=abc-123&redirect_uri=https%3A%2F%2Fexample.com%2Foauth%2Fcallback&access_type=online&provider=google&prompt=select_provider%2Cdetect&scope=email.read_only%20calendar%20contacts&state=abc-123-state&login_hint=test%40gmail.com&response_type=code")
    }

    @Test
    fun `urlForOAuth2PKCE should return correct object with secret hashed and set`() {
      val config = UrlForAuthenticationConfig(
        clientId = "abc-123",
        redirectUri = "https://example.com/oauth/callback",
        scope = listOf("email.read_only", "calendar", "contacts"),
        loginHint = "test@gmail.com",
        provider = AuthProvider.GOOGLE,
        prompt = Prompt.SELECT_PROVIDER_DETECT,
        state = "abc-123-state",
      )
      val mockUuid: UUID = Mockito.mock()
      whenever(mockUuid.toString()).thenReturn("nylas")
      val staticUuid = Mockito.mockStatic(UUID::class.java)
      staticUuid.`when`<UUID> { UUID.randomUUID() }.thenReturn(mockUuid)

      val pkceAuthURL = auth.urlForOAuth2PKCE(config)

      assert(pkceAuthURL.url == "https://api.test.nylas.com/v3/connect/auth?client_id=abc-123&redirect_uri=https%3A%2F%2Fexample.com%2Foauth%2Fcallback&access_type=online&provider=google&prompt=select_provider%2Cdetect&scope=email.read_only%20calendar%20contacts&state=abc-123-state&login_hint=test%40gmail.com&response_type=code&code_challenge_method=s256&code_challenge=ZTk2YmY2Njg2YTNjMzUxMGU5ZTkyN2RiNzA2OWNiMWNiYTliOTliMDIyZjQ5NDgzYTZjZTMyNzA4MDllNjhhMg")
      assert(pkceAuthURL.secret == "nylas")
      assert(pkceAuthURL.secretHash == "ZTk2YmY2Njg2YTNjMzUxMGU5ZTkyN2RiNzA2OWNiMWNiYTliOTliMDIyZjQ5NDgzYTZjZTMyNzA4MDllNjhhMg")
    }

    @Test
    fun `urlForAdminConsent should return the correct URL`() {
      val url = auth.urlForAdminConsent(config, "abc-123-credential-id")

      assert(url == "https://api.test.nylas.com/v3/connect/auth?client_id=abc-123&redirect_uri=https%3A%2F%2Fexample.com%2Foauth%2Fcallback&access_type=online&provider=google&prompt=select_provider%2Cdetect&scope=email.read_only%20calendar%20contacts&state=abc-123-state&login_hint=test%40gmail.com&response_type=adminconsent&credential_id=abc-123-credential-id")
    }
  }

  @Nested
  inner class TokenTests {
    @Test
    fun `exchangeCodeForToken calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CodeExchangeRequest::class.java)
      val request = CodeExchangeRequest(
        redirectUri = "https://example.com/oauth/callback",
        code = "abc-123-code",
        clientId = "abc-123",
        clientSecret = "abc-123-secret",
        codeVerifier = "abc-123-verifier",
      )

      auth.exchangeCodeForToken(request)

      val json = adapter.toJson(request)
      val jsonMap = JsonHelper.jsonToMap(json)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<CodeExchangeResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connect/token", pathCaptor.firstValue)
      assertEquals(CodeExchangeResponse::class.java, typeCaptor.firstValue)
      assertEquals(json, requestBodyCaptor.firstValue)
      assertEquals("authorization_code", jsonMap["grant_type"])
    }

    @Test
    fun `exchangeCodeForToken clientSecret defaults to API key`() {
      val adapter = JsonHelper.moshi().adapter(CodeExchangeRequest::class.java)
      val request = CodeExchangeRequest(
        redirectUri = "https://example.com/oauth/callback",
        code = "abc-123-code",
        clientId = "abc-123",
        codeVerifier = "abc-123-verifier",
      )
      val expectedRequest = mapOf(
        "redirect_uri" to "https://example.com/oauth/callback",
        "code" to "abc-123-code",
        "client_id" to "abc-123",
        "client_secret" to "test-api-key",
        "code_verifier" to "abc-123-verifier",
        "grant_type" to "authorization_code",
      )

      auth.exchangeCodeForToken(request)

      val json = adapter.toJson(request)
      val jsonMap = JsonHelper.jsonToMap(json)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<CodeExchangeResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connect/token", pathCaptor.firstValue)
      assertEquals(CodeExchangeResponse::class.java, typeCaptor.firstValue)
      assertEquals(jsonMap, expectedRequest)
      assertEquals("authorization_code", jsonMap["grant_type"])
    }

    @Test
    fun `refreshAccessToken calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(TokenExchangeRequest::class.java)
      val request = TokenExchangeRequest(
        redirectUri = "https://example.com/oauth/callback",
        refreshToken = "abc-123-refresh-token",
        clientId = "abc-123",
        clientSecret = "abc-123-secret",
      )

      auth.refreshAccessToken(request)

      val json = adapter.toJson(request)
      val jsonMap = JsonHelper.jsonToMap(json)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<CodeExchangeResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connect/token", pathCaptor.firstValue)
      assertEquals(CodeExchangeResponse::class.java, typeCaptor.firstValue)
      assertEquals(json, requestBodyCaptor.firstValue)
      assertEquals("refresh_token", jsonMap["grant_type"])
    }

    @Test
    fun `refreshAccessToken clientSecret defaults to API`() {
      val adapter = JsonHelper.moshi().adapter(TokenExchangeRequest::class.java)
      val request = TokenExchangeRequest(
        redirectUri = "https://example.com/oauth/callback",
        refreshToken = "abc-123-refresh-token",
        clientId = "abc-123",
      )
      val expectedRequest = mapOf(
        "redirect_uri" to "https://example.com/oauth/callback",
        "client_id" to "abc-123",
        "client_secret" to "test-api-key",
        "refresh_token" to "abc-123-refresh-token",
        "grant_type" to "refresh_token",
      )

      auth.refreshAccessToken(request)

      val json = adapter.toJson(request)
      val jsonMap = JsonHelper.jsonToMap(json)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<CodeExchangeResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connect/token", pathCaptor.firstValue)
      assertEquals(CodeExchangeResponse::class.java, typeCaptor.firstValue)
      assertEquals(jsonMap, expectedRequest)
      assertEquals("refresh_token", jsonMap["grant_type"])
    }
  }

  @Nested
  inner class OtherAuthTests {
    @Test
    fun `customAuthentication calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateGrantRequest::class.java)
      val request = CreateGrantRequest(
        provider = AuthProvider.GOOGLE,
        settings = mapOf("email" to "test@nylas.com"),
        state = "abc-123-state",
        scope = listOf("email.read_only", "calendar", "contacts"),
      )

      auth.customAuthentication(request)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<CodeExchangeResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connect/custom", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Grant::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(request), requestBodyCaptor.firstValue)
    }

    @Test
    fun `detectProvider calls requests with the correct params`() {
      val request = ProviderDetectParams(
        email = "test@nylas.com",
        allProviderTypes = true,
      )

      auth.detectProvider(request)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ProviderDetectParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<CodeExchangeResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/providers/detect", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, ProviderDetectResponse::class.java), typeCaptor.firstValue)
      assertEquals(request, queryParamCaptor.firstValue)
    }

    @Test
    fun `revoke calls requests with the correct params`() {
      val token = "user-token"

      val res = auth.revoke(TokenParams(token))

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ProviderDetectParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<CodeExchangeResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connect/revoke", pathCaptor.firstValue)
      assertNull(requestBodyCaptor.firstValue)
      assertEquals(true, res)
    }

    @Test
    fun `idTokenInfo calls requests with the correct params`() {
      val idToken = "user-id-token"

      auth.idTokenInfo(idToken)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<TokenInfoRequest>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<TokenInfoResponse>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connect/tokeninfo", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, TokenInfoResponse::class.java), typeCaptor.firstValue)
      assertEquals(TokenInfoRequest(idToken = idToken), queryParamCaptor.firstValue)
    }

    @Test
    fun `accessTokenInfo calls requests with the correct params`() {
      val accessToken = "nylas-access-token"

      auth.accessTokenInfo(accessToken)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<TokenInfoRequest>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<TokenInfoResponse>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connect/tokeninfo", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, TokenInfoResponse::class.java), typeCaptor.firstValue)
      assertEquals(TokenInfoRequest(accessToken = accessToken), queryParamCaptor.firstValue)
    }
  }
}
