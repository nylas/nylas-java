package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

/**
 * Nylas Bookings API
 *
 * The Nylas Bookings API allows you to create new bookings or manage existing ones, as well as getting
 * bookings details for a user.
 */
class Bookings(client: NylasClient) : Resource<Booking>(client, Booking::class.java) {
  /**
   * Find a booking
   * @param bookingId The ID of the booking to find.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The Booking object
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(
    bookingId: String,
    queryParams: FindBookingQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): Response<Booking> {
    val path = String.format("v3/scheduling/bookings/%s", bookingId)
    return findResource(path, queryParams, overrides = overrides)
  }

  /**
   * Create a booking
   * @param requestBody The data to create the booking with.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The Booking object
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(
    requestBody: CreateBookingRequest,
    queryParams: CreateBookingQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): Response<Booking> {
    val path = "v3/scheduling/bookings"
    val adapter = JsonHelper.moshi().adapter(CreateBookingRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, queryParams, overrides = overrides)
  }

  /**
   * Confirm a booking
   * @param bookingId The ID of the booking to update.
   * @param requestBody The data to update the booking with.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The Booking object
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun confirm(
    bookingId: String,
    requestBody: ConfirmBookingRequest,
    queryParams: ConfirmBookingQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): Response<Booking> {
    val path = String.format("v3/scheduling/bookings/%s", bookingId)
    val adapter = JsonHelper.moshi().adapter(ConfirmBookingRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, queryParams, overrides = overrides)
  }

  /**
   * Reschedule a booking
   * @param bookingId The ID of the booking to update.
   * @param requestBody The data to update the booking with.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The Booking object
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun reschedule(
    bookingId: String,
    requestBody: RescheduleBookingRequest,
    queryParams: RescheduleBookingQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): Response<Booking> {
    val path = String.format("v3/scheduling/bookings/%s", bookingId)
    val adapter = JsonHelper.moshi().adapter(RescheduleBookingRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return patchResource(path, serializedRequestBody, queryParams, overrides = overrides)
  }

  /**
   * Destroy a booking
   * @param bookingId The ID of the booking to destroy.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The DeleteResponse object
   */
  fun destroy(bookingId: String, queryParams: DestroyBookingQueryParams? = null, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/scheduling/bookings/%s", bookingId)
    return destroyResource(path, queryParams, overrides = overrides)
  }
}
