package com.nylas.models

import kotlin.test.Test
import kotlin.test.assertEquals

class RequestOverridesTests {
  @Test
  fun `public data class ABI remains four fields`() {
    val constructor = RequestOverrides::class.java.getConstructor(
      String::class.java,
      String::class.java,
      java.lang.Long::class.java,
      Map::class.java,
    )

    val overrides = constructor.newInstance(
      "api-key",
      "https://api.test.nylas.com",
      30L,
      mapOf("X-Test" to "true"),
    )

    assertEquals("api-key", overrides.apiKey)
    assertEquals("https://api.test.nylas.com", overrides.apiUri)
    assertEquals(30L, overrides.timeout)
    assertEquals(mapOf("X-Test" to "true"), overrides.headers)
    assertEquals(false, overrides.omitAuthorization)

    RequestOverrides::class.java.getDeclaredMethod(
      "copy",
      String::class.java,
      String::class.java,
      java.lang.Long::class.java,
      Map::class.java,
    )
    assertEquals(
      false,
      RequestOverrides::class.java.declaredConstructors.any { declaredConstructor ->
        declaredConstructor.parameterTypes.lastOrNull() == java.lang.Boolean.TYPE
      },
    )
  }
}
