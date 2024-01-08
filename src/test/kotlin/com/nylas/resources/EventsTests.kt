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

class EventsTests {
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
    fun `Event serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Event::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "busy": true,
            "calendar_id": "7d93zl2palhxqdy6e5qinsakt",
            "conferencing": {
              "provider": "Zoom Meeting",
              "details": {
                "meeting_code": "code-123456",
                "password": "password-123456",
                "url": "https://zoom.us/j/1234567890?pwd=1234567890"
              }
            },
            "created_at": 1661874192,
            "description": "Description of my new calendar",
            "hide_participants": false,
            "grant_id": "41009df5-bf11-4c97-aa18-b285b5f2e386",
            "html_link": "https://www.google.com/calendar/event?eid=bTMzcGJrNW4yYjk4bjk3OWE4Ef3feD2VuM29fMjAyMjA2MjdUMjIwMDAwWiBoYWxsYUBueWxhcy5jb20",
            "id": "5d3qmne77v32r8l4phyuksl2x",
            "location": "Roller Rink",
            "metadata": {
              "your_key": "your_value"
            },
            "object": "event",
            "organizer": {
              "email": "organizer@example.com",
              "name": ""
            },
            "participants": [
              {
                "comment": "Aristotle",
                "email": "aristotle@example.com",
                "name": "Aristotle",
                "phone_number": "+1 23456778",
                "status": "maybe"
              }
            ],
            "read_only": false,
            "reminders": {
              "use_default": false,
              "overrides": [
                {
                  "reminder_minutes": 10,
                  "reminder_method": "email"
                }
              ]
            },
            "recurrence": [
              "RRULE:FREQ=WEEKLY;BYDAY=MO",
              "EXDATE:20211011T000000Z"
            ],
            "status": "confirmed",
            "title": "Birthday Party",
            "updated_at": 1661874192,
            "visibility": "private",
            "when": {
              "start_time": 1661874192,
              "end_time": 1661877792,
              "start_timezone": "America/New_York",
              "end_timezone": "America/New_York",
              "object": "timespan"
            }
          }
        """.trimIndent()
      )

      val event = adapter.fromJson(jsonBuffer)!!
      assertIs<Event>(event)
      assertEquals(true, event.busy)
      assertEquals("7d93zl2palhxqdy6e5qinsakt", event.calendarId)
      assertIs<Conferencing.Details>(event.conferencing)
      val conferencingDetails = event.conferencing as Conferencing.Details
      assertEquals(ConferencingProvider.ZOOM_MEETING, conferencingDetails.provider)
      assertEquals("code-123456", conferencingDetails.details.meetingCode)
      assertEquals("password-123456", conferencingDetails.details.password)
      assertEquals("https://zoom.us/j/1234567890?pwd=1234567890", conferencingDetails.details.url)
      assertEquals(1661874192, event.createdAt)
      assertEquals("Description of my new calendar", event.description)
      assertEquals(false, event.hideParticipants)
      assertEquals("41009df5-bf11-4c97-aa18-b285b5f2e386", event.grantId)
      assertEquals("https://www.google.com/calendar/event?eid=bTMzcGJrNW4yYjk4bjk3OWE4Ef3feD2VuM29fMjAyMjA2MjdUMjIwMDAwWiBoYWxsYUBueWxhcy5jb20", event.htmlLink)
      assertEquals("5d3qmne77v32r8l4phyuksl2x", event.id)
      assertEquals("Roller Rink", event.location)
      assertEquals(mapOf("your_key" to "your_value"), event.metadata)
      assertEquals("event", event.getObject())
      assertEquals("organizer@example.com", event.organizer?.email)
      assertEquals("", event.organizer?.name)
      assertEquals(1, event.participants.size)
      assertEquals("Aristotle", event.participants[0].comment)
      assertEquals("aristotle@example.com", event.participants[0].email)
      assertEquals("Aristotle", event.participants[0].name)
      assertEquals("+1 23456778", event.participants[0].phoneNumber)
      assertEquals(ParticipantStatus.MAYBE, event.participants[0].status)
      assertEquals(false, event.readOnly)
      assertEquals(2, event.recurrence?.size)
      assertEquals("RRULE:FREQ=WEEKLY;BYDAY=MO", event.recurrence?.get(0))
      assertEquals("EXDATE:20211011T000000Z", event.recurrence?.get(1))
      assertEquals(EventStatus.CONFIRMED, event.status)
      assertEquals("Birthday Party", event.title)
      assertEquals(1661874192, event.updatedAt)
      assertEquals(EventVisibility.PRIVATE, event.visibility)
      assertIs<When.Timespan>(event.getWhen())
      val whenTimespan = event.getWhen() as When.Timespan
      assertEquals(1661874192, whenTimespan.startTime)
      assertEquals(1661877792, whenTimespan.endTime)
      assertEquals("America/New_York", whenTimespan.startTimezone)
      assertEquals("America/New_York", whenTimespan.endTimezone)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var events: Events

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      events = Events(mockNylasClient)
    }

    @Test
    fun `listing events calls requests with the correct params`() {
      val listEventQueryParams = ListEventQueryParams(
        calendarId = "calendar-id",
      )

      events.list(grantId, listEventQueryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListEventQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture()
      )

      assertEquals("v3/grants/$grantId/events", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Event::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `finding a event calls requests with the correct params`() {
      val eventId = "event-123"
      val findEventQueryParams = FindEventQueryParams(
        calendarId = "calendar-id",
      )

      events.find(grantId, eventId, findEventQueryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<FindEventQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture()
      )

      assertEquals("v3/grants/$grantId/events/$eventId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a event calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateEventRequest::class.java)
      val createEventRequest = CreateEventRequest(
        whenObj = CreateEventRequest.When.Timespan(
          startTime = 1620000000,
          endTime = 1620000000,
          startTimezone = "America/Los_Angeles",
          endTimezone = "America/Los_Angeles"
        ),
        description = "Description of my new event",
        location = "Los Angeles, CA",
        metadata = mapOf("your-key" to "value"),
      )
      val createEventQueryParams = CreateEventQueryParams(
        calendarId = "calendar-id",
        notifyParticipants = true,
      )

      events.create(grantId, createEventRequest, createEventQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<CreateEventQueryParams>()
      verify(mockNylasClient).executePost<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture()
      )

      assertEquals("v3/grants/$grantId/events", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createEventRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a event calls requests with the correct params`() {
      val eventId = "event-123"
      val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
      val updateEventRequest = UpdateEventRequest(
        description = "Description of my new event",
        location = "Los Angeles, CA",
      )
      val updateEventQueryParams = UpdateEventQueryParams(
        calendarId = "calendar-id",
        notifyParticipants = true,
      )

      events.update(grantId, eventId, updateEventRequest, updateEventQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<UpdateEventQueryParams>()
      verify(mockNylasClient).executePut<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture()
      )

      assertEquals("v3/grants/$grantId/events/$eventId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateEventRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a event calls requests with the correct params`() {
      val eventId = "event-123"
      val destroyEventQueryParams = DestroyEventQueryParams(
        calendarId = "calendar-id",
        notifyParticipants = true,
      )

      events.destroy(grantId, eventId, destroyEventQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<DestroyEventQueryParams>()
      verify(mockNylasClient).executeDelete<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture()
      )

      assertEquals("v3/grants/$grantId/events/$eventId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }

  @Nested
  inner class OtherMethodTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var events: Events

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      events = Events(mockNylasClient)
    }

    @Test
    fun `sending RSVP calls requests with the correct params`() {
      val eventId = "event-123"
      val adapter = JsonHelper.moshi().adapter(SendRsvpRequest::class.java)
      val sendRsvpRequest = SendRsvpRequest(
        status = RsvpStatus.YES,
      )
      val sendRsvpQueryParams = SendRsvpQueryParams(
        calendarId = "calendar-id",
      )

      events.sendRsvp(grantId, eventId, sendRsvpRequest, sendRsvpQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<SendRsvpQueryParams>()
      verify(mockNylasClient).executePost<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture()
      )

      assertEquals("v3/grants/$grantId/events/$eventId/send-rsvp", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertEquals(adapter.toJson(sendRsvpRequest), requestBodyCaptor.firstValue)
    }
  }
}