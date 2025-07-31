package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.Buffer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.reflect.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RedirectUriTests {
  private val mockHttpClient: OkHttpClient = Mockito.mock(OkHttpClient::class.java)
  private val mockCall: Call = Mockito.mock(Call::class.java)
  private val mockResponse: okhttp3.Response = Mockito.mock(okhttp3.Response::class.java)
  private val mockResponseBody: ResponseBody = Mockito.mock(ResponseBody::class.java)
  private val mockOkHttpClientBuilder: OkHttpClient.Builder = Mockito.mock()

  @BeforeEach
  fun setup() {
    MockitoAnnotations.openMocks(this)
    whenever(mockOkHttpClientBuilder.addInterceptor(any<Interceptor>())).thenReturn(mockOkHttpClientBuilder)
    whenever(mockOkHttpClientBuilder.build()).thenReturn(mockHttpClient)
    whenever(mockHttpClient.newCall(any())).thenReturn(mockCall)
    whenever(mockCall.execute()).thenReturn(mockResponse)
    whenever(mockResponse.isSuccessful).thenReturn(true)
    whenever(mockResponse.body).thenReturn(mockResponseBody)
  }

  @Nested
  inner class SerializationTests {
    @Test
    fun `RedirectUri serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(RedirectUri::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "0556d035-6cb6-4262-a035-6b77e11cf8fc",
            "url": "http://localhost/abc",
            "platform": "web",
            "settings": {
              "origin": "string",
              "bundle_id": "string",
              "app_store_id": "string",
              "team_id": "string",
              "package_name": "string",
              "sha1_certificate_fingerprint": "string"
            }
          }
        """.trimIndent(),
      )

      val redirectUri = adapter.fromJson(jsonBuffer)!!
      assertEquals("0556d035-6cb6-4262-a035-6b77e11cf8fc", redirectUri.id)
      assertEquals("http://localhost/abc", redirectUri.url)
      assertEquals(Platform.WEB, redirectUri.platform)
      assertIs<RedirectUriSettings>(redirectUri.settings)
      assertEquals("string", redirectUri.settings?.origin)
      assertEquals("string", redirectUri.settings?.bundleId)
      assertEquals("string", redirectUri.settings?.appStoreId)
      assertEquals("string", redirectUri.settings?.teamId)
      assertEquals("string", redirectUri.settings?.packageName)
      assertEquals("string", redirectUri.settings?.sha1CertificateFingerprint)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var redirectUris: RedirectUris

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      redirectUris = RedirectUris(mockNylasClient)
    }

    @Test
    fun `listing redirectUris calls requests with the correct params`() {
      redirectUris.list()
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<RedirectUri>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/applications/redirect-uris", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, RedirectUri::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `finding a redirectUri calls requests with the correct params`() {
      val redirectUriId = "redirectUri-123"

      redirectUris.find(redirectUriId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<RedirectUri>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/applications/redirect-uris/$redirectUriId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, RedirectUri::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a redirectUri calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateRedirectUriRequest::class.java)
      val createRedirectUriRequest = CreateRedirectUriRequest(
        url = "http://localhost/abc",
        platform = Platform.WEB,
        settings = RedirectUriSettings(
          origin = "string",
          bundleId = "string",
          appStoreId = "string",
          teamId = "string",
          packageName = "string",
          sha1CertificateFingerprint = "string",
        ),
      )

      redirectUris.create(createRedirectUriRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<ListResponse<RedirectUri>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/applications/redirect-uris", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, RedirectUri::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createRedirectUriRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a redirectUri calls requests with the correct params`() {
      val redirectUriId = "redirectUri-123"
      val adapter = JsonHelper.moshi().adapter(UpdateRedirectUriRequest::class.java)
      val updateRedirectUriRequest = UpdateRedirectUriRequest(
        url = "http://localhost/abc",
        platform = Platform.WEB,
        settings = RedirectUriSettings(
          origin = "string",
          bundleId = "string",
          appStoreId = "string",
          teamId = "string",
          packageName = "string",
          sha1CertificateFingerprint = "string",
        ),
      )

      redirectUris.update(redirectUriId, updateRedirectUriRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePatch<ListResponse<RedirectUri>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/applications/redirect-uris/$redirectUriId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, RedirectUri::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateRedirectUriRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a redirectUri calls requests with the correct params`() {
      val redirectUriId = "redirectUri-123"

      redirectUris.destroy(redirectUriId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<ListResponse<RedirectUri>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/applications/redirect-uris/$redirectUriId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }
}
