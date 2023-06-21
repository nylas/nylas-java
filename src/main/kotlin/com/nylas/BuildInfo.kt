package com.nylas

import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.util.*

object BuildInfo {
  private val log = LoggerFactory.getLogger(BuildInfo::class.java)
  var VERSION: String? = null
  private var COMMIT_ID: String? = null
  private var TIMESTAMP: String? = null

  init {
    var version: String? = null
    var commitId: String? = null
    var timestamp: String? = null
    try {
      // First, look for build.properties file left by gradle
      BuildInfo::class.java.getResourceAsStream("/build.properties").use { `in` ->
        if (`in` != null) {
          val props = Properties()
          props.load(`in`)
          version = props.getProperty("version")
          commitId = props.getProperty("commit.hash")
          timestamp = props.getProperty("build.timestamp")
        }
      }

      // Otherwise, look directly for gradle.properties file (e.g. in an IDE)
      if (version == null || version!!.isEmpty()) {
        FileInputStream("gradle.properties").use { `in` ->
          val props = Properties()
          props.load(`in`)
          version = props.getProperty("version")
        }
      }
    } catch (t: Throwable) {
      log.error("Failed to load build info", t)
    }
    VERSION = version
    COMMIT_ID = commitId
    TIMESTAMP = timestamp
  }
}
