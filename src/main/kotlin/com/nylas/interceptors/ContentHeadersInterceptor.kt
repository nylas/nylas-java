package com.nylas.interceptors

import com.nylas.NylasClient
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ContentHeadersInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url().encodedPath()
        val contentHeader = request.header(NylasClient.HttpHeaders.CONTENT_TYPE.headerName)
        if (contentHeader == null && !isDownloadablePath(path)) {
            val enhancedRequest = request
                .newBuilder()
                .header(
                    NylasClient.HttpHeaders.CONTENT_TYPE.headerName,
                    NylasClient.MediaType.APPLICATION_JSON.mediaType
                )
                .header(NylasClient.HttpHeaders.ACCEPT.headerName, NylasClient.MediaType.APPLICATION_JSON.mediaType)
                .build()
            return chain.proceed(enhancedRequest)
        }
        return chain.proceed(request)
    }

    private fun isDownloadablePath(path: String): Boolean {
        return path == "%2Fcontacts%2Fpicture" || path == "%2Ffiles%2Fdownload" || path == "%2Fdelta%2Fstreaming"
    }
}
