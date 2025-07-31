package com.nylas.interceptors

import okhttp3.*
import okio.Buffer
import okio.GzipSource
import org.slf4j.LoggerFactory
import java.io.EOFException
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * OkHttp Interceptor which provides 3 loggers for HTTP requests/responses:
 * <dl>
 * <dt>com.nylas.http.Summary</dt><dd>logs one line for each request, containing method, URI, and content size
 * and one line for each response containing status code, message, content size and duration.</dd>
 * <dt>com.nylas.http.Headers</dt><dd>logs request and response headers (except Authorization value by default).</dd>
 * <dt>com.nylas.http.Body</dt><dd>logs request and response bodies (only the first 10kB by default).</dd>
 * </dl>
 * @suppress Not for public use.
 */
class HttpLoggingInterceptor : Interceptor {
  private var isLogAuthHeader = false
  private var maxBodyBytesToLog = 10000

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    logRequest(request)
    val startNanos = System.nanoTime()
    val response = try {
      chain.proceed(request)
    } catch (t: Throwable) {
      if (requestLogs.isDebugEnabled) {
        requestLogs.debug("<= Exception=$t")
      }
      throw t
    }
    val durationMillis = (System.nanoTime() - startNanos) / 1000000
    logResponse(response, durationMillis)
    return response
  }

  @Throws(IOException::class)
  private fun logRequest(request: Request) {
    val requestBody = request.body
    val hasBody = requestBody != null

    // Summary
    if (requestLogs.isDebugEnabled) {
      requestLogs.debug(
        "=> " + request.method +
          " " + request.url +
          " reqBodySize=" + if (hasBody) requestBody!!.contentLength() else 0,
      )
    }
    logHeaders("=>", request.headers)
    if (bodyLogs.isDebugEnabled) {
      val message = if (!hasBody) {
        " No request body"
      } else {
        val contentType = requestBody!!.contentType()
        if (!isPrintableMediaType(contentType)) {
          " Skipped logging request body of type that may not be printable: $contentType"
        } else {
          val buf = Buffer()
          requestBody.writeTo(buf)
          bodyBufferToString("", buf, contentType)
        }
      }
      bodyLogs.debug("=>$message")
    }
  }

  @Throws(IOException::class)
  private fun logResponse(response: Response?, durationMillis: Long) {
    val contentLength = getContentLength(response)
    // Summary
    if (requestLogs.isDebugEnabled) {
      requestLogs.debug(
        "<= " + response!!.code +
          " " + response.message +
          " resBodySize=" + contentLength +
          " durationMs=" + durationMillis,
      )
    }
    logHeaders("<=", response!!.headers)
    if (bodyLogs.isDebugEnabled) {
      val message: String
      if (contentLength == -1L) {
        message = " No response body"
      } else {
        val contentType = response.body!!.contentType()
        if (!isPrintableMediaType(contentType)) {
          message = " Skipped logging response body of type that may not be printable: $contentType"
        } else {
          val source = response.body!!.source()
          source.request(Long.MAX_VALUE) // if zipped, may need to buffer all of it
          var buf = source.buffer.clone()
          var gzippedMessage = ""
          if ("gzip".equals(response.headers["Content-Encoding"], ignoreCase = true)) {
            val gzippedSize = buf.size
            GzipSource(buf).use { gzippedResponseBody ->
              buf = Buffer()
              buf.writeAll(gzippedResponseBody)
            }
            gzippedMessage = " (decompressed " + gzippedSize + " bytes to " + buf.size + " bytes)"
          }
          message = bodyBufferToString(gzippedMessage, buf, contentType)
        }
      }
      bodyLogs.debug("<=$message")
    }
  }

  private fun logHeaders(direction: String, headers: Headers) {
    if (headersLogs.isDebugEnabled) {
      val headersLog = StringBuilder().append(direction).append("\n")
      for (i in 0 until headers.size) {
        val name = headers.name(i)
        var value = headers.value(i)
        if (!isLogAuthHeader && "Authorization" == name) {
          value = "<not logged>"
        }
        headersLog.append("  ").append(name).append(": ").append(value).append("\n")
      }
      headersLog.deleteCharAt(headersLog.length - 1)
      headersLogs.debug(headersLog.toString())
    }
  }

  private fun isPrintableMediaType(type: MediaType?): Boolean {
    return (
      type != null &&
        ("text" == type.type || type.toString().startsWith("application/json"))
      )
  }

  @Throws(EOFException::class)
  private fun bodyBufferToString(prefix: String, buf: Buffer, contentType: MediaType?): String {
    var bytesToLog = buf.size
    var truncationMessage = ""
    if (bytesToLog > maxBodyBytesToLog) {
      bytesToLog = maxBodyBytesToLog.toLong()
      truncationMessage = """
(truncated ${buf.size} byte body after $bytesToLog bytes)"""
    }
    val charset = contentType!!.charset(StandardCharsets.UTF_8)
    val str =
      """
        $prefix
        ${buf.readString(bytesToLog, charset!!)}$truncationMessage
      """.trimIndent()

    return str
  }

  private fun getContentLength(response: Response?): Long {
    return if (response!!.body == null) {
      -1
    } else {
      response.body!!.contentLength()
    }
  }

  companion object {
    private val requestLogs = LoggerFactory.getLogger("com.nylas.http.Summary")
    private val headersLogs = LoggerFactory.getLogger("com.nylas.http.Headers")
    private val bodyLogs = LoggerFactory.getLogger("com.nylas.http.Body")
  }
}
