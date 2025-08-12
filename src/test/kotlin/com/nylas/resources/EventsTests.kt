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
    fun `Event serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Event::class.java)
      val jsonBuffer =
        Buffer().writeUtf8(
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
            },
            "notetaker": {
              "id": "notetaker-123",
              "name": "Event Notetaker",
              "meeting_settings": {
                "video_recording": false,
                "audio_recording": true,
                "transcription": true
              }
            }
          }
          """.trimIndent(),
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
      assertEquals(
        "https://www.google.com/calendar/event?eid=bTMzcGJrNW4yYjk4bjk3OWE4Ef3feD2VuM29fMjAyMjA2MjdUMjIwMDAwWiBoYWxsYUBueWxhcy5jb20",
        event.htmlLink,
      )
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
      assertEquals(false, event.reminders?.useDefault)
      assertEquals(1, event.reminders?.overrides?.size)
      assertEquals(10, event.reminders?.overrides?.get(0)?.reminderMinutes)
      assertEquals(ReminderMethod.EMAIL, event.reminders?.overrides?.get(0)?.reminderMethod)
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

      // Verify notetaker field
      assertEquals("notetaker-123", event.notetaker?.id)
      assertEquals("Event Notetaker", event.notetaker?.name)
      assertEquals(false, event.notetaker?.meetingSettings?.videoRecording)
      assertEquals(true, event.notetaker?.meetingSettings?.audioRecording)
      assertEquals(true, event.notetaker?.meetingSettings?.transcription)
    }

    @Test
    fun `Event serializes when with date type properly`() {
      val adapter = JsonHelper.moshi().adapter(Event::class.java)
      val jsonBuffer =
        Buffer().writeUtf8(
          """
          {
            "busy": true,
            "calendar_id": "7d93zl2palhxqdy6e5qinsakt",
            "created_at": 1661874192,
            "description": "Description of my new calendar",
            "hide_participants": false,
            "grant_id": "41009df5-bf11-4c97-aa18-b285b5f2e386",
            "html_link": "https://www.google.com/calendar/event?eid=bTMzcGJrNW4yYjk4bjk3OWE4Ef3feD2VuM29fMjAyMjA2MjdUMjIwMDAwWiBoYWxsYUBueWxhcy5jb20",
            "id": "5d3qmne77v32r8l4phyuksl2x",
            "location": "Roller Rink",
            "object": "event",
            "organizer": {
              "email": "organizer@example.com",
              "name": ""
            },
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
            "status": "confirmed",
            "title": "Birthday Party",
            "updated_at": 1661874192,
            "visibility": "private",
            "when": {
              "date": "2024-06-18",
              "object": "date"
            }
          }
          """.trimIndent(),
        )

      val event = adapter.fromJson(jsonBuffer)!!
      assertIs<Event>(event)
      assertIs<When.Date>(event.getWhen())
      val whenDate = event.getWhen() as When.Date
      assertEquals("2024-06-18", whenDate.date)
    }

    @Test
    fun `CreateEventAutoConferencingProvider serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CreateEventAutoConferencingProvider::class.java)

      assertEquals("\"Google Meet\"", adapter.toJson(CreateEventAutoConferencingProvider.GOOGLE_MEET))
      assertEquals("\"Zoom Meeting\"", adapter.toJson(CreateEventAutoConferencingProvider.ZOOM_MEETING))
      assertEquals("\"Microsoft Teams\"", adapter.toJson(CreateEventAutoConferencingProvider.MICROSOFT_TEAMS))
    }

    @Test
    fun `CreateEventAutoConferencingProvider deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CreateEventAutoConferencingProvider::class.java)

      assertEquals(CreateEventAutoConferencingProvider.GOOGLE_MEET, adapter.fromJson("\"Google Meet\""))
      assertEquals(CreateEventAutoConferencingProvider.ZOOM_MEETING, adapter.fromJson("\"Zoom Meeting\""))
      assertEquals(CreateEventAutoConferencingProvider.MICROSOFT_TEAMS, adapter.fromJson("\"Microsoft Teams\""))
    }

    @Test
    fun `CreateEventManualConferencingProvider serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CreateEventManualConferencingProvider::class.java)

      assertEquals("\"Google Meet\"", adapter.toJson(CreateEventManualConferencingProvider.GOOGLE_MEET))
      assertEquals("\"Zoom Meeting\"", adapter.toJson(CreateEventManualConferencingProvider.ZOOM_MEETING))
      assertEquals("\"Microsoft Teams\"", adapter.toJson(CreateEventManualConferencingProvider.MICROSOFT_TEAMS))
      assertEquals("\"Teams for Business\"", adapter.toJson(CreateEventManualConferencingProvider.TEAMS_FOR_BUSINESS))
      assertEquals("\"Skype for Business\"", adapter.toJson(CreateEventManualConferencingProvider.SKYPE_FOR_BUSINESS))
      assertEquals("\"Skype for Consumer\"", adapter.toJson(CreateEventManualConferencingProvider.SKYPE_FOR_CONSUMER))
    }

    @Test
    fun `CreateEventManualConferencingProvider deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CreateEventManualConferencingProvider::class.java)

      assertEquals(CreateEventManualConferencingProvider.GOOGLE_MEET, adapter.fromJson("\"Google Meet\""))
      assertEquals(CreateEventManualConferencingProvider.ZOOM_MEETING, adapter.fromJson("\"Zoom Meeting\""))
      assertEquals(CreateEventManualConferencingProvider.MICROSOFT_TEAMS, adapter.fromJson("\"Microsoft Teams\""))
      assertEquals(CreateEventManualConferencingProvider.TEAMS_FOR_BUSINESS, adapter.fromJson("\"Teams for Business\""))
      assertEquals(CreateEventManualConferencingProvider.SKYPE_FOR_BUSINESS, adapter.fromJson("\"Skype for Business\""))
      assertEquals(CreateEventManualConferencingProvider.SKYPE_FOR_CONSUMER, adapter.fromJson("\"Skype for Consumer\""))
    }

    @Test
    fun `CreateEventRequest with Autocreate conferencing serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CreateEventRequest::class.java)
      val createEventRequest = CreateEventRequest(
        whenObj = CreateEventRequest.When.Time(1620000000),
        title = "Test Event",
        conferencing = CreateEventRequest.Conferencing.Autocreate(
          provider = CreateEventAutoConferencingProvider.GOOGLE_MEET,
          autocreate = mapOf("setting1" to "value1"),
        ),
      )

      val json = adapter.toJson(createEventRequest)
      val jsonMap = JsonHelper.moshi().adapter(Map::class.java).fromJson(json)!!

      val conferencing = jsonMap["conferencing"] as Map<*, *>
      assertEquals("Google Meet", conferencing["provider"])
      assertEquals(mapOf("setting1" to "value1"), conferencing["autocreate"])
    }

    @Test
    fun `CreateEventRequest with Details conferencing serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CreateEventRequest::class.java)
      val createEventRequest = CreateEventRequest(
        whenObj = CreateEventRequest.When.Time(1620000000),
        title = "Test Event",
        conferencing = CreateEventRequest.Conferencing.Details(
          provider = CreateEventManualConferencingProvider.ZOOM_MEETING,
          details = CreateEventRequest.Conferencing.Details.Config(
            url = "https://zoom.us/j/123456789",
            meetingCode = "123456789",
            password = "secret",
          ),
        ),
      )

      val json = adapter.toJson(createEventRequest)
      val jsonMap = JsonHelper.moshi().adapter(Map::class.java).fromJson(json)!!

      val conferencing = jsonMap["conferencing"] as Map<*, *>
      assertEquals("Zoom Meeting", conferencing["provider"])
      val details = conferencing["details"] as Map<*, *>
      assertEquals("https://zoom.us/j/123456789", details["url"])
      assertEquals("123456789", details["meeting_code"])
      assertEquals("secret", details["password"])
    }

    @Test
    fun `UpdateEventRequest with Autocreate conferencing serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
      val updateEventRequest = UpdateEventRequest(
        title = "Updated Event",
        conferencing = UpdateEventRequest.Conferencing.Autocreate(
          provider = UpdateEventAutoConferencingProvider.MICROSOFT_TEAMS,
          autocreate = emptyMap(),
        ),
      )

      val json = adapter.toJson(updateEventRequest)
      val jsonMap = JsonHelper.moshi().adapter(Map::class.java).fromJson(json)!!

      val conferencing = jsonMap["conferencing"] as Map<*, *>
      assertEquals("Microsoft Teams", conferencing["provider"])
      assertEquals(emptyMap<String, Any>(), conferencing["autocreate"])
    }

    @Test
    fun `UpdateEventRequest with Details conferencing serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
      val updateEventRequest = UpdateEventRequest(
        title = "Updated Event",
        conferencing = UpdateEventRequest.Conferencing.Details(
          provider = UpdateEventManualConferencingProvider.TEAMS_FOR_BUSINESS,
          details = UpdateEventRequest.Conferencing.Details.Config(
            url = "https://teams.microsoft.com/join/123",
            pin = "123456",
          ),
        ),
      )

      val json = adapter.toJson(updateEventRequest)
      val jsonMap = JsonHelper.moshi().adapter(Map::class.java).fromJson(json)!!

      val conferencing = jsonMap["conferencing"] as Map<*, *>
      assertEquals("Teams for Business", conferencing["provider"])
      val details = conferencing["details"] as Map<*, *>
      assertEquals("https://teams.microsoft.com/join/123", details["url"])
      assertEquals("123456", details["pin"])
    }

    @Test
    fun `Event with existing ConferencingProvider still works properly`() {
      // This test verifies that the original Event model continues to work with the original ConferencingProvider enum
      // The Event model uses the original Conferencing sealed class, not the new CreateEvent/UpdateEvent ones
      val adapter = JsonHelper.moshi().adapter(Event::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
        {
          "id": "event-123",
          "grant_id": "grant-456", 
          "calendar_id": "calendar-789",
          "when": {
            "time": 1620000000,
            "object": "time"
          },
          "title": "Test Event with Original Conferencing",
          "object": "event",
          "conferencing": {
            "provider": "Zoom Meeting",
            "details": {
              "url": "https://zoom.us/j/123456789",
              "meeting_code": "123456789"
            }
          }
        }
        """.trimIndent(),
      )

      val event = adapter.fromJson(jsonBuffer)!!
      assertIs<Conferencing.Details>(event.conferencing)
      val details = event.conferencing as Conferencing.Details
      assertEquals(ConferencingProvider.ZOOM_MEETING, details.provider)
      assertEquals("https://zoom.us/j/123456789", details.details.url)
      assertEquals("123456789", details.details.meetingCode)
    }

    @Test
    fun `CreateEventRequest Autocreate with deprecated ConferencingProvider works properly`() {
      // Test backward compatibility using deprecated fromConferencingProvider method
      @Suppress("DEPRECATION")
      val autocreate = CreateEventRequest.Conferencing.Autocreate.fromConferencingProvider(
        ConferencingProvider.GOOGLE_MEET,
        mapOf("setting1" to "value1"),
      )

      assertEquals(CreateEventAutoConferencingProvider.GOOGLE_MEET, autocreate.provider)
      assertEquals(mapOf("setting1" to "value1"), autocreate.autocreate)
    }

    @Test
    fun `CreateEventRequest Details with deprecated ConferencingProvider works properly`() {
      // Test backward compatibility using deprecated fromConferencingProvider method
      val config = CreateEventRequest.Conferencing.Details.Config(
        url = "https://zoom.us/j/123456789",
        meetingCode = "123456789",
      )

      @Suppress("DEPRECATION")
      val details = CreateEventRequest.Conferencing.Details.fromConferencingProvider(
        ConferencingProvider.ZOOM_MEETING,
        config,
      )

      assertEquals(CreateEventManualConferencingProvider.ZOOM_MEETING, details.provider)
      assertEquals(config, details.details)
    }

    @Test
    fun `UpdateEventRequest Autocreate with deprecated ConferencingProvider works properly`() {
      // Test backward compatibility using deprecated fromConferencingProvider method
      @Suppress("DEPRECATION")
      val autocreate = UpdateEventRequest.Conferencing.Autocreate.fromConferencingProvider(
        ConferencingProvider.MICROSOFT_TEAMS,
        mapOf("teams_setting" to true),
      )

      assertEquals(UpdateEventAutoConferencingProvider.MICROSOFT_TEAMS, autocreate.provider)
      assertEquals(mapOf("teams_setting" to true), autocreate.autocreate)
    }

    @Test
    fun `UpdateEventRequest Details with deprecated ConferencingProvider works properly`() {
      // Test backward compatibility using deprecated fromConferencingProvider method
      val config = UpdateEventRequest.Conferencing.Details.Config(
        url = "https://teams.microsoft.com/join/123",
        pin = "654321",
      )

      @Suppress("DEPRECATION")
      val details = UpdateEventRequest.Conferencing.Details.fromConferencingProvider(
        ConferencingProvider.MICROSOFT_TEAMS,
        config,
      )

      assertEquals(UpdateEventManualConferencingProvider.MICROSOFT_TEAMS, details.provider)
      assertEquals(config, details.details)
    }

    @Test
    fun `UpdateEventAutoConferencingProvider serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(UpdateEventAutoConferencingProvider::class.java)

      assertEquals("\"Google Meet\"", adapter.toJson(UpdateEventAutoConferencingProvider.GOOGLE_MEET))
      assertEquals("\"Zoom Meeting\"", adapter.toJson(UpdateEventAutoConferencingProvider.ZOOM_MEETING))
      assertEquals("\"Microsoft Teams\"", adapter.toJson(UpdateEventAutoConferencingProvider.MICROSOFT_TEAMS))
    }

    @Test
    fun `UpdateEventAutoConferencingProvider deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(UpdateEventAutoConferencingProvider::class.java)

      assertEquals(UpdateEventAutoConferencingProvider.GOOGLE_MEET, adapter.fromJson("\"Google Meet\""))
      assertEquals(UpdateEventAutoConferencingProvider.ZOOM_MEETING, adapter.fromJson("\"Zoom Meeting\""))
      assertEquals(UpdateEventAutoConferencingProvider.MICROSOFT_TEAMS, adapter.fromJson("\"Microsoft Teams\""))
    }

    @Test
    fun `UpdateEventManualConferencingProvider serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(UpdateEventManualConferencingProvider::class.java)

      assertEquals("\"Google Meet\"", adapter.toJson(UpdateEventManualConferencingProvider.GOOGLE_MEET))
      assertEquals("\"Zoom Meeting\"", adapter.toJson(UpdateEventManualConferencingProvider.ZOOM_MEETING))
      assertEquals("\"Microsoft Teams\"", adapter.toJson(UpdateEventManualConferencingProvider.MICROSOFT_TEAMS))
      assertEquals("\"Teams for Business\"", adapter.toJson(UpdateEventManualConferencingProvider.TEAMS_FOR_BUSINESS))
      assertEquals("\"Skype for Business\"", adapter.toJson(UpdateEventManualConferencingProvider.SKYPE_FOR_BUSINESS))
      assertEquals("\"Skype for Consumer\"", adapter.toJson(UpdateEventManualConferencingProvider.SKYPE_FOR_CONSUMER))
    }

    @Test
    fun `UpdateEventManualConferencingProvider deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(UpdateEventManualConferencingProvider::class.java)

      assertEquals(UpdateEventManualConferencingProvider.GOOGLE_MEET, adapter.fromJson("\"Google Meet\""))
      assertEquals(UpdateEventManualConferencingProvider.ZOOM_MEETING, adapter.fromJson("\"Zoom Meeting\""))
      assertEquals(UpdateEventManualConferencingProvider.MICROSOFT_TEAMS, adapter.fromJson("\"Microsoft Teams\""))
      assertEquals(UpdateEventManualConferencingProvider.TEAMS_FOR_BUSINESS, adapter.fromJson("\"Teams for Business\""))
      assertEquals(UpdateEventManualConferencingProvider.SKYPE_FOR_BUSINESS, adapter.fromJson("\"Skype for Business\""))
      assertEquals(UpdateEventManualConferencingProvider.SKYPE_FOR_CONSUMER, adapter.fromJson("\"Skype for Consumer\""))
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
      val listEventQueryParams =
        ListEventQueryParams(
          calendarId = "calendar-id",
        )

      events.list(grantId, listEventQueryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/events", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Event::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `listing import events calls requests with the correct params`() {
      val listImportEventQueryParams =
        ListImportEventQueryParams.Builder("calendar-id")
          .limit(50)
          .pageToken("next-page-token")
          .start(1620000000)
          .end(1620100000)
          .select("id,title,when")
          .build()

      events.listImportEvents(grantId, listImportEventQueryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListImportEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/events/import", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Event::class.java), typeCaptor.firstValue)
      assertEquals(listImportEventQueryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a event calls requests with the correct params`() {
      val eventId = "event-123"
      val findEventQueryParams =
        FindEventQueryParams(
          calendarId = "calendar-id",
        )

      events.find(grantId, eventId, findEventQueryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<FindEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/events/$eventId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a event calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateEventRequest::class.java)
      val createEventRequest =
        CreateEventRequest(
          whenObj =
          CreateEventRequest.When.Timespan(
            startTime = 1620000000,
            endTime = 1620000000,
            startTimezone = "America/Los_Angeles",
            endTimezone = "America/Los_Angeles",
          ),
          description = "Description of my new event",
          location = "Los Angeles, CA",
          metadata = mapOf("your-key" to "value"),
          notetaker = EventNotetakerRequest(
            name = "Event Creation Notetaker",
            meetingSettings = EventNotetakerRequest.MeetingSettings(
              videoRecording = true,
              audioRecording = true,
              transcription = true,
            ),
          ),
        )
      val createEventQueryParams =
        CreateEventQueryParams(
          calendarId = "calendar-id",
          notifyParticipants = true,
        )

      events.create(grantId, createEventRequest, createEventQueryParams)
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

      assertEquals("v3/grants/$grantId/events", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createEventRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a event calls requests with the correct params`() {
      val eventId = "event-123"
      val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
      val updateEventRequest =
        UpdateEventRequest(
          description = "Description of my new event",
          location = "Los Angeles, CA",
          notetaker = EventNotetakerRequest(
            name = "Updated Event Notetaker",
            meetingSettings = EventNotetakerRequest.MeetingSettings(
              videoRecording = false,
              audioRecording = true,
              transcription = true,
            ),
          ),
        )
      val updateEventQueryParams =
        UpdateEventQueryParams(
          calendarId = "calendar-id",
          notifyParticipants = true,
        )

      events.update(grantId, eventId, updateEventRequest, updateEventQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<UpdateEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/events/$eventId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateEventRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating an event with autocreate conferencing calls requests with the correct params`() {
      val eventId = "event-123"
      val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
      val updateEventRequest =
        UpdateEventRequest(
          title = "Updated Meeting with Autocreate",
          conferencing = UpdateEventRequest.Conferencing.Autocreate(
            provider = UpdateEventAutoConferencingProvider.ZOOM_MEETING,
            autocreate = mapOf("waiting_room" to true, "join_before_host" to false),
          ),
        )
      val updateEventQueryParams =
        UpdateEventQueryParams(
          calendarId = "calendar-id",
          notifyParticipants = true,
        )

      events.update(grantId, eventId, updateEventRequest, updateEventQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<UpdateEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/events/$eventId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateEventRequest), requestBodyCaptor.firstValue)

      // Verify the JSON contains the correct conferencing data
      val jsonMap = JsonHelper.moshi().adapter(Map::class.java).fromJson(requestBodyCaptor.firstValue)!!
      val conferencing = jsonMap["conferencing"] as Map<*, *>
      assertEquals("Zoom Meeting", conferencing["provider"])
      val autocreate = conferencing["autocreate"] as Map<*, *>
      assertEquals(true, autocreate["waiting_room"])
      assertEquals(false, autocreate["join_before_host"])
    }

    @Test
    fun `updating an event with manual conferencing details calls requests with the correct params`() {
      val eventId = "event-123"
      val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
      val updateEventRequest =
        UpdateEventRequest(
          title = "Updated Meeting with Manual Conferencing",
          conferencing = UpdateEventRequest.Conferencing.Details(
            provider = UpdateEventManualConferencingProvider.SKYPE_FOR_BUSINESS,
            details = UpdateEventRequest.Conferencing.Details.Config(
              url = "https://meet.lync.com/example/123",
              password = "secret123",
              phone = listOf("+1-555-123-4567", "+1-555-987-6543"),
            ),
          ),
        )
      val updateEventQueryParams =
        UpdateEventQueryParams(
          calendarId = "calendar-id",
          notifyParticipants = true,
        )

      events.update(grantId, eventId, updateEventRequest, updateEventQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<UpdateEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/events/$eventId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateEventRequest), requestBodyCaptor.firstValue)

      // Verify the JSON contains the correct conferencing data
      val jsonMap = JsonHelper.moshi().adapter(Map::class.java).fromJson(requestBodyCaptor.firstValue)!!
      val conferencing = jsonMap["conferencing"] as Map<*, *>
      assertEquals("Skype for Business", conferencing["provider"])
      val details = conferencing["details"] as Map<*, *>
      assertEquals("https://meet.lync.com/example/123", details["url"])
      assertEquals("secret123", details["password"])
      assertEquals(listOf("+1-555-123-4567", "+1-555-987-6543"), details["phone"])
    }

    @Test
    fun `updating event reminders calls requests with the correct params`() {
      val eventId = "event-123"
      val updateEventRequest =
        UpdateEventRequest(
          reminders = Reminders(
            useDefault = false,
            overrides = listOf(
              ReminderOverride(
                reminderMinutes = 15,
                reminderMethod = ReminderMethod.EMAIL,
              ),
              ReminderOverride(
                reminderMinutes = 30,
                reminderMethod = ReminderMethod.POPUP,
              ),
            ),
          ),
        )
      val updateEventQueryParams =
        UpdateEventQueryParams(
          calendarId = "calendar-id",
          notifyParticipants = true,
        )

      events.update(grantId, eventId, updateEventRequest, updateEventQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<UpdateEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/events/$eventId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Event::class.java), typeCaptor.firstValue)

      // Parse both expected and actual JSON into Maps to compare structure while preserving field names
      val jsonAdapter = JsonHelper.moshi().adapter(Map::class.java)
      val actualJson = jsonAdapter.fromJson(requestBodyCaptor.firstValue)!!

      val expectedJson = mapOf(
        "reminders" to mapOf(
          "use_default" to false,
          "overrides" to listOf(
            mapOf(
              "reminder_minutes" to 15.0,
              "reminder_method" to "email",
            ),
            mapOf(
              "reminder_minutes" to 30.0,
              "reminder_method" to "popup",
            ),
          ),
        ),
      )

      assertEquals(expectedJson, actualJson)

      // Also verify that the request can be correctly deserialized
      val adapter = JsonHelper.moshi().adapter(UpdateEventRequest::class.java)
      val actualRequest = adapter.fromJson(requestBodyCaptor.firstValue)
      assertEquals(updateEventRequest, actualRequest)
    }

    @Test
    fun `destroying a event calls requests with the correct params`() {
      val eventId = "event-123"
      val destroyEventQueryParams =
        DestroyEventQueryParams(
          calendarId = "calendar-id",
          notifyParticipants = true,
        )

      events.destroy(grantId, eventId, destroyEventQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<DestroyEventQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
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
      val sendRsvpRequest =
        SendRsvpRequest(
          status = RsvpStatus.YES,
        )
      val sendRsvpQueryParams =
        SendRsvpQueryParams(
          calendarId = "calendar-id",
        )

      events.sendRsvp(grantId, eventId, sendRsvpRequest, sendRsvpQueryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<SendRsvpQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<ListResponse<Event>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/events/$eventId/send-rsvp", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertEquals(adapter.toJson(sendRsvpRequest), requestBodyCaptor.firstValue)
    }
  }
}
