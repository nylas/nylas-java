package com.nylas;

import okhttp3.HttpUrl.Builder;

class SearchQuery extends RestfulQuery<SearchQuery> {

	private String query;
	
	SearchQuery(String query) {
		this.query = query;
	}
	
	SearchQuery(String query, Integer limit, Integer offset) {
		this(query);
		limit(limit);
		offset(offset);
	}
	
	@Override
	public void addParameters(Builder url) {
		super.addParameters(url);  // must call through
		
		if (query != null) {
			url.addQueryParameter("q", query);
		}
	}

	public SearchQuery query(String query) {
		this.query = query;
		return this;
	}


}
