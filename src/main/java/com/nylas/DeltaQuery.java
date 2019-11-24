package com.nylas;

import okhttp3.HttpUrl;

public class DeltaQuery extends RestfulQuery<DeltaQuery> {

	private String excludeTypes;
	private String includeTypes;
	
	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);
		
		if (excludeTypes != null) {
			url.addQueryParameter("exclude_types", excludeTypes);
		}
		if (includeTypes != null) {
			url.addQueryParameter("include_types", includeTypes);
		}
	}
	
	public DeltaQuery excludeTypes(String excludeTypes) {
		this.excludeTypes = excludeTypes;
		return this;
	}
	
	public DeltaQuery includeTypes(String includeTypes) {
		this.includeTypes = includeTypes;
		return this;
	}
	
}
