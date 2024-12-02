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

class CalendarsTest {
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
            "timezone": "America/Los_Angeles"
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
      verify(mockNylasClient).executeGet<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Calendar::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `finding a calendar calls requests with the correct params`() {
      val calendarId = "calendar-123"

      calendars.find(grantId, calendarId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars/$calendarId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Calendar::class.java), typeCaptor.firstValue)
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
      )

      calendars.create(grantId, createCalendarRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<ListResponse<Calendar>>(
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
      )

      calendars.update(grantId, calendarId, updateCalendarRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<ListResponse<Calendar>>(
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
      verify(mockNylasClient).executeDelete<ListResponse<Calendar>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/calendars/$calendarId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
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
      verify(mockNylasClient).executePost<ListResponse<Calendar>>(
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
      )

      calendars.getFreeBusy(grantId, getFreeBusyRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListCalendersQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<ListResponse<Calendar>>(
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
