package com.nylas

import com.nylas.models.NylasApiError
import com.nylas.models.NylasOAuthError
import com.nylas.models.NylasSdkTimeoutError
import com.nylas.util.JsonHelper
import okhttp3.*
import okio.Buffer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.net.SocketTimeoutException
import kotlin.test.assertFailsWith

class NylasClientTest {

  private lateinit var nylasClient: NylasClient

  @BeforeEach
  fun setup() {
    val httpClientBuilder = OkHttpClient.Builder()
    nylasClient = NylasClient("testApiKey", httpClientBuilder)
  }

  @Nested
  inner class InitializationTests {
    @Test
    fun `builder uses the default url`() {
      val client = NylasClient.Builder("testApiKey")
        .build()

      val urlBuilder = client.newUrlBuilder()
      val builtUrl = urlBuilder.build().toString()

      assert(builtUrl.startsWith("https://api.us.nylas.com/"))
    }

    @Test
    fun `builder sets correct apiUri`() {
      val customApiUri = "https://custom-api.nylas.com/"
      val client = NylasClient.Builder("testApiKey")
        .apiUri(customApiUri)
        .build()

      val urlBuilder = client.newUrlBuilder()
      val builtUrl = urlBuilder.build().toString()

      assert(builtUrl.startsWith(customApiUri))
    }

    @Test
    fun `builder sets correct httpClient`() {
      val mockOkHttpClientBuilder: OkHttpClient.Builder = mock()
      val mockOkHttpClient: OkHttpClient = mock()
      whenever(mockOkHttpClientBuilder.addInterceptor(any())).thenReturn(mockOkHttpClientBuilder)
      whenever(mockOkHttpClientBuilder.build()).thenReturn(mockOkHttpClient)

      val client = NylasClient.Builder("testApiKey")
        .httpClient(mockOkHttpClientBuilder)
        .build()

      val field = NylasClient::class.java.getDeclaredField("httpClient")
      field.isAccessible = true

      assertEquals(mockOkHttpClient, field.get(client))
    }

    @Test
    fun `initializing the NylasClient with no optional fields should use correct defaults`() {
      val client = NylasClient(apiKey = "testApiKey")

      val apiUriField = NylasClient::class.java.getDeclaredField("apiUri")
      apiUriField.isAccessible = true
      val apiUriSet = apiUriField.get(client) as HttpUrl

      assertEquals("https://api.us.nylas.com/", apiUriSet.toString())
      assertEquals("testApiKey", client.apiKey)
    }

    @Test
    fun `initializing the NylasClient with values sets them correctly`() {
      val mockOkHttpClientBuilder: OkHttpClient.Builder = mock()
      val mockOkHttpClient: OkHttpClient = mock()
      whenever(mockOkHttpClientBuilder.addInterceptor(any())).thenReturn(mockOkHttpClientBuilder)
      whenever(mockOkHttpClientBuilder.build()).thenReturn(mockOkHttpClient)

      val client = NylasClient(apiKey = "testApiKey", httpClientBuilder = mockOkHttpClientBuilder, apiUri = "https://custom-api.nylas.com/")

      val httpClientField = NylasClient::class.java.getDeclaredField("httpClient")
      httpClientField.isAccessible = true
      val apiUriField = NylasClient::class.java.getDeclaredField("apiUri")
      apiUriField.isAccessible = true
      val apiUriSet = apiUriField.get(client) as HttpUrl

      assertEquals(mockOkHttpClient, httpClientField.get(client))
      assertEquals("https://custom-api.nylas.com/", apiUriSet.toString())
      assertEquals("testApiKey", client.apiKey)
    }
  }

  @Nested
  inner class ResourcesTests {
    @Test
    fun `applications returns a valid Applications instance`() {
      val result = nylasClient.applications()
      assertNotNull(result)
    }

    @Test
    fun `attachments returns a valid Applications instance`() {
      val result = nylasClient.attachments()
      assertNotNull(result)
    }

    @Test
    fun `auth returns a valid Auth instance`() {
      val result = nylasClient.auth()
      assertNotNull(result)
    }

    @Test
    fun `calendars returns a valid Calendars instance`() {
      val result = nylasClient.calendars()
      assertNotNull(result)
    }

    @Test
    fun `connectors returns a valid Calendars instance`() {
      val result = nylasClient.connectors()
      assertNotNull(result)
    }

    @Test
    fun `drafts returns a valid Calendars instance`() {
      val result = nylasClient.drafts()
      assertNotNull(result)
    }

    @Test
    fun `events returns a valid Events instance`() {
      val result = nylasClient.events()
      assertNotNull(result)
    }

    @Test
    fun `folders returns a valid Events instance`() {
      val result = nylasClient.folders()
      assertNotNull(result)
    }

    @Test
    fun `messages returns a valid Events instance`() {
      val result = nylasClient.messages()
      assertNotNull(result)
    }

    @Test
    fun `threads returns a valid Events instance`() {
      val result = nylasClient.threads()
      assertNotNull(result)
    }

    @Test
    fun `webhooks returns a valid Webhooks instance`() {
      val result = nylasClient.webhooks()
      assertNotNull(result)
    }
  }

  @Nested
  inner class RequestTests {
    private val mockHttpClient: OkHttpClient = mock(OkHttpClient::class.java)
    private val mockCall: Call = mock(Call::class.java)
    private val mockResponse: Response = mock(Response::class.java)
    private val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)

    @BeforeEach
    fun setup() {
      val mockOkHttpClientBuilder: OkHttpClient.Builder = mock()
      whenever(mockOkHttpClientBuilder.addInterceptor(any())).thenReturn(mockOkHttpClientBuilder)
      whenever(mockOkHttpClientBuilder.build()).thenReturn(mockHttpClient)
      whenever(mockHttpClient.newCall(any())).thenReturn(mockCall)
      nylasClient = NylasClient("testApiKey", mockOkHttpClientBuilder)
    }

    @Test
    fun `should handle successful request`() {
      val urlBuilder = nylasClient.newUrlBuilder()
      whenever(mockCall.execute()).thenReturn(mockResponse)
      whenever(mockResponse.isSuccessful).thenReturn(true)
      whenever(mockResponse.body()).thenReturn(mockResponseBody)
      whenever(mockResponseBody.source()).thenReturn(Buffer().writeUtf8("{ \"foo\": \"bar\" }"))

      val result = nylasClient.executeRequest<Map<String, String>>(urlBuilder, NylasClient.HttpMethod.GET, null, JsonHelper.mapTypeOf(String::class.java, String::class.java))

      assertEquals("bar", result["foo"])
    }

    @Test
    fun `should throw NylasOAuthError on error from oauth endpoints`() {
      val tokenUrlBuilder = nylasClient.newUrlBuilder().addPathSegments("v3/connect/token")
      val revokeUrlBuilder = nylasClient.newUrlBuilder().addPathSegments("v3/connect/revoke")
      val oauthUrls = listOf(tokenUrlBuilder, revokeUrlBuilder)
      whenever(mockCall.execute()).thenReturn(mockResponse)
      whenever(mockResponse.isSuccessful).thenReturn(false)
      whenever(mockResponse.code()).thenReturn(401)
      whenever(mockResponse.body()).thenReturn(mockResponseBody)
      whenever(mockResponseBody.string()).thenReturn("{ \"error\": \"internal_error\", \"error_code\": 500, \"error_description\": \"Internal error, contact administrator\", \"error_uri\": \"https://accounts.nylas.io/#tag/Event-Codes\", \"request_id\": \"eccc9c3f-7150-48e1-965e-4f89714ab51a\" }")

      for (urlBuilder in oauthUrls) {
        val exception = assertFailsWith<NylasOAuthError> {
          nylasClient.executeRequest(urlBuilder, NylasClient.HttpMethod.GET, null, String::class.java)
        }

        assertEquals("internal_error", exception.error)
        assertEquals("Internal error, contact administrator", exception.errorDescription)
        assertEquals("https://accounts.nylas.io/#tag/Event-Codes", exception.errorUri)
        assertEquals("500", exception.errorCode)
        assertEquals(401, exception.statusCode)
      }
    }

    @Test
    fun `should throw NylasOAuthError on error from oauth endpoints even if unexpected response from API`() {
      val tokenUrlBuilder = nylasClient.newUrlBuilder().addPathSegments("v3/connect/token")
      val revokeUrlBuilder = nylasClient.newUrlBuilder().addPathSegments("v3/connect/revoke")
      val oauthUrls = listOf(tokenUrlBuilder, revokeUrlBuilder)
      whenever(mockCall.execute()).thenReturn(mockResponse)
      whenever(mockResponse.isSuccessful).thenReturn(false)
      whenever(mockResponse.code()).thenReturn(500)
      whenever(mockResponse.body()).thenReturn(mockResponseBody)
      whenever(mockResponseBody.string()).thenReturn("not a json")

      for (urlBuilder in oauthUrls) {
        val exception = assertFailsWith<NylasOAuthError> {
          nylasClient.executeRequest(urlBuilder, NylasClient.HttpMethod.GET, null, String::class.java)
        }

        assertEquals("unknown", exception.error)
        assertEquals("Unknown error received from the API: not a json", exception.errorDescription)
        assertEquals("unknown", exception.errorUri)
        assertEquals("0", exception.errorCode)
        assertEquals(500, exception.statusCode)
      }
    }

    @Test
    fun `should throw NylasApiError on error from non-oauth endpoints`() {
      val urlBuilder = nylasClient.newUrlBuilder().addPathSegments("v3/some/other/path")
      whenever(mockCall.execute()).thenReturn(mockResponse)
      whenever(mockResponse.isSuccessful).thenReturn(false)
      whenever(mockResponse.code()).thenReturn(400)
      whenever(mockResponse.body()).thenReturn(mockResponseBody)
      whenever(mockResponseBody.string()).thenReturn("{ \"request_id\": \"4c2740b4-52a4-412e-bdee-49a6c6671b22\", \"error\": { \"type\": \"provider_error\", \"message\": \"Unauthorized\", \"provider_error\": { \"error\": \"Request had invalid authentication credentials.\" } } }")

      val exception = assertFailsWith<NylasApiError> {
        nylasClient.executeRequest(urlBuilder, NylasClient.HttpMethod.GET, null, String::class.java)
      }

      assertEquals("provider_error", exception.type)
      assertEquals("Unauthorized", exception.message)
      assertEquals("Request had invalid authentication credentials.", exception.providerError?.get("error"))
      assertEquals("4c2740b4-52a4-412e-bdee-49a6c6671b22", exception.requestId)
      assertEquals(400, exception.statusCode)
    }

    @Test
    fun `should throw NylasApiError on error from non-oauth endpoints even if unexpected response from API`() {
      val urlBuilder = nylasClient.newUrlBuilder().addPathSegments("v3/some/other/path")
      whenever(mockCall.execute()).thenReturn(mockResponse)
      whenever(mockResponse.isSuccessful).thenReturn(false)
      whenever(mockResponse.code()).thenReturn(400)
      whenever(mockResponse.body()).thenReturn(mockResponseBody)
      whenever(mockResponseBody.string()).thenReturn("not a json")

      val exception = assertFailsWith<NylasApiError> {
        nylasClient.executeRequest(urlBuilder, NylasClient.HttpMethod.GET, null, String::class.java)
      }

      assertEquals("unknown", exception.type)
      assertEquals("Unknown error received from the API: not a json", exception.message)
      assertEquals(400, exception.statusCode)
    }

    @Test
    fun `should handle socket timeout exception`() {
      val urlBuilder = nylasClient.newUrlBuilder()
      whenever(mockCall.execute()).thenThrow(SocketTimeoutException())

      val exception = assertFailsWith<NylasSdkTimeoutError> {
        nylasClient.executeRequest(urlBuilder, NylasClient.HttpMethod.GET, null, String::class.java)
      }

      assertNotNull(exception)
    }

    @Test
    fun `should handle unexpected null response body`() {
      val urlBuilder = nylasClient.newUrlBuilder()
      whenever(mockCall.execute()).thenReturn(mockResponse)
      whenever(mockResponse.isSuccessful).thenReturn(true)
      whenever(mockResponse.body()).thenReturn(null)

      val exception = assertFailsWith<Exception> {
        nylasClient.executeRequest(urlBuilder, NylasClient.HttpMethod.GET, null, String::class.java)
      }

      assertEquals("Unexpected null response body", exception.message)
    }

    // TODO::Should we handle this case?
    /*@Test
    fun `should handle failed deserialization of response body`() {
      val urlBuilder = nylasClient.newUrlBuilder()
      whenever(mockCall.execute()).thenReturn(mockResponse)
      whenever(mockResponse.isSuccessful).thenReturn(true)
      whenever(mockResponse.body()).thenReturn(mockResponseBody)
      whenever(mockResponseBody.source()).thenReturn(Buffer().writeUtf8("invalid json"))

      val exception = assertFailsWith<Exception> {
        nylasClient.executeRequest(urlBuilder, NylasClient.HttpMethod.GET, null, String::class.java)
      }

      assertEquals("Failed to deserialize response body", exception.message)
    }*/

    @Test
    fun `should handle download request`() {
      whenever(mockCall.execute()).thenReturn(mockResponse)
      whenever(mockResponse.isSuccessful).thenReturn(true)
      whenever(mockResponse.body()).thenReturn(mockResponseBody)

      val result = nylasClient.downloadResponse("test/path")

      assertEquals(mockResponseBody, result)
    }
  }

  @Nested
  inner class EnumTests {
    @Test
    fun `HttpMethod should have correct number of values`() {
      val expectedCount = 5
      assertEquals(expectedCount, NylasClient.HttpMethod.values().size)
    }

    @Test
    fun `HttpMethod should have correct values`() {
      val expectedValues = listOf("GET", "PUT", "POST", "DELETE", "PATCH")
      val actualValues = NylasClient.HttpMethod.values().map { it.name }
      assertEquals(expectedValues, actualValues)
    }

    @Test
    fun `MediaType values should be correctly associated with their strings`() {
      assertEquals("application/json", NylasClient.MediaType.APPLICATION_JSON.mediaType)
      assertEquals("message/rfc822", NylasClient.MediaType.MESSAGE_RFC822.mediaType)
    }

    @Test
    fun `HttpHeaders values should be correctly associated with their strings`() {
      assertEquals("Accept", NylasClient.HttpHeaders.ACCEPT.headerName)
      assertEquals("Authorization", NylasClient.HttpHeaders.AUTHORIZATION.headerName)
      assertEquals("Content-Type", NylasClient.HttpHeaders.CONTENT_TYPE.headerName)
    }
  }
}
