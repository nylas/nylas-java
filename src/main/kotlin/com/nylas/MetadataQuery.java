package com.nylas;

import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetadataQuery {

	private String metadataSearch;
	private final List<String> metadataKeys = new ArrayList<>();
	private final List<String> metadataValues = new ArrayList<>();
	private final List<String> metadataPairs = new ArrayList<>();

	/**
	 * Modifier keywords for querying metadata.
	 *
	 * NONE will return any object that does not match the metadata provided.
	 * ANY will return any account that matches any of the metadata provided.
	 * All will return any account that matches all the metadata provided.
	 */
	public enum MetadataSearchQueryType {
		NONE,
		ANY,
		ALL,

		;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	/**
	 * Set the type of filtering to perform with the metadata provided in the query.
	 * If not set, the API defaults to "any".
	 *
	 * @param metadataSearchQueryType The keyword modifier for metadata queries.
	 * @return The query with the metadata search parameter added.
	 */
	public MetadataQuery metadataSearch(MetadataSearchQueryType metadataSearchQueryType) {
		this.metadataSearch = metadataSearchQueryType.toString();
		return this;
	}

	/**
	 * Set the metadata keys to query.
	 *
	 * @param metadataKey The metadata key(s) to query.
	 * @return The query with the metadata key parameter added.
	 */
	public MetadataQuery metadataKey(String... metadataKey) {
		this.metadataKeys.addAll(Arrays.asList(metadataKey));
		return this;
	}

	/**
	 * Set the metadata values to query.
	 *
	 * @param metadataValue The metadata value(s) to query.
	 * @return The query with the metadata value parameter added.
	 */
	public MetadataQuery metadataValue(String... metadataValue) {
		this.metadataValues.addAll(Arrays.asList(metadataValue));
		return this;
	}

	/**
	 * Set the metadata key-value pair to query.
	 *
	 * @param key The key of the key-value pair to query.
	 * @param value The value of the key-value pair to query.
	 * @return The query with the metadata pair parameter added.
	 */
	public MetadataQuery metadataPair(String key, String value) {
		this.metadataPairs.add(key + ":" + value);
		return this;
	}

	protected void addParameters(HttpUrl.Builder url) {
		if (metadataSearch != null) {
			url.addQueryParameter("metadata_search", metadataSearch);
		}
		for(String key : metadataKeys) {
			url.addQueryParameter("metadata_key", key);
		}
		for(String value : metadataValues) {
			url.addQueryParameter("metadata_value", value);
		}
		for(String value : metadataPairs) {
			url.addQueryParameter("metadata_pair", value);
		}
	}
}
