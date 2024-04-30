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
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.ByteArrayInputStream
import java.lang.reflect.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class DraftsTests {
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
    fun `Draft serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Draft::class.java)
      val jsonBuffer =
        Buffer().writeUtf8(
          """
            {
              "body": "Hello, I just sent a message using Nylas!",
              "cc": [
                {
                  "email": "arya.stark@example.com"
                }
              ],
              "attachments": [
                {
                  "content_type": "text/calendar",
                  "id": "4kj2jrcoj9ve5j9yxqz5cuv98",
                  "size": 1708
                }
              ],
              "folders": [
                "8l6c4d11y1p4dm4fxj52whyr9",
                "d9zkcr2tljpu3m4qpj7l2hbr0"
              ],
              "from": [
                {
                  "name": "Daenerys Targaryen",
                  "email": "daenerys.t@example.com"
                }
              ],
              "grant_id": "41009df5-bf11-4c97-aa18-b285b5f2e386",
              "id": "5d3qmne77v32r8l4phyuksl2x",
              "object": "draft",
              "reply_to": [
                {
                  "name": "Daenerys Targaryen",
                  "email": "daenerys.t@example.com"
                }
              ],
              "snippet": "Hello, I just sent a message using Nylas!",
              "starred": true,
              "subject": "Hello from Nylas!",
              "thread_id": "1t8tv3890q4vgmwq6pmdwm8qgsaer",
              "to": [
                {
                  "name": "Jon Snow",
                  "email": "j.snow@example.com"
                }
              ],
              "date": 1705084742,
              "created_at": 1705084926
            }
          }
          """.trimIndent(),
        )

      val draft = adapter.fromJson(jsonBuffer)!!
      assertIs<Draft>(draft)
      assertEquals("Hello, I just sent a message using Nylas!", draft.body)
      assertEquals(1, draft.cc?.size)
      assertEquals("arya.stark@example.com", draft.cc?.first()?.email)
      assertEquals(1, draft.attachments?.size)
      assertEquals("text/calendar", draft.attachments?.first()?.contentType)
      assertEquals("4kj2jrcoj9ve5j9yxqz5cuv98", draft.attachments?.first()?.id)
      assertEquals(1708, draft.attachments?.first()?.size)
      assertEquals(2, draft.folders?.size)
      assertEquals("8l6c4d11y1p4dm4fxj52whyr9", draft.folders?.first())
      assertEquals("d9zkcr2tljpu3m4qpj7l2hbr0", draft.folders?.last())
      assertEquals(1, draft.from?.size)
      assertEquals("Daenerys Targaryen", draft.from?.first()?.name)
      assertEquals("daenerys.t@example.com", draft.from?.first()?.email)
      assertEquals("41009df5-bf11-4c97-aa18-b285b5f2e386", draft.grantId)
      assertEquals("5d3qmne77v32r8l4phyuksl2x", draft.id)
      assertEquals("draft", draft.getObject())
      assertEquals(1, draft.replyTo?.size)
      assertEquals("Daenerys Targaryen", draft.replyTo?.first()?.name)
      assertEquals("daenerys.t@example.com", draft.replyTo?.first()?.email)
      assertEquals("Hello, I just sent a message using Nylas!", draft.snippet)
      assertEquals(true, draft.starred)
      assertEquals("Hello from Nylas!", draft.subject)
      assertEquals("1t8tv3890q4vgmwq6pmdwm8qgsaer", draft.threadId)
      assertEquals(1, draft.to?.size)
      assertEquals("Jon Snow", draft.to?.first()?.name)
      assertEquals("j.snow@example.com", draft.to?.first()?.email)
      assertEquals(1705084742, draft.date)
      assertEquals(1705084926, draft.createdAt)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var drafts: Drafts

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      drafts = Drafts(mockNylasClient)
    }

    @Test
    fun `listing drafts calls requests with the correct params`() {
      val queryParams =
        ListDraftsQueryParams(
          limit = 10,
          threadId = "thread-123",
          subject = "subject",
          to = listOf("to"),
          cc = listOf("cc"),
          bcc = listOf("bcc"),
          unread = true,
          starred = true,
          hasAttachment = true,
        )

      drafts.list(grantId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListDraftsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Draft::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `listing drafts without query params calls requests with the correct params`() {
      drafts.list(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListDraftsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Draft::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a draft calls requests with the correct params`() {
      val draftId = "draft-123"

      drafts.find(grantId, draftId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts/$draftId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Draft::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a draft calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateDraftRequest::class.java)
      val createDraftRequest =
        CreateDraftRequest(
          body = "Hello, I just sent a message using Nylas!",
          cc = listOf(EmailName(email = "test@gmail.com", name = "Test")),
          bcc = listOf(EmailName(email = "bcc@gmail.com", name = "BCC")),
          subject = "Hello from Nylas!",
          starred = true,
          sendAt = 1620000000,
          replyToMessageId = "reply-to-message-id",
          trackingOptions = TrackingOptions(label = "label", links = true, opens = true, threadReplies = true),
          customHeaders = listOf(CustomHeader(name = "header", value = "value")),
        )

      drafts.create(grantId, createDraftRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Draft::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createDraftRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a draft with small attachment calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateDraftRequest::class.java)
      val testInputStream = ByteArrayInputStream("test data".toByteArray())
      val createDraftRequest =
        CreateDraftRequest(
          body = "Hello, I just sent a message using Nylas!",
          cc = listOf(EmailName(email = "test@gmail.com", name = "Test")),
          bcc = listOf(EmailName(email = "bcc@gmail.com", name = "BCC")),
          subject = "Hello from Nylas!",
          starred = true,
          sendAt = 1620000000,
          replyToMessageId = "reply-to-message-id",
          trackingOptions = TrackingOptions(label = "label", links = true, opens = true, threadReplies = true),
          attachments = listOf(
            CreateAttachmentRequest(
              content = testInputStream,
              contentType = "text/plain",
              filename = "attachment.txt",
              size = 100,
            ),
          ),
          customHeaders = listOf(CustomHeader(name = "header", value = "value")),
        )

      drafts.create(grantId, createDraftRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Draft::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createDraftRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a draft with large attachment calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateDraftRequest::class.java)
      val testInputStream = ByteArrayInputStream("test data".toByteArray())
      val createDraftRequest =
        CreateDraftRequest(
          body = "Hello, I just sent a message using Nylas!",
          cc = listOf(EmailName(email = "test@gmail.com", name = "Test")),
          bcc = listOf(EmailName(email = "bcc@gmail.com", name = "BCC")),
          subject = "Hello from Nylas!",
          starred = true,
          sendAt = 1620000000,
          replyToMessageId = "reply-to-message-id",
          trackingOptions = TrackingOptions(label = "label", links = true, opens = true, threadReplies = true),
          attachments = listOf(
            CreateAttachmentRequest(
              content = testInputStream,
              contentType = "text/plain",
              filename = "attachment.txt",
              size = 3 * 1024 * 1024,
            ),
          ),
          customHeaders = listOf(CustomHeader(name = "header", value = "value")),
        )

      drafts.create(grantId, createDraftRequest)

      val pathCaptor = argumentCaptor<String>()
      val methodCaptor = argumentCaptor<NylasClient.HttpMethod>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<RequestBody>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeFormRequest<Response<Draft>>(
        pathCaptor.capture(),
        methodCaptor.capture(),
        requestBodyCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Draft::class.java), typeCaptor.firstValue)
      assertEquals(NylasClient.HttpMethod.POST, methodCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
      val multipart = requestBodyCaptor.firstValue as MultipartBody
      assertEquals(2, multipart.size())
      val buffer = Buffer()
      val fileBuffer = Buffer()
      multipart.part(0).body().writeTo(buffer)
      multipart.part(1).body().writeTo(fileBuffer)
      assertEquals(adapter.toJson(createDraftRequest), buffer.readUtf8())
      assertEquals("test data", fileBuffer.readUtf8())
    }

    @Test
    fun `updating a draft calls requests with the correct params`() {
      val draftId = "draft-123"
      val adapter = JsonHelper.moshi().adapter(UpdateDraftRequest::class.java)
      val updateDraftRequest =
        UpdateDraftRequest(
          body = "Hello, I just sent a message using Nylas!",
          subject = "Hello from Nylas!",
          unread = false,
          starred = true,
        )

      drafts.update(grantId, draftId, updateDraftRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts/$draftId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Draft::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateDraftRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `updating a draft with small attachments calls requests with the correct params`() {
      val draftId = "draft-123"
      val adapter = JsonHelper.moshi().adapter(UpdateDraftRequest::class.java)
      val testInputStream = ByteArrayInputStream("test data".toByteArray())
      val updateDraftRequest =
        UpdateDraftRequest(
          body = "Hello, I just sent a message using Nylas!",
          subject = "Hello from Nylas!",
          unread = false,
          starred = true,
          attachments = listOf(
            CreateAttachmentRequest(
              content = testInputStream,
              contentType = "text/plain",
              filename = "attachment.txt",
              size = 100,
            ),
          ),
        )

      drafts.update(grantId, draftId, updateDraftRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts/$draftId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Draft::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateDraftRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `updating a draft with large attachments calls requests with the correct params`() {
      val draftId = "draft-123"
      val adapter = JsonHelper.moshi().adapter(UpdateDraftRequest::class.java)
      val testInputStream = ByteArrayInputStream("test data".toByteArray())
      val updateDraftRequest =
        UpdateDraftRequest(
          body = "Hello, I just sent a message using Nylas!",
          subject = "Hello from Nylas!",
          unread = false,
          starred = true,
          attachments = listOf(
            CreateAttachmentRequest(
              content = testInputStream,
              contentType = "text/plain",
              filename = "attachment.txt",
              size = 3 * 1024 * 1024,
            ),
          ),
        )

      drafts.update(grantId, draftId, updateDraftRequest)

      val pathCaptor = argumentCaptor<String>()
      val methodCaptor = argumentCaptor<NylasClient.HttpMethod>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<RequestBody>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeFormRequest<Response<Draft>>(
        pathCaptor.capture(),
        methodCaptor.capture(),
        requestBodyCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts/$draftId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Draft::class.java), typeCaptor.firstValue)
      assertEquals(NylasClient.HttpMethod.PUT, methodCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
      val multipart = requestBodyCaptor.firstValue as MultipartBody
      assertEquals(2, multipart.size())
      val buffer = Buffer()
      val fileBuffer = Buffer()
      multipart.part(0).body().writeTo(buffer)
      multipart.part(1).body().writeTo(fileBuffer)
      assertEquals(adapter.toJson(updateDraftRequest), buffer.readUtf8())
      assertEquals("test data", fileBuffer.readUtf8())
    }

    @Test
    fun `destroying a draft calls requests with the correct params`() {
      val draftId = "draft-123"

      drafts.destroy(grantId, draftId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<ListResponse<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts/$draftId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }

  @Nested
  inner class OtherMethodTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var drafts: Drafts

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      drafts = Drafts(mockNylasClient)
    }

    @Test
    fun `send calls requests with the correct params`() {
      val grantId = "abc-123-grant-id"
      val draftId = "draft-123"

      drafts.send(grantId, draftId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<ListResponse<Draft>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/drafts/$draftId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Message::class.java), typeCaptor.firstValue)
      assertNull(requestBodyCaptor.firstValue)
    }
  }
}
