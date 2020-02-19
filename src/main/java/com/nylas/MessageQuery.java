package com.nylas;

import java.time.Instant;

import okhttp3.HttpUrl;

public class MessageQuery extends MessageObjectQuery<MessageQuery> {

	private String from;
	private Long receivedBefore;
	private Long receivedAfter;
	
	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		
		if (from != null) {
			url.addQueryParameter("from", from);
		}
		if (receivedBefore != null) {
			url.addQueryParameter("received_before", receivedBefore.toString());
		}
		if (receivedAfter != null) {
			url.addQueryParameter("received_after", receivedAfter.toString());
		}
	}
	
	public MessageQuery from(String from) {
		this.from = from;
		return this;
	}
	
	public MessageQuery receivedBefore(long receivedBefore) {
		this.receivedBefore = receivedBefore;
		return this;
	}
	
	public MessageQuery receivedBefore(Instant receivedBefore) {
		return receivedBefore(receivedBefore.getEpochSecond());
	}
	
	public MessageQuery receivedAfter(long receivedAfter) {
		this.receivedAfter = receivedAfter;
		return this;
	}
	
	public MessageQuery receivedAfter(Instant receivedAfter) {
		return receivedAfter(receivedAfter.getEpochSecond());
	}
}
