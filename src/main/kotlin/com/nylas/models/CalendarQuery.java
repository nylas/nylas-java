package com.nylas.models;

import okhttp3.HttpUrl;

public class CalendarQuery extends RestfulQuery<CalendarQuery> {

	private MetadataQuery metadataQuery;

	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		if (metadataQuery != null) {
			metadataQuery.addParameters(url);
		}
	}

	/**
	 * Add a metadata query to the calendar query.
	 *
	 * @param metadataQuery The metadata query.
	 * @return The Calendar query with the metadata query set.
	 */
	public CalendarQuery metadataQuery(MetadataQuery metadataQuery) {
		this.metadataQuery = metadataQuery;
		return this;
	}
}
