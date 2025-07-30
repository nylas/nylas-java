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

class ConnectorsTests {
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
    fun `Connector serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Connector::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "provider": "google",
            "settings": {
              "topic_name": "abc123"
            },
            "scope": [
              "https://www.googleapis.com/auth/userinfo.email",
              "https://www.googleapis.com/auth/userinfo.profile"
            ]
          }
        """.trimIndent(),
      )

      val connector = adapter.fromJson(jsonBuffer)!!
      assertIs<Connector.Google>(connector)
      assertEquals(AuthProvider.GOOGLE, connector.provider)
      assertEquals("abc123", connector.settings.topicName)
      assertEquals(
        listOf(
          "https://www.googleapis.com/auth/userinfo.email",
          "https://www.googleapis.com/auth/userinfo.profile",
        ),
        connector.scope,
      )
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var connectors: Connectors

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      connectors = Connectors(mockNylasClient)
    }

    @Test
    fun `listing connectors calls requests with the correct params`() {
      connectors.list()
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Connector>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Connector::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `finding a connector calls requests with the correct params`() {
      connectors.find(AuthProvider.GOOGLE)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Connector>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Connector::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a connector calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateConnectorRequest::class.java)
      val createConnectorRequest = CreateConnectorRequest.Google(
        settings = GoogleCreateConnectorSettings(
          clientId = "CLIENT_ID",
          clientSecret = "CLIENT_SECRET",
          topicName = "abc123",
        ),
        scope = listOf(
          "https://www.googleapis.com/auth/userinfo.email",
          "https://www.googleapis.com/auth/userinfo.profile",
        ),
      )

      connectors.create(createConnectorRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Connector>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Connector::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createConnectorRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a connector calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateConnectorRequest::class.java)
      val updateConnectorRequest = UpdateConnectorRequest.Google(
        settings = GoogleConnectorSettings(
          topicName = "abc123",
        ),
        scope = listOf(
          "https://www.googleapis.com/auth/userinfo.email",
          "https://www.googleapis.com/auth/userinfo.profile",
        ),
      )

      connectors.update(AuthProvider.GOOGLE, updateConnectorRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePatch<Response<Connector>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Connector::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateConnectorRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a connector calls requests with the correct params`() {
      connectors.destroy(AuthProvider.IMAP)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/imap", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }

  @Nested
  inner class ResourcesTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var connectors: Connectors

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      connectors = Connectors(mockNylasClient)
    }

    @Test
    fun `credentials returns a Credentials instance`() {
      val credentials = connectors.credentials()
      assertIs<Credentials>(credentials)
    }
  }
}
