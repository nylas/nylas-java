package com.nylas;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;

public class EventQuery extends RestfulQuery<EventQuery> {

	private Boolean expandRecurring;
	private Boolean showCancelled;
	private String eventId;
	private String calendarId;
	private String title;
	private String description;
	private String location;
	private Instant startsBefore;
	private Instant startsAfter;
	private Instant endsBefore;
	private Instant endsAfter;
	private MetadataQuery metadataQuery;
	private List<String> metadataKeys;
	private List<String> metadataValues;
	private List<String> metadataPairs;

	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		
		if (expandRecurring != null) {
			url.addQueryParameter("expand_recurring", expandRecurring.toString());
		}
		if (showCancelled != null) {
			url.addQueryParameter("show_cancelled", showCancelled.toString());
		}
		if (eventId != null) {
			url.addQueryParameter("event_id", eventId);
		}
		if (calendarId != null) {
			url.addQueryParameter("calendar_id", calendarId);
		}
		if (title != null) {
			url.addQueryParameter("title", title);
		}
		if (description != null) {
			url.addQueryParameter("description", description);
		}
		if (location != null) {
			url.addQueryParameter("location", location);
		}
		if (startsBefore != null) {
			url.addQueryParameter("starts_before", Instants.formatEpochSecond(startsBefore));
		}
		if (startsAfter != null) {
			url.addQueryParameter("starts_after", Instants.formatEpochSecond(startsAfter));
		}
		if (endsBefore != null) {
			url.addQueryParameter("ends_before", Instants.formatEpochSecond(endsBefore));
		}
		if (endsAfter != null) {
			url.addQueryParameter("ends_after", Instants.formatEpochSecond(endsAfter));
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
		if (metadataQuery != null) {
			metadataQuery.addParameters(url);
		}
	}
	
	public EventQuery expandRecurring(Boolean expandRecurring) {
		this.expandRecurring = expandRecurring;
		return this;
	}
	
	public EventQuery showCancelled(Boolean showCancelled) {
		this.showCancelled = showCancelled;
		return this;
	}
	
	public EventQuery eventId(String eventId) {
		this.eventId = eventId;
		return this;
	}
	
	public EventQuery calendarId(String calendarId) {
		this.calendarId = calendarId;
		return this;
	}
	
	public EventQuery title(String title) {
		this.title = title;
		return this;
	}
	
	public EventQuery description(String description) {
		this.description = description;
		return this;
	}
	
	public EventQuery location(String location) {
		this.location = location;
		return this;
	}

	public EventQuery startsBefore(Instant startsBefore) {
		this.startsBefore = startsBefore;
		return this;
	}
	
	public EventQuery startsAfter(Instant startsAfter) {
		this.startsAfter = startsAfter;
		return this;
	}
	
	public EventQuery endsBefore(Instant endsBefore) {
		this.endsBefore = endsBefore;
		return this;
	}
	
	public EventQuery endsAfter(Instant endsAfter) {
		this.endsAfter = endsAfter;
		return this;
	}

	/**
	 * Add a metadata query to the event query.
	 *
	 * @param metadataQuery The metadata query.
	 * @return The Event query with the metadata query set.
	 */
	public EventQuery metadataQuery(MetadataQuery metadataQuery) {
		this.metadataQuery = metadataQuery;
		return this;
	}

	/**
	 * Return events with metadata containing a property having the given key.
	 * 
	 * If multiple instances of metadata methods are invoked
	 * (any combination of calls to metadataKey, metadataValue, or metadataPair),
	 * then this query will return events which match ANY one of them.
	 *
	 * @deprecated Use of these metadata methods are replaced by {@link #metadataQuery(MetadataQuery)}
	 */
	@Deprecated
	public EventQuery metadataKey(String... metadataKey) {
		if (this.metadataKeys == null) {
			this.metadataKeys = new ArrayList<>();
		}
		this.metadataKeys.addAll(Arrays.asList(metadataKey));
		return this;
	}

	/**
	 * Return events with metadata containing a property having the given value.
	 * 
	 * If multiple instances of metadata methods are invoked
	 * (any combination of calls to metadataKey, metadataValue, or metadataPair),
	 * then this query will return events which match ANY one of them.
	 *
	 * @deprecated Use of these metadata methods are replaced by {@link #metadataQuery(MetadataQuery)}
 	*/
	@Deprecated
	public EventQuery metadataValue(String... metadataValue) {
		if (this.metadataValues == null) {
			this.metadataValues = new ArrayList<>();
		}
		this.metadataValues.addAll(Arrays.asList(metadataValue));
		return this;
	}

	/**
	 * Return events with metadata containing a property having the given key-value pair.
	 * 
	 * If multiple instances of metadata methods are invoked
	 * (any combination of calls to metadataKey, metadataValue, or metadataPair),
	 * then this query will return events which match ANY one of them.
	 *
	 * @deprecated Use of these metadata methods are replaced by {@link #metadataQuery(MetadataQuery)}
	 */
	@Deprecated
	public EventQuery metadataPair(String key, String value) {
		if (this.metadataPairs == null) {
			this.metadataPairs = new ArrayList<>();
		}
		this.metadataPairs.add(key + ":" + value);
		return this;
	}
}
