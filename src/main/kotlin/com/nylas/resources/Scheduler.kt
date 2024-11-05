package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.resources.configurations.Configurations

/**
 * Nylas Scheduler API
 *
 * The Nylas Scheduler API allows you to manage scheduling configurations, bookings, and sessions.
 *
 * @param client The configured Nylas API client
 */
class Scheduler(private val client: NylasClient) {

  /**
   * Access the Configurations API.
   *
   * @return The Configurations API.
   */
  fun configurations(): Configurations {
    return Configurations(client)
  }
}
