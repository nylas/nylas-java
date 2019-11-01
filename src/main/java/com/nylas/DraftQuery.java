package com.nylas;

import java.util.Arrays;

import okhttp3.HttpUrl;

public class DraftQuery extends RestfulQuery<DraftQuery> {

	private String anyEmail;

	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		
		if (anyEmail != null) {
			url.addQueryParameter("any_email", anyEmail);
		}
	}
	
	public DraftQuery anyEmail(String... emails) {
		return anyEmail(Arrays.asList(emails));
	}
	
	public DraftQuery anyEmail(Iterable<String> emails) {
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
