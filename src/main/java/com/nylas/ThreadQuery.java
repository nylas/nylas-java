package com.nylas;

import java.util.Set;

public class ThreadQuery {

	private Integer limit;
	private Integer offset;
	private String subject;
	private Set<String> anyEmail;
	private String to;
	private String from;
	private String cc;
	private String bcc;
	private String in;
	private Boolean unread;
	private Boolean starred;
	private String filename;
	private Long lastMessageBefore;
	private Long lastMessageAfter;
	private Long startedBefore;
	private Long startedAfter;
	
	public ThreadQuery limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public ThreadQuery offset(int offset) {
		this.offset = offset;
		return this;
	}
	
	public ThreadQuery subject(String subject) {
		this.subject = subject;
		return this;
	}
	
	public ThreadQuery anyEmail(Set<String> anyEmail) {
		this.anyEmail = anyEmail;
		return this;
	}
	
	public ThreadQuery to(String to) {
		this.to = to;
		return this;
	}
	
	public ThreadQuery from(String from) {
		this.from = from;
		return this;
	}
	
	public ThreadQuery cc(String cc) {
		this.cc = cc;
		return this;
	}
	
	public ThreadQuery bcc(String bcc) {
		this.bcc = bcc;
		return this;
	}
	
	public ThreadQuery in(String in) {
		this.in = in;
		return this;
	}
	
	public ThreadQuery unread(boolean unread) {
		this.unread = unread;
		return this;
	}
	
	public ThreadQuery starred(boolean starred) {
		this.starred = starred;
		return this;
	}
	
	public ThreadQuery filename(String filename) {
		this.filename = filename;
		return this;
	}
	
	public ThreadQuery lastMessageBefore(long lastMessageBefore) {
		this.lastMessageBefore = lastMessageBefore;
		return this;
	}
	
	public ThreadQuery lastMessageAfter(long lastMessageAfter) {
		this.lastMessageAfter = lastMessageAfter;
		return this;
	}
	
	public ThreadQuery startedBefore(long startedBefore) {
		this.startedBefore = startedBefore;
		return this;
	}
	
	public ThreadQuery startedAfter(long startedAfter) {
		this.startedAfter = startedAfter;
		return this;
	}
	

}
