package com.nylas.models

import com.nylas.util.JsonHelper
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

/**
 * Class representation of a Nylas get free busy response.
 * It can be either a [FreeBusy] or a [FreeBusyError].
 */
sealed class GetFreeBusyResponse {
  /**
   * The type of the response.
   */
  @Json(name = "object")
  protected open val obj: FreeBusyType? = null

  /**
   * The participant's email address.
   */
  @Json(name = "email")
  abstract val email: String

  /**
   * This class represents a successful free-busy response.
   */
  data class FreeBusy(
    /**
     * A list of busy time slots.
     */
    @Json(name = "time_slots")
    val timeSlots: List<FreeBusyTimeSlot>,
    @Json(name = "object")
    override val obj: FreeBusyType = FreeBusyType.FREE_BUSY,
    @Json(name = "email")
    override val email: String,
  ) : GetFreeBusyResponse()

  /**
   * This class represents a failed free-busy response.
   */
  data class FreeBusyError(
    /**
     * Description of the error fetching data for this participant.
     */
    @Json(name = "error")
    val error: String,
    @Json(name = "object")
    override val obj: FreeBusyType = FreeBusyType.ERROR,
    @Json(name = "email")
    override val email: String,
  ) : GetFreeBusyResponse()

  /**
   * Returns the type of object.
   * @return The type of object.
   */
  fun getObject() = obj

  companion object {
    /**
     * A JsonAdapter factory for the [GetFreeBusyResponse] sealed class (used for deserialization).
     * @suppress Not for public use.
     */
    @JvmStatic
    val FREE_BUSY_JSON_FACTORY: JsonAdapter.Factory = PolymorphicJsonAdapterFactory.of(GetFreeBusyResponse::class.java, "object")
      .withSubtype(FreeBusy::class.java, FreeBusyType.FREE_BUSY.value)
      .withSubtype(FreeBusyError::class.java, FreeBusyType.ERROR.value)

    /**
     * A JsonAdapter for the [GetFreeBusyResponse] sealed class (used for serialization).
     * @suppress Not for public use.
     */
    @JvmStatic
    val GET_FREE_BUSY_RESPONSE_ADAPTER = JsonHelper.listTypeOf(GetFreeBusyResponse::class.java)
  }
}
