package com.nylas.resources

import com.nylas.NylasClient

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
  fun configurations(): Configurations = Configurations(client)

  /**
   * Access the Sessions API.
   *
   * @return The Sessions API.
   */
  fun sessions(): Sessions = Sessions(client)
}
