package com.nylas;

import okhttp3.HttpUrl;

public class AccountsQuery {

	private Integer limit;
	private Integer offset;

	
	public void addParameters(HttpUrl.Builder url) {
		if (limit != null) {
			url.addQueryParameter("limit", limit.toString());
		}
		if (offset != null) {
			url.addQueryParameter("offset", offset.toString());
		}
	}
	
	public AccountsQuery limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public AccountsQuery offset(int offset) {
		this.offset = offset;
		return this;
	}
	

}
