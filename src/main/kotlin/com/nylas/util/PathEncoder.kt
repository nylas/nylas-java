package com.nylas.util

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

object PathEncoder {
  fun encode(segment: String): String =
    try {
      URLEncoder.encode(segment, "UTF-8").replace("+", "%20")
    } catch (e: UnsupportedEncodingException) {
      throw RuntimeException(e)
    }
}
