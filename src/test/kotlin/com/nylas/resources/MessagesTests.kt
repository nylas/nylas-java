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
import kotlin.test.assertNull

class MessagesTests {
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
    fun `Message serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Message::class.java)
      val jsonBuffer =
        Buffer().writeUtf8(
          """
          {
            "body": "Hello, I just sent a message using Nylas!",
            "cc": [
              {
                "name": "Arya Stark",
                "email": "arya.stark@example.com"
              }
            ],
            "date": 1635355739,
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
            "object": "message",
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
            "unread": true
          }
          """.trimIndent(),
        )

      val message = adapter.fromJson(jsonBuffer)!!
      assertIs<Message>(message)
      assertEquals("Hello, I just sent a message using Nylas!", message.body)
      assertEquals(1635355739, message.date)
      assertEquals(1, message.cc?.size)
      assertEquals("Arya Stark", message.cc?.get(0)?.name)
      assertEquals("arya.stark@example.com", message.cc?.get(0)?.email)
      assertEquals(1, message.attachments?.size)
      assertEquals("text/calendar", message.attachments?.get(0)?.contentType)
      assertEquals("4kj2jrcoj9ve5j9yxqz5cuv98", message.attachments?.get(0)?.id)
      assertEquals(1708, message.attachments?.get(0)?.size)
      assertEquals(2, message.folders?.size)
      assertEquals("8l6c4d11y1p4dm4fxj52whyr9", message.folders?.get(0))
      assertEquals("d9zkcr2tljpu3m4qpj7l2hbr0", message.folders?.get(1))
      assertEquals(1, message.from.size)
      assertEquals("Daenerys Targaryen", message.from[0].name)
      assertEquals("daenerys.t@example.com", message.from[0].email)
      assertEquals("41009df5-bf11-4c97-aa18-b285b5f2e386", message.grantId)
      assertEquals("5d3qmne77v32r8l4phyuksl2x", message.id)
      assertEquals("message", message.getObject())
      assertEquals(1, message.replyTo?.size)
      assertEquals("Daenerys Targaryen", message.replyTo?.get(0)?.name)
      assertEquals("daenerys.t@example.com", message.replyTo?.get(0)?.email)
      assertEquals("Hello, I just sent a message using Nylas!", message.snippet)
      assertEquals(true, message.starred)
      assertEquals("Hello from Nylas!", message.subject)
      assertEquals("1t8tv3890q4vgmwq6pmdwm8qgsaer", message.threadId)
      assertEquals(1, message.to?.size)
      assertEquals("Jon Snow", message.to?.get(0)?.name)
      assertEquals("j.snow@example.com", message.to?.get(0)?.email)
      assertEquals(true, message.unread)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var messages: Messages

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      messages = Messages(mockNylasClient)
    }

    @Test
    fun `listing messages calls requests with the correct params`() {
      val queryParams =
        ListMessagesQueryParams(
          limit = 10,
          pageToken = "abc-123",
          subject = "Hello from Nylas!",
          anyEmail = listOf("test@gmail.com"),
          to = listOf("rec@gmail.com"),
          cc = listOf("cc@gmail.com"),
          bcc = listOf("bcc@gmail.com"),
          from = listOf("from@gmail.com"),
          inFolder = listOf("inbox"),
          unread = true,
          starred = true,
          hasAttachment = true,
          searchQueryNative = "1634832749",
          receivedBefore = 1634832749,
          receivedAfter = 1634832749,
          fields = MessageFields.INCLUDE_HEADERS,
        )

      messages.list(grantId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Message::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `listing messages without query params calls requests with the correct params`() {
      messages.list(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Message::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a message calls requests with the correct params`() {
      val messageId = "message-123"

      messages.find(grantId, messageId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/$messageId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Message::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `updating a message calls requests with the correct params`() {
      val messageId = "message-123"
      val adapter = JsonHelper.moshi().adapter(UpdateMessageRequest::class.java)
      val updateMessageRequest =
        UpdateMessageRequest(
          starred = true,
          unread = true,
          folders = listOf("folder-1", "folder-2"),
        )

      messages.update(grantId, messageId, updateMessageRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executePut<ListResponse<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/$messageId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Message::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateMessageRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `destroying a message calls requests with the correct params`() {
      val messageId = "message-123"

      messages.destroy(grantId, messageId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeDelete<ListResponse<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/$messageId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }

  @Nested
  inner class ScheduledMessagesTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var messages: Messages

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      messages = Messages(mockNylasClient)
    }

    @Test
    fun `listing scheduled messages calls requests with the correct params`() {
      messages.listScheduledMessages(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<Response<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/schedules", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, ScheduledMessagesList::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a scheduled message calls requests with the correct params`() {
      val scheduledMessageId = "scheduled-message-123"

      messages.findScheduledMessage(grantId, scheduledMessageId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<Response<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/schedules/$scheduledMessageId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, ScheduledMessage::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `stopping a scheduled message calls requests with the correct params`() {
      val scheduledMessageId = "scheduled-message-123"

      messages.stopScheduledMessage(grantId, scheduledMessageId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeDelete<Response<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/schedules/$scheduledMessageId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, StopScheduledMessageResponse::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }

  @Nested
  inner class SendTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var messages: Messages

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      messages = Messages(mockNylasClient)
    }

    @Test
    fun `sending a message calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(SendMessageRequest::class.java)
      val sendMessageRequest =
        SendMessageRequest(
          to = listOf(EmailName(email = "test@gmail.com", name = "Test")),
          body = "Hello, I just sent a message using Nylas!",
          cc = listOf(EmailName(email = "test@gmail.com", name = "Test")),
          bcc = listOf(EmailName(email = "bcc@gmail.com", name = "BCC")),
          subject = "Hello from Nylas!",
          starred = true,
          sendAt = 1620000000,
          replyToMessageId = "reply-to-message-id",
          trackingOptions = TrackingOptions(label = "label", links = true, opens = true, threadReplies = true),
        )

      messages.send(grantId, sendMessageRequest)

      val pathCaptor = argumentCaptor<String>()
      val methodCaptor = argumentCaptor<NylasClient.HttpMethod>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<RequestBody>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeFormRequest<Response<Message>>(
        pathCaptor.capture(),
        methodCaptor.capture(),
        requestBodyCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/messages/send", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Message::class.java), typeCaptor.firstValue)
      assertEquals(NylasClient.HttpMethod.POST, methodCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
      val multipart = requestBodyCaptor.firstValue as MultipartBody
      assertEquals(1, multipart.size())
      val buffer = Buffer()
      multipart.part(0).body().writeTo(buffer)
      assertEquals(adapter.toJson(sendMessageRequest), buffer.readUtf8())
    }
  }

  @Nested
  inner class ResourceTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var messages: Messages

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      messages = Messages(mockNylasClient)
    }

    @Test
    fun `credentials returns a smart compose instance`() {
      val smartCompose = messages.smartCompose()
      assertIs<SmartCompose>(smartCompose)
    }
  }
}
