package com.nylas;

public class Tracking {

	private boolean links;
	private boolean opens;
	private boolean thread_replies;
	private String payload;
	
	public boolean isLinks() {
		return links;
	}
	
	public void setLinks(boolean links) {
		this.links = links;
	}
	
	public boolean isOpens() {
		return opens;
	}
	
	public void setOpens(boolean opens) {
		this.opens = opens;
	}
	
	public boolean isThreadReplies() {
		return thread_replies;
	}
	
	public void setThreadReplies(boolean threadReplies) {
		this.thread_replies = threadReplies;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
}
