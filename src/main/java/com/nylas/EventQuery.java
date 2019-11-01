package com.nylas;

import java.time.Instant;

import okhttp3.HttpUrl;

public class EventQuery extends PaginatedQuery<EventQuery> {

	private Boolean expandRecurring;
	private Boolean showCancelled;
	private String eventId;
	private String calendarId;
	private String title;
	private String description;
	private String location;
	private Long startsBefore;
	private Long startsAfter;
	private Long endsBefore;
	private Long endsAfter;

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
			url.addQueryParameter("starts_before", startsBefore.toString());
		}
		if (startsAfter != null) {
			url.addQueryParameter("starts_after", startsAfter.toString());
		}
		if (endsBefore != null) {
			url.addQueryParameter("ends_before", endsBefore.toString());
		}
		if (endsAfter != null) {
			url.addQueryParameter("ends_after", endsAfter.toString());
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
	
	public EventQuery startsBefore(long startsBefore) {
		this.startsBefore = startsBefore;
		return this;
	}
	
	public EventQuery startsBefore(Instant startsBefore) {
		return startsBefore(startsBefore.getEpochSecond());
	}
	
	public EventQuery startsAfter(long startsAfter) {
		this.startsAfter = startsAfter;
		return this;
	}
	
	public EventQuery startsAfter(Instant startsAfter) {
		return startsAfter(startsAfter.getEpochSecond());
	}
	
	public EventQuery endsBefore(long endsBefore) {
		this.endsBefore = endsBefore;
		return this;
	}
	
	public EventQuery endsBefore(Instant endsBefore) {
		return endsBefore(endsBefore.getEpochSecond());
	}
	
	public EventQuery endsAfter(long endsAfter) {
		this.endsAfter = endsAfter;
		return this;
	}
	
	public EventQuery endsAfter(Instant endsAfter) {
		return endsAfter(endsAfter.getEpochSecond());
	}
	
}
