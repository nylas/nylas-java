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

class CalendarsTest {
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
    fun `Calendar serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Calendar::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "grant_id": "abc-123-grant-id",
            "description": "Description of my new calendar",
            "hex_color": "#039BE5",
            "hex_foreground_color": "#039BE5",
            "id": "5d3qmne77v32r8l4phyuksl2x",
            "is_owned_by_user": true,
            "is_primary": true,
            "location": "Los Angeles, CA",
            "metadata": {
              "your-key": "value"
            },
            "name": "My New Calendar",
            "object": "calendar",
            "read_only": false,
            "timezone": "America/Los_Angeles",
            "notetaker": {
              "name": "Custom Calendar Notetaker",
              "meeting_settings": {
                "video_recording": true,
                "audio_recording": true,
                "transcription": false
              },
              "rules": {
                "event_selection": ["internal", "all"],
                "participant_filter": {
                  "participants_gte": 2,
                  "participants_lte": 10
                }
              }
            }
          }
        """.trimIndent(),
      )

      val cal = adapter.fromJson(jsonBuffer)!!
      assertIs<Calendar>(cal)
      assertEquals("abc-123-grant-id", cal.grantId)
      assertEquals("Description of my new calendar", cal.description)
      assertEquals("#039BE5", cal.hexColor)
      assertEquals("#039BE5", cal.hexForegroundColor)
      assertEquals("5d3qmne77v32r8l4phyuksl2x", cal.id)
      assertEquals(true, cal.isOwnedByUser)
      assertEquals(true, cal.isPrimary)
      assertEquals("Los Angeles, CA", cal.location)
      assertEquals("My New Calendar", cal.name)
      assertEquals(false, cal.readOnly)
      assertEquals("America/Los_Angeles", cal.timezone)
      assertEquals("Custom Calendar Notetaker", cal.notetaker?.name)
      assertEquals(true, cal.notetaker?.meetingSettings?.videoRecording)
      assertEquals(true, cal.notetaker?.meetingSettings?.audioRecording)
      assertEquals(false, cal.notetaker?.meetingSettings?.transcription)
      assertEquals(2, cal.notetaker?.rules?.eventSelection?.size)
      assertEquals(CalendarNotetaker.EventSelectionType.INTERNAL, cal.notetaker?.rules?.eventSelection?.get(0))
      assertEquals(CalendarNotetaker.EventSelectionType.ALL, cal.notetaker?.rules?.eventSelection?.get(1))
      assertEquals(2, cal.notetaker?.rules?.participantFilter?.participantsGte)
      assertEquals(10, cal.notetaker?.rules?.participantFilter?.participantsLte)
    }

    @Test
    fun `GetFreeBusyRequest serializes tentative as busy when set`() {
      val adapter = JsonHelper.moshi().adapter(GetFreeBusyRequest::class.java)
      val request = GetFreeBusyRequest(
        startTime = 1620000000,
        endTime = 1620003600,
        emails = listOf("test@nylas.com"),
        tentativeAsBusy = true,
      )

      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!

      assertEquals(true, deserialized.tentativeAsBusy)
    }

    @Test
    fun `GetFreeBusyRequest legacy constructor remains backward compatible`() {
      val adapter = JsonHelper.moshi().adapter(GetFreeBusyRequest::class.java)
      val request = GetFreeBusyRequest(
        1620000000,
        1620003600,
        listOf("test@nylas.com"),
      )

      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!

      assertEquals(null, deserialized.tentativeAsBusy)
      assertEquals(false, json.contains("tentative_as_busy"))
    }
  }

  @Nested
  inner class CreateCalendarRequestTests {
    @Test
    fun `CreateCalendarRequest serializes all fields to JSON correctly`() {
      val adapter = JsonHelper.moshi().adapter(CreateCalendarRequest::class.java)
      val request = CreateCalendarRequest(
        name = "My New Calendar",
        description = "Description of my new calendar",
        location = "Los Angeles, CA",
        timezone = "America/Los_Angeles",
        metadata = mapOf("your-key" to "value"),
        notetaker = CalendarNotetaker(
          name = "Custom Notetaker",
          meetingSettings = CalendarNotetaker.MeetingSettings(
            videoRecording = true,
            audioRecording = false,
            transcription = true,
          ),
          rules = CalendarNotetaker.Rules(
            eventSelection = listOf(CalendarNotetaker.EventSelectionType.INTERNAL),
            participantFilter = CalendarNotetaker.ParticipantFilter(
              participantsGte = 2,
              participantsLte = 10,
            ),
          ),
        ),
      )

      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!

      assertEquals("My New Calendar", deserialized.name)
      assertEquals("Description of my new calendar", deserialized.description)
      assertEquals("Los Angeles, CA", deserialized.location)
      assertEquals("America/Los_Angeles", deserialized.timezone)
      assertEquals(mapOf("your-key" to "value"), deserialized.metadata)
      assertEquals("Custom Notetaker", deserialized.notetaker?.name)
      assertEquals(true, deserialized.notetaker?.meetingSettings?.videoRecording)
      assertEquals(false, deserialized.notetaker?.meetingSettings?.audioRecording)
      assertEquals(true, deserialized.notetaker?.meetingSettings?.transcription)
      assertEquals(1, deserialized.notetaker?.rules?.eventSelection?.size)
      assertEquals(CalendarNotetaker.EventSelectionType.INTERNAL, deserialized.notetaker?.rules?.eventSelection?.get(0))
      assertEquals(2, deserialized.notetaker?.rules?.participantFilter?.participantsGte)
      assertEquals(10, deserialized.notetaker?.rules?.participantFilter?.participantsLte)
    }

    @Test
    fun `CreateCalendarRequest serializes with only required name field`() {
      val adapter = JsonHelper.moshi().adapter(CreateCalendarRequest::class.java)
      val request = CreateCalendarRequest(name = "Minimal Calendar")

      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!

      assertEquals("Minimal Calendar", deserialized.name)
      assertNull(deserialized.description)
      assertNull(deserialized.location)
      assertNull(deserialized.timezone)
      assertNull(deserialized.metadata)
      assertNull(deserialized.notetaker)
    }

    @Test
    fun `CreateCalendarRequest Builder sets all fields correctly`() {
      val request = CreateCalendarRequest.Builder("My New Calendar")
        .description("Description of my new calendar")
        .location("Los Angeles, CA")
        .timezone("America/Los_Angeles")
        .metadata(mapOf("your-key" to "value"))
        .notetaker(CalendarNotetaker(name = "Custom Notetaker"))
        .build()

      assertEquals("My New Calendar", request.name)
      assertEquals("Description of my new calendar", request.description)
      assertEquals("Los Angeles, CA", request.location)
      assertEquals("America/Los_Angeles", request.timezone)
      assertEquals(mapOf("your-key" to "value"), request.metadata)
      assertEquals("Custom Notetaker", request.notetaker?.name)
    }

    @Test
    fun `CreateCalendarRequest Builder builds with only required name field`() {
      val request = CreateCalendarRequest.Builder("Minimal Calendar").build()

      assertEquals("Minimal Calendar", request.name)
      assertNull(request.description)
      assertNull(request.location)
      assertNull(request.timezone)
      assertNull(request.metadata)
      assertNull(request.notetaker)
    }
  }

  @Nested
  inner class UpdateCalendarRequestTests {
    @Test
    fun `UpdateCalendarRequest serializes all fields to JSON correctly`() {
      val adapter = JsonHelper.moshi().adapter(UpdateCalendarRequest::class.java)
      val request = UpdateCalendarRequest(
        name = "My Updated Calendar",
        description = "Updated description",
        location = "New York, NY",
        timezone = "America/New_York",
        metadata = mapOf("key" to "value"),
        hexColor = "#039BE5",
        hexForegroundColor = "#FFFFFF",
        notetaker = CalendarNotetaker(
          name = "Updated Notetaker",
          meetingSettings = CalendarNotetaker.MeetingSettings(
            videoRecording = false,
            audioRecording = true,
            transcription = true,
          ),
          rules = CalendarNotetaker.Rules(
            eventSelection = listOf(CalendarNotetaker.EventSelectionType.EXTERNAL),
            participantFilter = CalendarNotetaker.ParticipantFilter(
              participantsGte = null,
              participantsLte = 15,
            ),
          ),
        ),
      )

      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!

      assertEquals("My Updated Calendar", deserialized.name)
      assertEquals("Updated description", deserialized.description)
      assertEquals("New York, NY", deserialized.location)
      assertEquals("America/New_York", deserialized.timezone)
      assertEquals(mapOf("key" to "value"), deserialized.metadata)
      assertEquals("#039BE5", deserialized.hexColor)
      assertEquals("#FFFFFF", deserialized.hexForegroundColor)
      assertEquals("Updated Notetaker", deserialized.notetaker?.name)
      assertEquals(false, deserialized.notetaker?.meetingSettings?.videoRecording)
      assertEquals(true, deserialized.notetaker?.meetingSettings?.audioRecording)
      assertEquals(true, deserialized.notetaker?.meetingSettings?.transcription)
      assertEquals(1, deserialized.notetaker?.rules?.eventSelection?.size)
      assertEquals(CalendarNotetaker.EventSelectionType.EXTERNAL, deserialized.notetaker?.rules?.eventSelection?.get(0))
      assertNull(deserialized.notetaker?.rules?.participantFilter?.participantsGte)
      assertEquals(15, deserialized.notetaker?.rules?.participantFilter?.participantsLte)
    }

    @Test
    fun `UpdateCalendarRequest serializes with no fields set`() {
      val adapter = JsonHelper.moshi().adapter(UpdateCalendarRequest::class.java)
      val request = UpdateCalendarRequest()

      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!

      assertNull(deserialized.name)
      assertNull(deserialized.description)
      assertNull(deserialized.location)
      assertNull(deserialized.timezone)
      assertNull(deserialized.metadata)
      assertNull(deserialized.hexColor)
      assertNull(deserialized.hexForegroundColor)
      assertNull(deserialized.notetaker)
    }

    @Test
    fun `UpdateCalendarRequest Builder sets all fields correctly`() {
      val request = UpdateCalendarRequest.Builder()
        .name("My Updated Calendar")
        .description("Updated description")
        .location("New York, NY")
        .timezone("America/New_York")
        .metadata(mapOf("key" to "value"))
        .hexColor("#039BE5")
        .hexForegroundColor("#FFFFFF")
        .notetaker(CalendarNotetaker(name = "Updated Notetaker"))
        .build()

      assertEquals("My Updated Calendar", request.name)
      assertEquals("Updated description", request.description)
      assertEquals("New York, NY", request.location)
      assertEquals("America/New_York", request.timezone)
      assertEquals(mapOf("key" to "value"), request.metadata)
      assertEquals("#039BE5", request.hexColor)
      assertEquals("#FFFFFF", request.hexForegroundColor)
      assertEquals("Updated Notetaker", request.notetaker?.name)
    }

    @Test
    fun `UpdateCalendarRequest Builder builds with no fields set`() {
      val request = UpdateCalendarRequest.Builder().build()

      assertNull(request.name)
      assertNull(request.description)
      assertNull(request.location)
      assertNull(request.timezone)
      assertNull(request.metadata)
      assertNull(request.hexColor)
      assertNull(request.hexForegroundColor)
      assertNull(request.notetaker)
    }
  }

  @Nested
  inner class SpecificTimeAvailabilityTests {
    @Test
    fun `SpecificTimeAvailability deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(SpecificTimeAvailability::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "date": "2026-03-18",
            "start": "09:00",
            "end": "17:00",
            "timezone": "America/Toronto"
          }
        """.trimIndent(),
      )

      val specificTimeAvailability = adapter.fromJson(jsonBuffer)!!
      assertIs<SpecificTimeAvailability>(specificTimeAvailability)
      assertEquals("2026-03-18", specificTimeAvailability.date)
      assertEquals("09:00", specificTimeAvailability.start)
      assertEquals("17:00", specificTimeAvailability.end)
      assertEquals("America/Toronto", specificTimeAvailability.timezone)
    }

    @Test
    fun `SpecificTimeAvailability serializes to JSON correctly`() {
      val adapter = JsonHelper.moshi().adapter(SpecificTimeAvailability::class.java)
      val specificTimeAvailability = SpecificTimeAvailability(
        date = "2026-03-18",
        start = "09:00",
        end = "17:00",
        timezone = "America/Toronto",
      )

      val json = adapter.toJson(specificTimeAvailability)
      assertEquals("""{"date":"2026-03-18","start":"09:00","end":"17:00","timezone":"America/Toronto"}""", json)
    }

    @Test
    fun `SpecificTimeAvailability round-trip serialization`() {
      val adapter = JsonHelper.moshi().adapter(SpecificTimeAvailability::class.java)
      val original = SpecificTimeAvailability(
        date = "2026-03-18",
        start = "09:00",
        end = "17:00",
        timezone = "America/Toronto",
      )

      val json = adapter.toJson(original)
      val deserialized = adapter.fromJson(json)!!
      assertEquals(original.date, deserialized.date)
      assertEquals(original.start, deserialized.start)
      assertEquals(original.end, deserialized.end)
      assertEquals(original.timezone, deserialized.timezone)
    }

    @Test
    fun `SpecificTimeAvailability Builder works correctly`() {
      val specificTimeAvailability = SpecificTimeAvailability.Builder(
        date = "2026-03-18",
        start = "09:00",
        end = "17:00",
        timezone = "America/Toronto",
      ).build()

      assertEquals("2026-03-18", specificTimeAvailability.date)
      assertEquals("09:00", specificTimeAvailability.start)
      assertEquals("17:00", specificTimeAvailability.end)
      assertEquals("America/Toronto", specificTimeAvailability.timezone)
    }

    @Test
    fun `AvailabilityParticipant serializes with specificTimeAvailability and onlySpecificTimeAvailability`() {
      val adapter = JsonHelper.moshi().adapter(AvailabilityParticipant::class.java)
      val participant = AvailabilityParticipant(
        email = "test@nylas.com",
        calendarIds = listOf("primary"),
        specificTimeAvailability = listOf(
          SpecificTimeAvailability(
            date = "2026-03-18",
            start = "09:00",
            end = "17:00",
            timezone = "America/Toronto",
          ),
        ),
        onlySpecificTimeAvailability = true,
      )

      val json = adapter.toJson(participant)
      val deserialized = adapter.fromJson(json)!!
      assertEquals("test@nylas.com", deserialized.email)
      assertEquals(1, deserialized.specificTimeAvailability?.size)
      assertEquals("2026-03-18", deserialized.specificTimeAvailability?.get(0)?.date)
      assertEquals("09:00", deserialized.specificTimeAvailability?.get(0)?.start)
      assertEquals("17:00", deserialized.specificTimeAvailability?.get(0)?.end)
      assertEquals("America/Toronto", deserialized.specificTimeAvailability?.get(0)?.timezone)
      assertEquals(true, deserialized.onlySpecificTimeAvailability)
    }

    @Test
    fun `AvailabilityParticipant serializes without new fields for backward compatibility`() {
      val adapter = JsonHelper.moshi().adapter(AvailabilityParticipant::class.java)
      val participant = AvailabilityParticipant(
        email = "test@nylas.com",
        calendarIds = listOf("calendar-123"),
      )

      val json = adapter.toJson(participant)
      val deserialized = adapter.fromJson(json)!!
      assertEquals("test@nylas.com", deserialized.email)
      assertEquals(null, deserialized.specificTimeAvailability)
      assertEquals(null, deserialized.onlySpecificTimeAvailability)
      assertEquals(null, deserialized.openHours)
    }

    @Test
    fun `AvailabilityParticipant deserializes JSON with all new fields`() {
      val adapter = JsonHelper.moshi().adapter(AvailabilityParticipant::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "email": "test@nylas.com",
            "calendar_ids": ["primary"],
            "specific_time_availability": [
              {
                "date": "2026-03-18",
                "start": "09:00",
                "end": "17:00",
                "timezone": "America/Toronto"
              }
            ],
            "only_specific_time_availability": true
          }
        """.trimIndent(),
      )

      val participant = adapter.fromJson(jsonBuffer)!!
      assertEquals("test@nylas.com", participant.email)
      assertEquals(listOf("primary"), participant.calendarIds)
      assertEquals(1, participant.specificTimeAvailability?.size)
      assertEquals("2026-03-18", participant.specificTimeAvailability?.get(0)?.date)
      assertEquals("09:00", participant.specificTimeAvailability?.get(0)?.start)
      assertEquals("17:00", participant.specificTimeAvailability?.get(0)?.end)
      assertEquals("America/Toronto", participant.specificTimeAvailability?.get(0)?.timezone)
      assertEquals(true, participant.onlySpecificTimeAvailability)
    }

    @Test
    fun `AvailabilityParticipant deserializes JSON without new fields for backward compatibility`() {
      val adapter = JsonHelper.moshi().adapter(AvailabilityParticipant::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "email": "test@nylas.com",
            "calendar_ids": ["calendar-123"]
          }
        """.trimIndent(),
      )

      val participant = adapter.fromJson(jsonBuffer)!!
      assertEquals("test@nylas.com", participant.email)
      assertEquals(null, participant.specificTimeAvailability)
      assertEquals(null, participant.onlySpecificTimeAvailability)
    }

    @Test
    fun `AvailabilityParticipant Builder works with all new fields`() {
      val participant = AvailabilityParticipant.Builder("test@nylas.com")
        .calendarIds(listOf("primary"))
        .specificTimeAvailability(
          listOf(
            SpecificTimeAvailability(
              date = "2026-03-18",
              start = "09:00",
              end = "17:00",
              timezone = "America/Toronto",
            ),
          ),
        )
        .onlySpecificTimeAvailability(true)
        .build()

      assertEquals("test@nylas.com", participant.email)
      assertEquals(listOf("primary"), participant.calendarIds)
      assertEquals(1, participant.specificTimeAvailability?.size)
      assertEquals("2026-03-18", participant.specificTimeAvailability?.get(0)?.date)
      assertEquals("09:00", participant.specificTimeAvailability?.get(0)?.start)
      assertEquals("17:00", participant.specificTimeAvailability?.get(0)?.end)
      assertEquals("America/Toronto", participant.specificTimeAvailability?.get(0)?.timezone)
      assertEquals(true, participant.onlySpecificTimeAvailability)
    }

    @Test
    fun `AvailabilityParticipant Builder works without new fields for backward compatibility`() {
      val participant = AvailabilityParticipant.Builder("test@nylas.com")
        .calendarIds(listOf("calendar-123"))
        .build()

      assertEquals("test@nylas.com", participant.email)
      assertEquals(null, participant.specificTimeAvailability)
      assertEquals(null, participant.onlySpecificTimeAvailability)
    }
  }

  @Nested
  inner class ListCalendarsQueryParamsTests {
    @Test
    fun `ListCalendersQueryParams Builder sets all fields correctly`() {
      val params = ListCalendersQueryParams.Builder()
        .limit(25)
        .pageToken("next-page-token")
        .metadataPair(mapOf("your-key" to "value"))
        .build()

      assertEquals(25, params.limit)
      assertEquals("next-page-token", params.pageToken)
      assertEquals(mapOf("your-key" to "value"), params.metadataPair)
    }

    @Test
    fun `ListCalendersQueryParams Builder builds with all null fields`() {
      val params = ListCalendersQueryParams.Builder().build()

      assertNull(params.limit)
      assertNull(params.pageToken)
      assertNull(params.metadataPair)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var calendars: Calendars

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      calendars = Calendars(mockNylasClient)
    }

    @Test
    fun `listing calendars calls requests with the correct params`() {
      calendars.list(grantId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGetEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Calendar::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `listing calendars passes query params to the request`() {
      val queryParams = ListCalendersQueryParams(
        limit = 25,
        pageToken = "next-page-token",
        metadataPair = mapOf("your-key" to "value"),
      )

      calendars.list(grantId, queryParams)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGetEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars", pathCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a calendar calls requests with the correct params`() {
      val calendarId = "calendar-123"

      calendars.find(grantId, calendarId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGetEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars/$calendarId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Calendar::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a calendar calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateCalendarRequest::class.java)
      val createCalendarRequest = CreateCalendarRequest(
        description = "Description of my new calendar",
        location = "Los Angeles, CA",
        metadata = mapOf("your-key" to "value"),
        name = "My New Calendar",
        timezone = "America/Los_Angeles",
        notetaker = CalendarNotetaker(
          name = "Test Notetaker",
          meetingSettings = CalendarNotetaker.MeetingSettings(
            videoRecording = true,
            audioRecording = true,
            transcription = false,
          ),
          rules = CalendarNotetaker.Rules(
            eventSelection = listOf(CalendarNotetaker.EventSelectionType.INTERNAL),
            participantFilter = CalendarNotetaker.ParticipantFilter(
              participantsGte = 2,
              participantsLte = null,
            ),
          ),
        ),
      )

      calendars.create(grantId, createCalendarRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePostEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Calendar::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createCalendarRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a calendar calls requests with the correct params`() {
      val calendarId = "calendar-123"
      val adapter = JsonHelper.moshi().adapter(UpdateCalendarRequest::class.java)
      val updateCalendarRequest = UpdateCalendarRequest(
        description = "Description of my new calendar",
        location = "Los Angeles, CA",
        metadata = mapOf("your-key" to "value"),
        name = "My New Calendar",
        timezone = "America/Los_Angeles",
        hexColor = "#039BE5",
        hexForegroundColor = "#039BE5",
        notetaker = CalendarNotetaker(
          name = "Updated Notetaker",
          meetingSettings = CalendarNotetaker.MeetingSettings(
            videoRecording = false,
            audioRecording = true,
            transcription = true,
          ),
          rules = CalendarNotetaker.Rules(
            eventSelection = listOf(CalendarNotetaker.EventSelectionType.EXTERNAL, CalendarNotetaker.EventSelectionType.ALL),
            participantFilter = CalendarNotetaker.ParticipantFilter(
              participantsGte = null,
              participantsLte = 15,
            ),
          ),
        ),
      )

      calendars.update(grantId, calendarId, updateCalendarRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePutEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars/$calendarId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Calendar::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateCalendarRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a calendar calls requests with the correct params`() {
      val calendarId = "calendar-123"

      calendars.destroy(grantId, calendarId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDeleteEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars/$calendarId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }

    @Test
    fun `finding a calendar URL-encodes a calendar id containing slashes`() {
      val calendarId = "prefix/cal-123"

      calendars.find(grantId, calendarId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGetEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars/prefix%2Fcal-123", pathCaptor.firstValue)
    }

    @Test
    fun `destroying a calendar URL-encodes a calendar id containing slashes`() {
      val calendarId = "prefix/cal-123"

      calendars.destroy(grantId, calendarId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDeleteEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars/prefix%2Fcal-123", pathCaptor.firstValue)
    }
  }

  @Nested
  inner class OtherMethodTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var calendars: Calendars

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      calendars = Calendars(mockNylasClient)
    }

    @Test
    fun `getting availability calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(GetAvailabilityRequest::class.java)
      val getAvailabilityRequest = GetAvailabilityRequest(
        startTime = 1620000000,
        endTime = 1620000000,
        participants = listOf(
          AvailabilityParticipant(
            email = "test@nylas.com",
            calendarIds = listOf("calendar-123"),
            openHours = listOf(
              OpenHours(
                days = listOf(0, 1, 2),
                start = "09:00",
                end = "17:00",
                timezone = "America/Los_Angeles",
                exdates = listOf("2021-05-03", "2021-05-04"),
              ),
            ),
          ),
        ),
        durationMinutes = 30,
        intervalMinutes = 15,
        roundTo30Minutes = true,
        availabilityRules = AvailabilityRules(
          availabilityMethod = AvailabilityMethod.MAX_AVAILABILITY,
          buffer = MeetingBuffer(
            before = 15,
            after = 15,
          ),
          defaultOpenHours = listOf(
            OpenHours(
              days = listOf(0, 1, 2),
              start = "09:00",
              end = "17:00",
              timezone = "America/Los_Angeles",
              exdates = listOf("2021-05-03", "2021-05-04"),
            ),
          ),
          roundRobinGroupId = "event-123",
        ),
      )

      calendars.getAvailability(getAvailabilityRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePostEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/calendars/availability", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, GetAvailabilityResponse::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(getAvailabilityRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `getting availability with roundTo calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(GetAvailabilityRequest::class.java)
      val getAvailabilityRequest = GetAvailabilityRequest(
        startTime = 1690862400,
        endTime = 1691208000,
        participants = listOf(
          AvailabilityParticipant(
            email = "test@nylas.com",
            calendarIds = listOf("primary"),
          ),
        ),
        durationMinutes = 30,
        intervalMinutes = 30,
        roundTo = 15,
      )

      calendars.getAvailability(getAvailabilityRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePostEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/calendars/availability", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, GetAvailabilityResponse::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(getAvailabilityRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `getting availability with specificTimeAvailability calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(GetAvailabilityRequest::class.java)
      val getAvailabilityRequest = GetAvailabilityRequest(
        startTime = 1773248400,
        endTime = 1774458000,
        participants = listOf(
          AvailabilityParticipant(
            email = "teotoplak95@gmail.com",
            calendarIds = listOf("primary"),
            specificTimeAvailability = listOf(
              SpecificTimeAvailability(
                date = "2026-03-18",
                start = "09:00",
                end = "17:00",
                timezone = "America/Toronto",
              ),
            ),
            onlySpecificTimeAvailability = true,
          ),
        ),
        durationMinutes = 30,
      )

      calendars.getAvailability(getAvailabilityRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePostEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/calendars/availability", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, GetAvailabilityResponse::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(getAvailabilityRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `getting free busy calls requests with the correct params`() {
      val grantId = "abc-123-grant-id"
      val adapter = JsonHelper.moshi().adapter(GetFreeBusyRequest::class.java)
      val getFreeBusyRequest = GetFreeBusyRequest(
        startTime = 1620000000,
        endTime = 1620000000,
        emails = listOf("test@nylas.com"),
        tentativeAsBusy = true,
      )

      calendars.getFreeBusy(grantId, getFreeBusyRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePostEncoded<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      val listOfFreeBusy = Types.newParameterizedType(List::class.java, GetFreeBusyResponse::class.java)
      assertEquals("v3/grants/$grantId/calendars/free-busy", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, listOfFreeBusy), typeCaptor.firstValue)
      assertEquals(adapter.toJson(getFreeBusyRequest), requestBodyCaptor.firstValue)
    }
  }
}
