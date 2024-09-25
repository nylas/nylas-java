package com.nylas.models

/**
 * Error thrown when the Nylas API closes the connection before the Nylas SDK receives a response.
 * @param url The URL that timed out.
 * @param originalErrorMessage The error message from the library that closed the connection.
 */
class NylasSdkRemoteClosedError(
  url: String,
  originalErrorMessage: String,
) : AbstractNylasSdkError("Nylas API closed the connection before the Nylas SDK received a response.")
