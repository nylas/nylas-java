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

class SmartComposeTests {
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
    fun `SmartCompose serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(ComposeMessageResponse::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "suggestion": "Hello world"
          }
        """.trimIndent(),
      )

      val smartCompose = adapter.fromJson(jsonBuffer)!!
      assertIs<ComposeMessageResponse>(smartCompose)
      assertEquals("Hello world", smartCompose.suggestion)
    }
  }

  @Nested
  inner class ComposeMessageTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var smartCompose: SmartCompose

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      smartCompose = SmartCompose(mockNylasClient)
    }

    @Test
    fun `composing a message calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(ComposeMessageRequest::class.java)
      val composeMessageRequest = ComposeMessageRequest(
        prompt = "Hello world",
      )

      smartCompose.composeMessage(grantId, composeMessageRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<CreateEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/smart-compose", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, ComposeMessageResponse::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(composeMessageRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `composing a reply calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(ComposeMessageRequest::class.java)
      val composeMessageRequest = ComposeMessageRequest(
        prompt = "Hello world",
      )
      val messageId = "abc-123-message-id"

      smartCompose.composeMessageReply(grantId, messageId, composeMessageRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<CreateEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/$messageId/smart-compose", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, ComposeMessageResponse::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(composeMessageRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }
}
