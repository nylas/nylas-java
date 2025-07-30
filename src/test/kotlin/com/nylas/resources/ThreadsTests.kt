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
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull

class ThreadsTests {
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
    fun `Thread serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Thread::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "grant_id": "ca8f1733-6063-40cc-a2e3-ec7274abef11",
            "id": "7ml84jdmfnw20sq59f30hirhe",
            "object": "thread",
            "has_attachments": false,
            "has_drafts": false,
            "earliest_message_date": 1634149514,
            "latest_message_received_date": 1634832749,
            "latest_message_sent_date": 1635174399,
            "participants": [
              {
                "email": "daenerys.t@example.com",
                "name": "Daenerys Targaryen"
              }
            ],
            "snippet": "jnlnnn --Sent with Nylas",
            "starred": false,
            "subject": "Dinner Wednesday?",
            "unread": false,
            "message_ids": [
              "njeb79kFFzli09",
              "998abue3mGH4sk"
            ],
            "draft_ids": [
              "a809kmmoW90Dx"
            ],
            "folders": [
              "8l6c4d11y1p4dm4fxj52whyr9",
              "d9zkcr2tljpu3m4qpj7l2hbr0"
            ],
            "latest_draft_or_message": {
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
                  "content": "YXR0YWNoDQoNCi0tLS0tLS0tLS0gRm9yd2FyZGVkIG1lc3NhZ2UgL=",
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
              "id": "njeb79kFFzli09",
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
          }
        """.trimIndent(),
      )

      val thread = adapter.fromJson(jsonBuffer)!!
      assertIs<Thread>(thread)
      assertEquals("ca8f1733-6063-40cc-a2e3-ec7274abef11", thread.grantId)
      assertEquals("7ml84jdmfnw20sq59f30hirhe", thread.id)
      assertEquals("thread", thread.getObject())
      assertEquals(false, thread.hasAttachments)
      assertEquals(false, thread.hasDrafts)
      assertEquals(1634149514, thread.earliestMessageDate)
      assertEquals(1634832749, thread.latestMessageReceivedDate)
      assertEquals(1635174399, thread.latestMessageSentDate)
      assertEquals(
        listOf(
          EmailName(
            email = "daenerys.t@example.com",
            name = "Daenerys Targaryen",
          ),
        ),
        thread.participants,
      )
      assertEquals("jnlnnn --Sent with Nylas", thread.snippet)
      assertEquals(false, thread.starred)
      assertEquals("Dinner Wednesday?", thread.subject)
      assertEquals(false, thread.unread)
      assertEquals(
        listOf(
          "njeb79kFFzli09",
          "998abue3mGH4sk",
        ),
        thread.messageIds,
      )
      assertEquals(
        listOf(
          "a809kmmoW90Dx",
        ),
        thread.draftIds,
      )
      assertEquals(
        listOf(
          "8l6c4d11y1p4dm4fxj52whyr9",
          "d9zkcr2tljpu3m4qpj7l2hbr0",
        ),
        thread.folders,
      )
      assertIs<Message>(thread.latestDraftOrMessage)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var threads: Threads

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      threads = Threads(mockNylasClient)
    }

    @Test
    fun `listing threads calls requests with the correct params`() {
      val queryParams = ListThreadsQueryParams(
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
        latestMessageBefore = 1634832749,
        latestMessageAfter = 1634149514,
        searchQueryNative = "1634832749",
      )

      threads.list(grantId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Thread>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/threads", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Thread::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `listing threads without query params calls requests with the correct params`() {
      threads.list(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Thread>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/threads", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Thread::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a thread calls requests with the correct params`() {
      val threadId = "thread-123"

      threads.find(grantId, threadId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Thread>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/threads/$threadId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Thread::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `updating a thread calls requests with the correct params`() {
      val threadId = "thread-123"
      val adapter = JsonHelper.moshi().adapter(UpdateThreadRequest::class.java)
      val updateThreadRequest = UpdateThreadRequest(
        starred = true,
        unread = true,
        folders = listOf("folder-1", "folder-2"),
      )

      threads.update(grantId, threadId, updateThreadRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<ListResponse<Thread>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/threads/$threadId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Thread::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateThreadRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `destroying a thread calls requests with the correct params`() {
      val threadId = "thread-123"

      threads.destroy(grantId, threadId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<ListResponse<Thread>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/threads/$threadId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `builder inFolder with string parameter works correctly`() {
      val queryParams = ListThreadsQueryParams.Builder()
        .inFolder("test-folder-id")
        .build()

      assertEquals(listOf("test-folder-id"), queryParams.inFolder)
      assertEquals("test-folder-id", queryParams.convertToMap()["in"])
    }

    @Test
    fun `builder inFolder with null string parameter works correctly`() {
      val queryParams = ListThreadsQueryParams.Builder()
        .inFolder(null as String?)
        .build()

      assertNull(queryParams.inFolder)
      assertFalse(queryParams.convertToMap().containsKey("in"))
    }

    @Test
    fun `builder inFolder with list parameter works correctly but shows deprecation warning`() {
      val queryParams = ListThreadsQueryParams.Builder()
        .inFolder(listOf("folder1", "folder2", "folder3"))
        .build()

      assertEquals(listOf("folder1", "folder2", "folder3"), queryParams.inFolder)
      // Only the first item should be used according to our implementation
      assertEquals("folder1", queryParams.convertToMap()["in"])
    }

    @Test
    fun `builder inFolder with empty list parameter works correctly`() {
      val queryParams = ListThreadsQueryParams.Builder()
        .inFolder(emptyList<String>())
        .build()

      assertEquals(emptyList<String>(), queryParams.inFolder)
      assertFalse(queryParams.convertToMap().containsKey("in"))
    }

    @Test
    fun `builder inFolder with null list parameter works correctly`() {
      val queryParams = ListThreadsQueryParams.Builder()
        .inFolder(null as List<String>?)
        .build()

      assertNull(queryParams.inFolder)
      assertFalse(queryParams.convertToMap().containsKey("in"))
    }

    @Test
    fun `convertToMap handles inFolder parameter correctly with multiple items`() {
      val queryParams = ListThreadsQueryParams(
        inFolder = listOf("folder1", "folder2", "folder3"),
      )

      val map = queryParams.convertToMap()

      // Should use only the first folder ID
      assertEquals("folder1", map["in"])
    }

    @Test
    fun `convertToMap handles inFolder parameter correctly with single item`() {
      val queryParams = ListThreadsQueryParams(
        inFolder = listOf("single-folder"),
      )

      val map = queryParams.convertToMap()

      assertEquals("single-folder", map["in"])
    }

    @Test
    fun `convertToMap handles inFolder parameter correctly with empty list`() {
      val queryParams = ListThreadsQueryParams(
        inFolder = emptyList(),
      )

      val map = queryParams.convertToMap()

      assertFalse(map.containsKey("in"))
    }

    @Test
    fun `convertToMap handles inFolder parameter correctly with null`() {
      val queryParams = ListThreadsQueryParams(
        inFolder = null,
      )

      val map = queryParams.convertToMap()

      assertFalse(map.containsKey("in"))
    }

    @Test
    fun `listing threads with new string inFolder parameter works correctly`() {
      val queryParams = ListThreadsQueryParams.Builder()
        .inFolder("test-folder-id")
        .limit(10)
        .build()

      threads.list(grantId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Thread>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/threads", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Thread::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)

      // Verify that the converted map has the correct value
      assertEquals("test-folder-id", queryParams.convertToMap()["in"])
    }
  }
}
