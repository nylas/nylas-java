package com.nylas.models

/**
 * Error thrown when the Nylas SDK times out before receiving a response from the server.
 * @param url The URL that timed out.
 * @param timeout The timeout value set in the Nylas SDK, in seconds.
 */
class NylasSdkTimeoutError(
  url: String,
  timeout: Int,
) : AbstractNylasSdkError("Nylas SDK timed out before receiving a response from the server.")
