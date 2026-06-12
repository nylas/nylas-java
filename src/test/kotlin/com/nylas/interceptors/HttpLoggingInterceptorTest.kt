package com.nylas.interceptors

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HttpLoggingInterceptorTest {
  @Test
  fun `redacts service account signature headers`() {
    assertTrue(HttpLoggingInterceptor.shouldRedactHeader("X-Nylas-Signature"))
    assertTrue(HttpLoggingInterceptor.shouldRedactHeader("x-nylas-signature"))
    assertFalse(HttpLoggingInterceptor.shouldRedactHeader("X-Nylas-Kid"))
    assertFalse(HttpLoggingInterceptor.shouldRedactHeader("X-Nylas-Nonce"))
  }
}
