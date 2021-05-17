package com.nylas;

import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;

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
	private String[] metadataKey;
	private String[] metadataValue;
	private Map<String, String> metadataPair;

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
		if (metadataKey != null) {
			for(String key : metadataKey) {
				url.addQueryParameter("metadata_key", key);
			}
		}
		if (metadataValue != null) {
			for(String value : metadataValue) {
				url.addQueryParameter("metadata_value", value);
			}
		}
		if (metadataPair != null) {
			for (Entry<String, String> pair : metadataPair.entrySet()) {
				url.addQueryParameter("metadata_pair", String.format("%s:%s", pair.getKey(), pair.getValue()));
			}
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

	public EventQuery metadataKey(String... metadataKey) {
		this.metadataKey = metadataKey;
		return this;
	}

	public EventQuery metadataValue(String... metadataValue) {
		this.metadataValue = metadataValue;
		return this;
	}

	public EventQuery metadataPair(Map<String, String> metadataPair) {
		this.metadataPair = metadataPair;
		return this;
	}
	
}
