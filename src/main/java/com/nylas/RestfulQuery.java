package com.nylas;

import okhttp3.HttpUrl;

public abstract class RestfulQuery<Q extends RestfulQuery<Q>> {

	public void addParameters(@SuppressWarnings("unused") HttpUrl.Builder url) {
		// subclasses to override
	}
	
	@SuppressWarnings("unchecked")
	final Q self() {
		return (Q) this;
	}
}
