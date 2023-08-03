package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.squareup.moshi.Types
import java.lang.reflect.Type

/**
 * Base class for Nylas API resources
 *
 * @suppress No public constructor or functions
 */
abstract class Resource<T> protected constructor(
  private val client: NylasClient,
  modelClass: Class<T>,
) {
  private val responseType: Type
  private val listResponseType: Type
  init {
    responseType = Types.newParameterizedType(Response::class.java, modelClass)
    listResponseType = Types.newParameterizedType(ListResponse::class.java, modelClass)
  }

  @Throws(AbstractNylasApiError::class)
  protected fun listResource(path: String, queryParams: IQueryParams? = null): ListResponse<T> {
    return client.executeGet(path, listResponseType, queryParams)
  }

  @Throws(AbstractNylasApiError::class)
  protected fun findResource(path: String, queryParams: IQueryParams? = null): Response<T> {
    return client.executeGet(path, responseType, queryParams)
  }

  @Throws(AbstractNylasApiError::class)
  protected fun createResource(path: String, requestBody: String?, queryParams: IQueryParams? = null): Response<T> {
    return client.executePost(path, responseType, requestBody, queryParams)
  }

  @Throws(AbstractNylasApiError::class)
  protected fun updateResource(path: String, requestBody: String?, queryParams: IQueryParams? = null): Response<T> {
    return client.executePut(path, responseType, requestBody, queryParams)
  }

  @Throws(AbstractNylasApiError::class)
  protected fun patchResource(path: String, requestBody: String?, queryParams: IQueryParams? = null): Response<T> {
    return client.executePatch(path, responseType, requestBody, queryParams)
  }

  @Throws(AbstractNylasApiError::class)
  protected fun destroyResource(path: String, queryParams: IQueryParams? = null): DeleteResponse {
    return client.executeDelete(path, DeleteResponse::class.java, queryParams)
  }
}
