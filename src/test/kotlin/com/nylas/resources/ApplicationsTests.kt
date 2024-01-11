package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.Call
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

class ApplicationsTests {
  private val mockHttpClient: OkHttpClient = Mockito.mock(OkHttpClient::class.java)
  private val mockCall: Call = Mockito.mock(Call::class.java)
  private val mockResponse: okhttp3.Response = Mockito.mock(okhttp3.Response::class.java)
  private val mockResponseBody: ResponseBody = Mockito.mock(ResponseBody::class.java)
  private val mockOkHttpClientBuilder: OkHttpClient.Builder = Mockito.mock()

  @BeforeEach
  fun setup() {
    MockitoAnnotations.openMocks(this)
    whenever(mockOkHttpClientBuilder.addInterceptor(any())).thenReturn(mockOkHttpClientBuilder)
    whenever(mockOkHttpClientBuilder.build()).thenReturn(mockHttpClient)
    whenever(mockHttpClient.newCall(any())).thenReturn(mockCall)
    whenever(mockCall.execute()).thenReturn(mockResponse)
    whenever(mockResponse.isSuccessful).thenReturn(true)
    whenever(mockResponse.body()).thenReturn(mockResponseBody)
  }

  @Nested
  inner class SerializationTests {
    @Test
    fun `ApplicationDetails serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(ApplicationDetails::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "application_id": "ad410018-d306-43f9-8361-fa5d7b2172e0",
            "organization_id": "f5db4482-dbbe-4b32-b347-61c260d803ce",
            "region": "us",
            "environment": "production",
            "branding": {
              "name": "My application",
              "icon_url": "https://my-app.com/my-icon.png",
              "website_url": "https://my-app.com",
              "description": "Online banking application."
            },
            "hosted_authentication": {
              "background_image_url": "https://my-app.com/bg.jpg",
              "alignment": "left",
              "color_primary": "#dc0000",
              "color_secondary": "#000056",
              "title": "string",
              "subtitle": "string",
              "background_color": "#003400",
              "spacing": 5
            },
            "callback_uris": [
              {
                "id": "0556d035-6cb6-4262-a035-6b77e11cf8fc",
                "url": "string",
                "platform": "web",
                "settings": {
                  "origin": "string",
                  "bundle_id": "string",
                  "package_name": "string",
                  "sha1_certificate_fingerprint": "string"
                }
              }
            ]
          }
        """.trimIndent(),
      )

      val app = adapter.fromJson(jsonBuffer)!!
      assertIs<ApplicationDetails>(app)
      assertEquals("ad410018-d306-43f9-8361-fa5d7b2172e0", app.applicationId)
      assertEquals("f5db4482-dbbe-4b32-b347-61c260d803ce", app.organizationId)
      assertEquals(Region.US, app.region)
      assertEquals(Environment.PRODUCTION, app.environment)
      assertIs<ApplicationDetails.Branding>(app.branding)
      assertEquals("My application", app.branding.name)
      assertEquals("https://my-app.com/my-icon.png", app.branding.iconUrl)
      assertEquals("https://my-app.com", app.branding.websiteUrl)
      assertEquals("Online banking application.", app.branding.description)
      assertIs<ApplicationDetails.HostedAuthentication>(app.hostedAuthentication)
      assertEquals("https://my-app.com/bg.jpg", app.hostedAuthentication?.backgroundImageUrl)
      assertEquals("left", app.hostedAuthentication?.alignment)
      assertEquals("#dc0000", app.hostedAuthentication?.colorPrimary)
      assertEquals("#000056", app.hostedAuthentication?.colorSecondary)
      assertEquals("string", app.hostedAuthentication?.title)
      assertEquals("string", app.hostedAuthentication?.subtitle)
      assertEquals("#003400", app.hostedAuthentication?.backgroundColor)
      assertEquals(5, app.hostedAuthentication?.spacing)
      assertEquals(1, app.callbackUris?.size)
      assertIs<RedirectUri>(app.callbackUris?.get(0))
      assertEquals("0556d035-6cb6-4262-a035-6b77e11cf8fc", app.callbackUris?.get(0)?.id)
      assertEquals("string", app.callbackUris?.get(0)?.url)
      assertEquals(Platform.WEB, app.callbackUris?.get(0)?.platform)
      assertIs<RedirectUriSettings>(app.callbackUris?.get(0)?.settings)
      assertEquals("string", app.callbackUris?.get(0)?.settings?.origin)
      assertEquals("string", app.callbackUris?.get(0)?.settings?.bundleId)
      assertEquals("string", app.callbackUris?.get(0)?.settings?.packageName)
      assertEquals("string", app.callbackUris?.get(0)?.settings?.sha1CertificateFingerprint)
    }
  }

  @Nested
  inner class GetDetailsTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var mockApplications: Applications

    @BeforeEach
    fun setup() {
      grantId = "grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      mockApplications = Applications(mockNylasClient)
    }

    @Test
    fun `getting application details calls requests with the correct params`() {
      mockApplications.getDetails()
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<Response<ApplicationDetails>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/applications", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, ApplicationDetails::class.java), typeCaptor.firstValue)
    }
  }

  @Nested
  inner class ResourcesTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var mockApplications: Applications

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      mockApplications = Applications(mockNylasClient)
    }

    @Test
    fun `redirectUris returns a RedirectUris instance`() {
      val redirectUris = mockApplications.redirectUris()
      assertIs<RedirectUris>(redirectUris)
    }
  }
}
