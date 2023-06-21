package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.ApplicationDetails
import com.nylas.models.NylasApiError
import com.nylas.models.Response
import com.squareup.moshi.Types
import java.io.IOException

class Applications(private val client: NylasClient) {
  fun redirectUris(): RedirectUris {
    return RedirectUris(client)
  }

  @Throws(IOException::class, NylasApiError::class)
  fun get(): Response<ApplicationDetails> {
    val path = "/v3/applications"
    val responseType = Types.newParameterizedType(Response::class.java, ApplicationDetails::class.java)
    return client.executeGet(path, responseType)
  }
}