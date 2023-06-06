package com.nylas;

import okhttp3.HttpUrl;

/**
 * Represents a query describing a particular set of restful model objects.
 * Includes pagination and filter information.
 */
public abstract class RestfulQuery<Q extends RestfulQuery<Q>> implements Cloneable {

	private Integer offset;
	private Integer limit;
	
	/**
	 * Specify how many items for the server to skip before returning results.
	 * Useful for pagination.
	 */
	public Q offset(int offset) {
		this.offset = offset;
		return self();
	}

	/**
	 * Specify how many items for the server to return.
	 * Useful for pagination.
	 */
	public Q limit(int limit) {
		this.limit = limit;
		return self();
	}
	
	int getEffectiveOffset() {
		return offset == null ? 0 : offset.intValue();
	}
	
	int getEffectiveLimit() {
		return limit == null ? Integer.MAX_VALUE : limit.intValue();
	}
	
	/**
	 * Add url query parameters for the the query.<br/>
	 * Internal method will be changing to package private.<br/>
	 * Subclasses may override but should call through.
	 */
	@Deprecated
	public void addParameters(HttpUrl.Builder url) {
		if (offset != null) {
			url.addQueryParameter("offset", offset.toString());
		}
		if (limit != null) {
			url.addQueryParameter("limit", limit.toString());
		}
	}
	
	/**
	 * Return a copy of this query with an updated offset and limit.<br/>
	 * Internal method will be changing to package private.
	 */
	@Deprecated
	public Q copyAtNewOffsetLimit(int newOffset, int newLimit) {
		try {
			@SuppressWarnings("unchecked")
			Q copy = (Q) super.clone();
			copy.offset(newOffset);
			copy.limit(newLimit);
			return copy;
		} catch(CloneNotSupportedException e) {
			throw new RuntimeException("clone contract failure", e);
		}
	}
	
	// helper method for fluent builder style without type warnings
	@SuppressWarnings("unchecked")
	protected final Q self() {
		return (Q) this;
	}
}
