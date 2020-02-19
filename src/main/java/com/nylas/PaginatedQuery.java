package com.nylas;

import okhttp3.HttpUrl;

public abstract class PaginatedQuery<Q extends PaginatedQuery<Q>> extends RestfulQuery<Q> {

	private Integer limit;
	private Integer offset;
	
	@Override
	public void addParameters(HttpUrl.Builder url) {
		if (limit != null) {
			url.addQueryParameter("limit", limit.toString());
		}
		if (offset != null) {
			url.addQueryParameter("offset", offset.toString());
		}
	}
	
	public Q limit(int limit) {
		this.limit = limit;
		return self();
	}
	
	public Q offset(int offset) {
		this.offset = offset;
		return self();
	}

}
