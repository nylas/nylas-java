package com.nylas;

import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalendarQuery extends RestfulQuery<CalendarQuery> {

	private String metadataSearch;
	private List<String> metadataKeys;
	private List<String> metadataValues;
	private List<String> metadataPairs;

	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		if (metadataSearch != null) {
			url.addQueryParameter("metadata_search", metadataSearch);
		}
		if (metadataKeys != null) {
			for(String key : metadataKeys) {
				url.addQueryParameter("metadata_key", key);
			}
		}
		if (metadataValues != null) {
			for(String value : metadataValues) {
				url.addQueryParameter("metadata_value", value);
			}
		}
		if (metadataPairs != null) {
			for(String value : metadataPairs) {
				url.addQueryParameter("metadata_pair", value);
			}
		}
	}

	/**
	 * Return calendars query with a modifier on the metadata parameters.
	 *
	 * If NONE is provided, it will return any calendar that does not match the metadata queried
	 * If ANY is provided, it will return any calendar with metadata
	 * If ALL is provided, it will return any calendar that matches all the metadata queried
	 */
	public CalendarQuery metadataSearch(MetadataSearchOptions metadataSearch) {
		this.metadataSearch = metadataSearch.toString();
		return this;
	}

	/**
	 * Return calendars with metadata containing a property having the given key.
	 *
	 * If multiple instances of metadata methods are invoked
	 * (any combination of calls to metadataKey, metadataValue, or metadataPair),
	 * then this query will return calendars which match ANY one of them.
	 */
	public CalendarQuery metadataKey(String... metadataKey) {
		if (this.metadataKeys == null) {
			this.metadataKeys = new ArrayList<>();
		}
		this.metadataKeys.addAll(Arrays.asList(metadataKey));
		return this;
	}

	/**
	 * Return calendars with metadata containing a property having the given value.
	 *
	 * If multiple instances of metadata methods are invoked
	 * (any combination of calls to metadataKey, metadataValue, or metadataPair),
	 * then this query will return calendars which match ANY one of them.
	 */
	public CalendarQuery metadataValue(String... metadataValue) {
		if (this.metadataValues == null) {
			this.metadataValues = new ArrayList<>();
		}
		this.metadataValues.addAll(Arrays.asList(metadataValue));
		return this;
	}

	/**
	 * Return calendars with metadata containing a property having the given key-value pair.
	 *
	 * If multiple instances of metadata methods are invoked
	 * (any combination of calls to metadataKey, metadataValue, or metadataPair),
	 * then this query will return calendars which match ANY one of them.
	 */
	public CalendarQuery metadataPair(String key, String value) {
		if (this.metadataPairs == null) {
			this.metadataPairs = new ArrayList<>();
		}
		this.metadataPairs.add(key + ":" + value);
		return this;
	}
}
