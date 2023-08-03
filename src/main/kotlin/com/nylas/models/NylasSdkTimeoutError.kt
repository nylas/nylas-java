package com.nylas.models

class NylasSdkTimeoutError(
  url: String,
  timeout: Int,
) : AbstractNylasSdkError("Nylas SDK timed out before receiving a response from the server.")
