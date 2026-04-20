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
import java.io.ByteArrayInputStream
import java.lang.reflect.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class DomainsTests {
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
    fun `SendTransactionalEmailRequest minimal payload serializes correctly`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      ).build()

      val json = adapter.toJson(request)

      // from must be a single object, not an array
      assert(json.contains("\"from\":{")) { "Expected 'from' to be a single object, got: $json" }
      assert(json.contains("\"to\":[")) { "Expected 'to' to be an array, got: $json" }
      assert(!json.contains("null")) { "Expected no null fields in minimal payload, got: $json" }
    }

    @Test
    fun `SendTransactionalEmailRequest full payload round-trip serializes correctly`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
        {
          "to": [{"name": "Jane Doe", "email": "jane.doe@example.com"}],
          "from": {"name": "ACME Support", "email": "support@acme.com"},
          "cc": [{"name": "CC User", "email": "cc@example.com"}],
          "bcc": [{"name": "BCC User", "email": "bcc@example.com"}],
          "reply_to": [{"name": "Reply", "email": "reply@example.com"}],
          "subject": "Welcome to ACME",
          "body": "Welcome! We're here to help.",
          "send_at": 1620000000,
          "reply_to_message_id": "msg-123",
          "tracking_options": {"opens": true, "links": true, "thread_replies": false, "label": "welcome"},
          "use_draft": false,
          "custom_headers": [{"name": "X-Custom", "value": "custom-value"}],
          "is_plaintext": false
        }
        """.trimIndent(),
      )

      val request = adapter.fromJson(jsonBuffer)!!
      assertIs<SendTransactionalEmailRequest>(request)
      assertEquals(1, request.to.size)
      assertEquals("jane.doe@example.com", request.to[0].email)
      assertEquals("support@acme.com", request.from.email)
      assertEquals("ACME Support", request.from.name)
      assertEquals(1, request.cc?.size)
      assertEquals("cc@example.com", request.cc?.get(0)?.email)
      assertEquals(1, request.bcc?.size)
      assertEquals("bcc@example.com", request.bcc?.get(0)?.email)
      assertEquals(1, request.replyTo?.size)
      assertEquals("reply@example.com", request.replyTo?.get(0)?.email)
      assertEquals("Welcome to ACME", request.subject)
      assertEquals("Welcome! We're here to help.", request.body)
      assertEquals(1620000000L, request.sendAt)
      assertEquals("msg-123", request.replyToMessageId)
      assertEquals(true, request.trackingOptions?.opens)
      assertEquals(true, request.trackingOptions?.links)
      assertEquals(false, request.trackingOptions?.threadReplies)
      assertEquals("welcome", request.trackingOptions?.label)
      assertEquals(false, request.useDraft)
      assertEquals(1, request.customHeaders?.size)
      assertEquals("X-Custom", request.customHeaders?.get(0)?.name)
      assertEquals("custom-value", request.customHeaders?.get(0)?.value)
      assertEquals(false, request.isPlaintext)
    }

    @Test
    fun `SendTransactionalEmailRequest isPlaintext true serializes correctly`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      ).isPlaintext(true).build()

      val json = adapter.toJson(request)
      assert(json.contains("\"is_plaintext\":true")) { "Expected is_plaintext:true in JSON, got: $json" }
    }

    @Test
    fun `SendTransactionalEmailRequest null optionals are omitted from JSON`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      ).build()

      val json = adapter.toJson(request)
      assert(!json.contains("\"cc\"")) { "Expected no cc field, got: $json" }
      assert(!json.contains("\"bcc\"")) { "Expected no bcc field, got: $json" }
      assert(!json.contains("\"subject\"")) { "Expected no subject field, got: $json" }
      assert(!json.contains("\"body\"")) { "Expected no body field, got: $json" }
      assert(!json.contains("\"send_at\"")) { "Expected no send_at field, got: $json" }
      assert(!json.contains("\"is_plaintext\"")) { "Expected no is_plaintext field, got: $json" }
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var domainName: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var domains: Domains

    @BeforeEach
    fun setup() {
      domainName = "acme.com"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      domains = Domains(mockNylasClient)
    }

    @Test
    fun `sending a transactional email calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      )
        .subject("Welcome to ACME")
        .body("Welcome! We're here to help.")
        .build()

      domains.sendTransactionalEmail(domainName, request)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/domains/$domainName/messages/send", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Message::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(request), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `sending a transactional email with overrides passes them through`() {
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      ).build()
      val overrides = RequestOverrides(apiKey = "override-key")

      domains.sendTransactionalEmail(domainName, request, overrides)

      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Message>>(
        any(),
        any(),
        any(),
        anyOrNull(),
        overrideParamCaptor.capture(),
      )
      assertEquals("override-key", overrideParamCaptor.firstValue.apiKey)
    }

    @Test
    fun `sending a transactional email with a large attachment calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val testInputStream = ByteArrayInputStream("test data".toByteArray())
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      )
        .subject("Welcome to ACME")
        .attachments(
          listOf(
            CreateAttachmentRequest(
              content = testInputStream,
              contentType = "text/plain",
              filename = "attachment.txt",
              size = 3 * 1024 * 1024,
            ),
          ),
        )
        .build()
      val attachmentLessRequest = request.copy(attachments = null)

      domains.sendTransactionalEmail(domainName, request)

      val pathCaptor = argumentCaptor<String>()
      val methodCaptor = argumentCaptor<NylasClient.HttpMethod>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<RequestBody>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeFormRequest<Response<Message>>(
        pathCaptor.capture(),
        methodCaptor.capture(),
        requestBodyCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/domains/$domainName/messages/send", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Message::class.java), typeCaptor.firstValue)
      assertEquals(NylasClient.HttpMethod.POST, methodCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
      val multipart = requestBodyCaptor.firstValue as MultipartBody
      assertEquals(2, multipart.size)
      val buffer = Buffer()
      val fileBuffer = Buffer()
      multipart.part(0).body.writeTo(buffer)
      multipart.part(1).body.writeTo(fileBuffer)
      assertEquals(adapter.toJson(attachmentLessRequest), buffer.readUtf8())
      assertEquals("test data", fileBuffer.readUtf8())
    }
  }
}
