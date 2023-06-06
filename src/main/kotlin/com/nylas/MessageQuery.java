package com.nylas;

import java.time.Instant;

import okhttp3.HttpUrl;

public class MessageQuery extends MessageObjectQuery<MessageQuery> {

	private String from;
	private Instant receivedBefore;
	private Instant receivedAfter;
	
	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		
		if (from != null) {
			url.addQueryParameter("from", from);
		}
		if (receivedBefore != null) {
			url.addQueryParameter("received_before", Instants.formatEpochSecond(receivedBefore));
		}
		if (receivedAfter != null) {
			url.addQueryParameter("received_after", Instants.formatEpochSecond(receivedAfter));
		}
	}
	
	public MessageQuery from(String from) {
		this.from = from;
		return this;
	}
	
	public MessageQuery receivedBefore(Instant receivedBefore) {
		this.receivedBefore = receivedBefore;
		return this;
	}
	
	public MessageQuery receivedAfter(Instant receivedAfter) {
		this.receivedAfter = receivedAfter;
		return this;
	}
}
