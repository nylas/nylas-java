package com.nylas.models

/**
 * Class representation of the query parameters for listing grants.
 */
data class ListGrantsQueryParams(
  /**
   * The maximum number of objects to return.
   * This field defaults to 10. The maximum allowed value is 200.
   */
  val limit: Int? = null,
  /**
   * Offset grant results by this number.
   */
  val offset: Int? = null,
  /**
   * Sort entries by field name
   */
  val sortBy: String? = null,
  /**
   * Specify ascending or descending order.
   */
  val orderBy: String? = null,
  /**
   * Scope grants from a specific point in time by Unix timestamp.
   */
  val since: Int? = null,
  /**
   * Scope grants to a specific point in time by Unix timestamp.
   */
  val before: Int? = null,
  /**
   * Filtering your query based on grant email address (if applicable)
   */
  val email: String? = null,
  /**
   * Filtering your query based on grant email status (if applicable)
   */
  val grantStatus: String? = null,
  /**
   * Filtering your query based on grant IP address
   */
  val ip: String? = null,
  /**
   * Filtering your query based on OAuth provider
   */
  val provider: AuthProvider? = null,
) : IQueryParams {
  /**
   * Builder for [ListGrantsQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var offset: Int? = null
    private var sortBy: String? = null
    private var orderBy: String? = null
    private var since: Int? = null
    private var before: Int? = null
    private var email: String? = null
    private var grantStatus: String? = null
    private var ip: String? = null
    private var provider: AuthProvider? = null

    /**
     * Sets the maximum number of objects to return.
     * This field defaults to 10. The maximum allowed value is 200.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int?) = apply { this.limit = limit }

    /**
     * Sets the offset grant results by this number.
     * @param offset The offset grant results by this number.
     * @return The builder.
     */
    fun offset(offset: Int?) = apply { this.offset = offset }

    /**
     * Sets the sort entries by field name.
     * @param sortBy The sort entries by field name.
     * @return The builder.
     */
    fun sortBy(sortBy: String?) = apply { this.sortBy = sortBy }

    /**
     * Sets the specify ascending or descending order.
     * @param orderBy The specify ascending or descending order.
     * @return The builder.
     */
    fun orderBy(orderBy: String?) = apply { this.orderBy = orderBy }

    /**
     * Sets the scope grants from a specific point in time by Unix timestamp.
     * @param since The scope grants from a specific point in time by Unix timestamp.
     * @return The builder.
     */
    fun since(since: Int?) = apply { this.since = since }

    /**
     * Sets the scope grants to a specific point in time by Unix timestamp.
     * @param before The scope grants to a specific point in time by Unix timestamp.
     * @return The builder.
     */
    fun before(before: Int?) = apply { this.before = before }

    /**
     * Sets the filtering your query based on grant email address (if applicable).
     * @param email The filtering your query based on grant email address (if applicable).
     * @return The builder.
     */
    fun email(email: String?) = apply { this.email = email }

    /**
     * Sets the filtering your query based on grant email status (if applicable).
     * @param grantStatus The filtering your query based on grant email status (if applicable).
     * @return The builder.
     */
    fun grantStatus(grantStatus: String?) = apply { this.grantStatus = grantStatus }

    /**
     * Sets the filtering your query based on grant IP address.
     * @param ip The filtering your query based on grant IP address.
     * @return The builder.
     */
    fun ip(ip: String?) = apply { this.ip = ip }

    /**
     * Sets the filtering your query based on OAuth provider.
     * @param provider The filtering your query based on OAuth provider.
     * @return The builder.
     */
    fun provider(provider: AuthProvider?) = apply { this.provider = provider }

    /**
     * Builds a [ListGrantsQueryParams] instance.
     * @return The [ListGrantsQueryParams] instance.
     */
    fun build() = ListGrantsQueryParams(
      limit,
      offset,
      sortBy,
      orderBy,
      since,
      before,
      email,
      grantStatus,
      ip,
      provider,
    )
  }
}
