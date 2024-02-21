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
import kotlin.test.assertNull

class GrantsTests {
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
    fun `Grant serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Grant::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "e19f8e1a-eb1c-41c0-b6a6-d2e59daf7f47",
            "provider": "google",
            "grant_status": "valid",
            "email": "email@example.com",
            "scope": [
              "Mail.Read",
              "User.Read",
              "offline_access"
            ],
            "user_agent": "string",
            "ip": "string",
            "state": "my-state",
            "created_at": 1617817109,
            "updated_at": 1617817109
          }
        """.trimIndent(),
      )

      val grant = adapter.fromJson(jsonBuffer)!!
      assertIs<Grant>(grant)
      assertEquals("e19f8e1a-eb1c-41c0-b6a6-d2e59daf7f47", grant.id)
      assertEquals(AuthProvider.GOOGLE, grant.provider)
      assertEquals(GrantStatus.VALID, grant.grantStatus)
      assertEquals("email@example.com", grant.email)
      assertEquals(listOf("Mail.Read", "User.Read", "offline_access"), grant.scope)
      assertEquals("string", grant.userAgent)
      assertEquals("string", grant.ip)
      assertEquals("my-state", grant.state)
      assertEquals(1617817109, grant.createdAt)
      assertEquals(1617817109, grant.updatedAt)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var grants: Grants

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      grants = Grants(mockNylasClient)
    }

    @Test
    fun `listing grants calls requests with the correct params`() {
      grants.list()

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Grant>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Grant::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `listing grants with queryParams calls requests with the correct params`() {
      val queryParams = ListGrantsQueryParams(
        limit = 10,
        offset = 10,
      )

      grants.list(queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Grant>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Grant::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a grant calls requests with the correct params`() {
      val grantId = "abc123"

      grants.find(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<Response<Grant>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Grant::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `updating a grant calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateGrantRequest::class.java)
      val updateGrantRequest = UpdateGrantRequest(
        settings = mapOf(
          "key" to "value",
        ),
        scope = listOf(
          "Mail.Read",
          "User.Read",
          "offline_access",
        ),
      )
      val grantId = "abc123"

      grants.update(grantId, updateGrantRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executePatch<Response<Grant>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Grant::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateGrantRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `destroying a grant calls requests with the correct params`() {
      val grantId = "abc123"

      grants.destroy(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }
}
