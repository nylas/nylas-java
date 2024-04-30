package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import java.net.URI
import java.util.*

/**
 * Nylas Webhooks API
 *
 * The Nylas Webhooks API allows your application to receive notifications in real-time when certain events occur.
 */
class Webhooks(client: NylasClient) : Resource<Webhook>(client, Webhook::class.java) {
  /**
   * List all webhook destinations for the application
   * @param overrides Optional request overrides to apply
   * @return The list of webhook destinations
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(overrides: RequestOverrides? = null): ListResponse<Webhook> {
    val path = "v3/webhooks"
    return listResource(path, overrides = overrides)
  }

  /**
   * Return a webhook destination
   * @param webhookId The id of the webhook destination to retrieve.
   * @param overrides Optional request overrides to apply
   * @return The webhook destination
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(webhookId: String, overrides: RequestOverrides? = null): Response<Webhook> {
    val path = String.format("v3/webhooks/%s", webhookId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a webhook destination
   * @param requestBody The values to create the webhook destination with
   * @param overrides Optional request overrides to apply
   * @return The created webhook destination
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreateWebhookRequest, overrides: RequestOverrides? = null): Response<WebhookWithSecret> {
    val path = "v3/webhooks"
    val adapter = JsonHelper.moshi().adapter(CreateWebhookRequest::class.java)
    val responseType = Types.newParameterizedType(Response::class.java, WebhookWithSecret::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a webhook destination
   * @param webhookId The id of the webhook destination to update.
   * @param requestBody The values to update the webhook destination with
   * @param overrides Optional request overrides to apply
   * @return The updated webhook destination
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(webhookId: String, requestBody: UpdateWebhookRequest, overrides: RequestOverrides? = null): Response<Webhook> {
    val path = String.format("v3/webhooks/%s", webhookId)
    val adapter = JsonHelper.moshi().adapter(UpdateWebhookRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a webhook destination
   * @param webhookId The id of the webhook destination to delete.
   * @param overrides Optional request overrides to apply
   * @return The deleted webhook response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(webhookId: String, overrides: RequestOverrides? = null): WebhookDeleteResponse {
    val path = String.format("v3/webhooks/%s", webhookId)
    return client.executeDelete(path, WebhookDeleteResponse::class.java, overrides = overrides)
  }

  /**
   * Update the webhook secret value for a destination
   * @param webhookId The id of the webhook destination to update the secret for.
   * @param overrides Optional request overrides to apply
   * @returns The updated webhook destination
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun rotateSecret(webhookId: String, overrides: RequestOverrides? = null): Response<WebhookWithSecret> {
    val path = String.format("v3/webhooks/rotate-secret/%s", webhookId)
    val responseType = Types.newParameterizedType(Response::class.java, WebhookWithSecret::class.java)
    return client.executePost(path, responseType, overrides = overrides)
  }

  /**
   * Get the current list of IP addresses that Nylas sends webhooks from
   * @param overrides Optional request overrides to apply
   * @returns The list of IP addresses that Nylas sends webhooks from
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun ipAddresses(overrides: RequestOverrides? = null): Response<WebhookIpAddressesResponse> {
    val path = "v3/webhooks/ip-addresses"
    val responseType = Types.newParameterizedType(Response::class.java, WebhookIpAddressesResponse::class.java)
    return client.executeGet(path, responseType, overrides = overrides)
  }

  /**
   * Extract the challenge parameter from a URL
   * @param url The URL sent by Nylas containing the challenge parameter
   * @returns The challenge parameter
   */
  @Throws(IllegalArgumentException::class)
  fun extractChallengeParameter(url: String): String {
    val uri = URI(url)
    val parameters = uri.rawQuery?.split("&")?.associate {
      it.split("=").let { pair -> pair[0] to pair[1] }
    }

    val challengeParameter = parameters?.get("challenge")
    if (challengeParameter.isNullOrEmpty()) {
      throw IllegalArgumentException("Invalid URL or no challenge parameter found.")
    }

    return challengeParameter
  }
}
