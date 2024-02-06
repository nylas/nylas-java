package com.nylas

import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.util.*

/**
 * @suppress Not for public use.
 */
object BuildInfo {
  private val log = LoggerFactory.getLogger(BuildInfo::class.java)
  var VERSION: String? = null

  init {
    try {
      // First, look for build.properties file left by gradle
      BuildInfo::class.java.getResourceAsStream("/build.properties").use { `in` ->
        if (`in` != null) {
          val props = Properties()
          props.load(`in`)
          VERSION = props.getProperty("nylas.build.version")
        }
      }

      // Otherwise, look directly for gradle.properties file (e.g. in an IDE)
      if (VERSION == null || VERSION!!.isEmpty()) {
        FileInputStream("gradle.properties").use { `in` ->
          val props = Properties()
          props.load(`in`)
          VERSION = props.getProperty("version")
        }
      }
    } catch (t: Throwable) {
      log.error("Failed to load build info", t)
    }
  }
}
