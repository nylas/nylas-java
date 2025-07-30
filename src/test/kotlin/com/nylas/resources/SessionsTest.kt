package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.models.Response
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.*
import okio.Buffer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.lang.reflect.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SessionsTest {
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
    fun `Session serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Session::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
        {
          "session_id": "session-id"
        }
        """.trimIndent(),
      )
      val session = adapter.fromJson(jsonBuffer)!!
      assertIs<Session>(session)
      assertEquals("session-id", session.sessionId)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var sessions: Sessions

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      sessions = Sessions(mockNylasClient)
    }

    @Test
    fun `creating a session calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateSessionRequest::class.java)
      val createSessionRequest = CreateSessionRequest.Builder()
        .configurationId("config-id")
        .timeToLive(30)
        .build()

      sessions.create(createSessionRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Session>(pathCaptor.capture(), typeCaptor.capture(), requestBodyCaptor.capture(), queryParamCaptor.capture(), overrideParamCaptor.capture())

      assertEquals("v3/scheduling/sessions", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Session::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createSessionRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `deleting a session calls requests with the correct params`() {
      val sessionId = "session-id"
      sessions.destroy(sessionId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(pathCaptor.capture(), typeCaptor.capture(), queryParamCaptor.capture(), overrideParamCaptor.capture())
      assertEquals("v3/scheduling/sessions/$sessionId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }
}
