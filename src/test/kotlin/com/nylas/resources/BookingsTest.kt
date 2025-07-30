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

class BookingsTest {
  private val mockHttpClient: OkHttpClient = Mockito.mock(OkHttpClient::class.java)
  private val mockCall: Call = Mockito.mock(Call::class.java)
  private val mockResponse: okhttp3.Response = Mockito.mock(okhttp3.Response::class.java)
  private val mockResponseBody: ResponseBody = Mockito.mock(ResponseBody::class.java)
  private val mockOkHttpClientBuilder: OkHttpClient.Builder = Mockito.mock()

  @BeforeEach
  fun setUp() {
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
    fun `Booking serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Booking::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
      {
        "booking_id": "booking-123",
        "event_id": "event-123",
        "title": "My test event",
        "organizer": {
          "name": "John Doe",
          "email": "user@example.com"
        },
        "status": "booked",
        "description": "This is an example of a description."
      }
        """.trimIndent(),
      )

      val booking = adapter.fromJson(jsonBuffer)
      assertIs<Booking>(booking)
      assertEquals("booking-123", booking.bookingId)
      assertEquals("event-123", booking.eventId)
      assertEquals("My test event", booking.title)
      assertEquals("John Doe", booking.organizer.name)
      assertEquals("user@example.com", booking.organizer.email)
      assertEquals(BookingStatus.BOOKED, booking.status)
      assertEquals("This is an example of a description.", booking.description)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var bookings: Bookings

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      bookings = Bookings(mockNylasClient)
    }

    @Test
    fun `creating a booking calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateBookingRequest::class.java)
      val participants = ArrayList<BookingParticipant>()
      val bookingParticipant = BookingParticipant("test@nylas.com")
      participants.add(bookingParticipant)

      val bookingGuest = BookingGuest("guest@nylas.com", "Guest")
      val startTime = "1630000000"
      val endTime = "1630003600"

      val createBookingRequest = CreateBookingRequest(
        startTime = startTime,
        endTime = endTime,
        participants = participants,
        guest = bookingGuest,
      )

      bookings.create(createBookingRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<CreateBookingQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Booking>(pathCaptor.capture(), typeCaptor.capture(), requestBodyCaptor.capture(), queryParamCaptor.capture(), overrideParamCaptor.capture())

      assertEquals("v3/scheduling/bookings", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Booking::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createBookingRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `finding a booking calls requests with the correct params`() {
      val bookingId = "booking-id"
      bookings.find(bookingId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<FindBookingQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Booking>(pathCaptor.capture(), typeCaptor.capture(), queryParamCaptor.capture(), overrideParamCaptor.capture())

      assertEquals("v3/scheduling/bookings/$bookingId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Booking::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `rescheduling a booking calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(RescheduleBookingRequest::class.java)
      val bookingId = "booking-id"
      val rescheduleBookingRequest = RescheduleBookingRequest(
        startTime = 1630000000,
        endTime = 1630003600,
      )
      bookings.reschedule(bookingId, rescheduleBookingRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<RescheduleBookingQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePatch<Booking>(pathCaptor.capture(), typeCaptor.capture(), requestBodyCaptor.capture(), queryParamCaptor.capture(), overrideParamCaptor.capture())

      assertEquals("v3/scheduling/bookings/$bookingId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Booking::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(rescheduleBookingRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `confirming a booking calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(ConfirmBookingRequest::class.java)
      val bookingId = "booking-id"
      val confirmBookingRequest = ConfirmBookingRequest(
        salt = "salt123",
        status = ConfirmBookingStatus.CANCELLED,
      )

      bookings.confirm(bookingId, confirmBookingRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ConfirmBookingQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Booking>(pathCaptor.capture(), typeCaptor.capture(), requestBodyCaptor.capture(), queryParamCaptor.capture(), overrideParamCaptor.capture())

      assertEquals("v3/scheduling/bookings/$bookingId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Booking::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(confirmBookingRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destryoing a booking calls request with the correct params`() {
      val bookingId = "booking-id"
      bookings.destroy(bookingId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<DestroyBookingQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(pathCaptor.capture(), typeCaptor.capture(), queryParamCaptor.capture(), overrideParamCaptor.capture())

      assertEquals("v3/scheduling/bookings/$bookingId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }
}
