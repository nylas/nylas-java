package com.nylas.models

/**
 * Represents a field that can be explicitly set to null (to clear its value on the server)
 * or left absent from the request payload entirely.
 *
 * - Leave the field as Kotlin null to omit it from serialization (no change to server value).
 * - Use [Clear] to send `null` explicitly and clear the server-side value.
 * - Use [Value] to send a specific value.
 */
sealed class NullableField<out T> {
  /** Serialize the field as JSON null, signalling the server to clear the value. */
  object Clear : NullableField<Nothing>()

  /** Serialize the field with the given value. */
  data class Value<T>(val v: T) : NullableField<T>()
}
