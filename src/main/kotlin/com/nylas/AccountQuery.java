package com.nylas;

import okhttp3.HttpUrl;

public class AccountQuery extends RestfulQuery<AccountQuery> {

	private MetadataQuery metadataQuery;

	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		if (metadataQuery != null) {
			metadataQuery.addParameters(url);
		}
	}

	/**
	 * Add a metadata query to the account query.
	 *
	 * @param metadataQuery The metadata query.
	 * @return The Account query with the metadata query set.
	 */
	public AccountQuery metadataQuery(MetadataQuery metadataQuery) {
		this.metadataQuery = metadataQuery;
		return this;
	}
}
