package com.nylas;

import java.util.Arrays;

import okhttp3.HttpUrl;

public class DraftsQuery {

	private Integer limit;
	private Integer offset;
	private String anyEmail;

	public void addParameters(HttpUrl.Builder url) {
		if (limit != null) {
			url.addQueryParameter("limit", limit.toString());
		}
		if (offset != null) {
			url.addQueryParameter("offset", offset.toString());
		}
		if (anyEmail != null) {
			url.addQueryParameter("any_email", anyEmail);
		}
	}
	
	public DraftsQuery limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public DraftsQuery offset(int offset) {
		this.offset = offset;
		return this;
	}
	
	public DraftsQuery anyEmail(String... emails) {
		return anyEmail(Arrays.asList(emails));
	}
	
	public DraftsQuery anyEmail(Iterable<String> emails) {
		if (emails == null) {
			this.anyEmail = null;
		} else {
			this.anyEmail = String.join(",", emails);
			if (this.anyEmail.isEmpty()) {
				this.anyEmail = null;
			}
		}
		return this;
	}
}
