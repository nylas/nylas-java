package com.nylas.util

import com.nylas.models.CreateAttachmentRequest
import com.nylas.models.IMessageAttachmentRequest
import com.nylas.util.FileUtils.Companion.toStreamingRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

class FileUtils {
  companion object {
    /**
     * Converts an [InputStream] into a streaming [RequestBody] for use with [okhttp3] requests.
     *
     * This method is optimized for memory efficiency by streaming data directly
     * from the source [InputStream] to OkHttp's internal handling, without
     * loading the entire content into memory first.
     *
     * @param contentType The media type of the request body. Defaults to `null`.
     * @return A [RequestBody] that streams content directly from the provided [InputStream].
     *
     * @throws IOException if there's an error while trying to access the content length.
     *
     * @suppress Not for public use.
     */
    @JvmStatic
    fun InputStream.toStreamingRequestBody(contentType: MediaType? = null): RequestBody {
      return object : RequestBody() {
        override fun contentType() = contentType

        override fun writeTo(sink: BufferedSink) {
          source().use { source ->
            sink.writeAll(source)
          }
        }

        override fun contentLength(): Long {
          return try {
            this@toStreamingRequestBody.available().toLong()
          } catch (e: IOException) {
            -1
          }
        }
      }
    }

    /**
     * Generates a [CreateAttachmentRequest] from a file path.
     * @param filePath The path to the file to upload.
     * @return A [CreateAttachmentRequest] that can be used to upload the file.
     */
    @JvmStatic
    fun createFileRequestBuilder(filePath: String): CreateAttachmentRequest {
      val path = Paths.get(filePath)
      val filename = path.fileName.toString()
      val contentType = Files.probeContentType(path) ?: "application/octet-stream"
      val content = Files.newInputStream(path)
      val size = Files.size(path)

      return CreateAttachmentRequest(
        filename = filename,
        contentType = contentType,
        size = size.toInt(),
        content = content,
      )
    }

    @JvmStatic
    fun <T : IMessageAttachmentRequest> buildFormRequest(requestBody: T, attachmentLessPayload: String): MultipartBody {
      val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
      multipartBuilder.addFormDataPart("message", attachmentLessPayload)

      // Add a separate form field for each attachment
      requestBody.attachments?.forEachIndexed { index, attachment ->
        val contentType = MediaType.parse(attachment.contentType)
        val contentBody = attachment.content.toStreamingRequestBody(contentType)
        multipartBuilder.addFormDataPart("file$index", attachment.filename, contentBody)
      }

      return multipartBuilder.build()
    }
  }
}
