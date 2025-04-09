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

class NotetakersTests {
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
    fun `Notetaker serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Notetaker::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "notetaker-123",
            "name": "Nylas Notetaker",
            "join_time": 1234567890,
            "meeting_link": "https://meet.google.com/abc-123",
            "meeting_provider": "Google Meet",
            "state": "scheduled",
            "object": "notetaker",
            "meeting_settings": {
              "video_recording": true,
              "audio_recording": true,
              "transcription": true
            }
          }
        """.trimIndent(),
      )

      val notetaker = adapter.fromJson(jsonBuffer)!!
      assertIs<Notetaker>(notetaker)
      assertEquals("notetaker-123", notetaker.id)
      assertEquals("Nylas Notetaker", notetaker.name)
      assertEquals(1234567890, notetaker.joinTime)
      assertEquals("https://meet.google.com/abc-123", notetaker.meetingLink)
      assertEquals(Notetaker.MeetingProvider.GOOGLE_MEET, notetaker.meetingProvider)
      assertEquals(Notetaker.NotetakerState.SCHEDULED, notetaker.state)
      assertEquals("notetaker", notetaker.getObject())
      assertEquals(true, notetaker.meetingSettings?.videoRecording)
      assertEquals(true, notetaker.meetingSettings?.audioRecording)
      assertEquals(true, notetaker.meetingSettings?.transcription)
    }

    @Test
    fun `DeleteResponse serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(DeleteResponse::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "request_id": "req-123"
          }
        """.trimIndent(),
      )

      val response = adapter.fromJson(jsonBuffer)!!
      assertIs<DeleteResponse>(response)
      assertEquals("req-123", response.requestId)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var notetakers: Notetakers

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      notetakers = Notetakers(mockNylasClient)
    }

    @Test
    fun `listing notetakers calls requests with the correct params`() {
      val queryParams = ListNotetakersQueryParams(
        limit = 10,
        pageToken = "abc-123",
        select = "id,updated_at",
      )

      notetakers.list(queryParams, grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListNotetakersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Notetaker>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/notetakers", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Notetaker::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a notetaker calls requests with the correct params`() {
      val notetakerId = "notetaker-123"

      notetakers.find(notetakerId, grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Notetaker>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        isNull(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/notetakers/$notetakerId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Notetaker::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a notetaker calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateNotetakerRequest::class.java)
      val createNotetakerRequest = CreateNotetakerRequest(
        meetingLink = "https://meet.example.com/meeting",
        name = "Meeting Assistant",
        joinTime = 1625097600,
        meetingSettings = CreateNotetakerRequest.MeetingSettings(
          videoRecording = true,
          audioRecording = true,
          transcription = true,
        ),
      )

      notetakers.create(createNotetakerRequest, grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Notetaker>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        isNull(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/notetakers", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Notetaker::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createNotetakerRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a notetaker calls requests with the correct params`() {
      val notetakerId = "notetaker-123"
      val adapter = JsonHelper.moshi().adapter(UpdateNotetakerRequest::class.java)
      val updateNotetakerRequest = UpdateNotetakerRequest(
        name = "Updated Meeting Assistant",
        joinTime = 1625184000,
        meetingSettings = UpdateNotetakerRequest.MeetingSettings(
          videoRecording = false,
          audioRecording = true,
          transcription = true,
        ),
      )

      notetakers.update(notetakerId, updateNotetakerRequest, grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<Notetaker>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        isNull(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/notetakers/$notetakerId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Notetaker::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateNotetakerRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `leaving a notetaker calls requests with the correct params`() {
      val notetakerId = "notetaker-123"

      notetakers.leave(notetakerId, grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<LeaveNotetakerResponse>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        isNull(),
        isNull(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/notetakers/$notetakerId/leave", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, LeaveNotetakerResponse::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `downloading notetaker media calls requests with the correct params`() {
      val notetakerId = "notetaker-123"

      notetakers.downloadMedia(notetakerId, grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<NotetakerMediaResponse>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        isNull(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/notetakers/$notetakerId/media", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, NotetakerMediaResponse::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `canceling a notetaker calls requests with the correct params`() {
      val notetakerId = "notetaker-123"

      notetakers.cancel(notetakerId, grantId)

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

      assertEquals("v3/grants/$grantId/notetakers/$notetakerId/cancel", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }
}
