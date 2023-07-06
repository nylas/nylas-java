package com.nylas.models

data class ListGrantsQueryParams(
  val limit: Int? = null,
  val offset: Int? = null,
  val sortBy: String? = null,
  val orderBy: String? = null,
  val since: Int? = null,
  val before: Int? = null,
  val email: String? = null,
  val grantStatus: String? = null,
  val ip: String? = null,
  val provider: AuthProvider? = null,
) : IQueryParams {
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

    fun limit(limit: Int?) = apply { this.limit = limit }
    fun offset(offset: Int?) = apply { this.offset = offset }
    fun sortBy(sortBy: String?) = apply { this.sortBy = sortBy }
    fun orderBy(orderBy: String?) = apply { this.orderBy = orderBy }
    fun since(since: Int?) = apply { this.since = since }
    fun before(before: Int?) = apply { this.before = before }
    fun email(email: String?) = apply { this.email = email }
    fun grantStatus(grantStatus: String?) = apply { this.grantStatus = grantStatus }
    fun ip(ip: String?) = apply { this.ip = ip }
    fun provider(provider: AuthProvider?) = apply { this.provider = provider }

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
