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

class AttachmentsTests {
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
    fun `Attachment serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Attachment::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "content_type": "image/png",
            "filename": "pic.png",
            "grant_id": "41009df5-bf11-4c97-aa18-b285b5f2e386",
            "id": "185e56cb50e12e82",
            "is_inline": true,
            "size": 13068,
            "content_id": "<ce9b9547-9eeb-43b2-ac4e-58768bdf04e4>"
          }
        """.trimIndent(),
      )

      val attachment = adapter.fromJson(jsonBuffer)!!
      assertEquals("image/png", attachment.contentType)
      assertEquals("pic.png", attachment.filename)
      assertEquals("41009df5-bf11-4c97-aa18-b285b5f2e386", attachment.grantId)
      assertEquals("185e56cb50e12e82", attachment.id)
      assertEquals(true, attachment.isInline)
      assertEquals(13068, attachment.size)
      assertEquals("<ce9b9547-9eeb-43b2-ac4e-58768bdf04e4>", attachment.contentId)
    }
  }

  @Nested
  inner class RequestTests {
    private lateinit var grantId: String
    private lateinit var attachmentId: String
    private lateinit var queryParams: FindAttachmentQueryParams
    private lateinit var mockNylasClient: NylasClient
    private lateinit var attachments: Attachments

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      queryParams = FindAttachmentQueryParams(
        messageId = "message-id",
      )
      attachmentId = "attachment-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      attachments = Attachments(mockNylasClient)
    }

    @Test
    fun `find makes a GET request to the correct path`() {
      attachments.find(grantId, attachmentId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<FindAttachmentQueryParams>()
      verify(mockNylasClient).executeGet<Response<Attachment>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/attachments/$attachmentId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Attachment::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `download makes a GET request to the correct path`() {
      attachments.download(grantId, attachmentId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).downloadResponse(
        pathCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/attachments/$attachmentId/download", pathCaptor.firstValue)
    }

    @Test
    fun `downloadBytes makes a GET request to the correct path`() {
      val byteArray = byteArrayOf(0b00000100, 0b00000010, 0b00000011)
      whenever(mockResponseBody.bytes()).thenReturn(byteArray)
      whenever(mockNylasClient.downloadResponse(any(), any())).thenReturn(mockResponseBody)

      val bytes = attachments.downloadBytes(grantId, attachmentId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).downloadResponse(
        pathCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/attachments/$attachmentId/download", pathCaptor.firstValue)
      assertEquals(byteArray, bytes)
    }
  }
}
