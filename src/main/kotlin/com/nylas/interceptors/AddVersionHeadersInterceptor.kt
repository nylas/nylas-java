package com.nylas.interceptors

import com.nylas.BuildInfo
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @suppress Not for public use.
 */
class AddVersionHeadersInterceptor : Interceptor {
  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val requestBuilder = chain.request().newBuilder()
      .header("User-Agent", USER_AGENT)
      .header("Accept-Encoding", "gzip")
    return chain.proceed(requestBuilder.build())
  }

  companion object {
    private val USER_AGENT = "Nylas Java SDK " + (BuildInfo.VERSION ?: "unknown")
  }
}
