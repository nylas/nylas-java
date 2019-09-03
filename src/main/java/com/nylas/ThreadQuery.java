package com.nylas;

import java.time.Instant;
import java.util.Arrays;

import okhttp3.HttpUrl;

public class ThreadQuery {

	private Integer limit;
	private Integer offset;
	private String subject;
	private String anyEmail;
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
	
	public void addParameters(HttpUrl.Builder url) {
		if (limit != null) {
			url.addQueryParameter("limit", limit.toString());
		}
		if (offset != null) {
			url.addQueryParameter("offset", offset.toString());
		}
		if (subject != null) {
			url.addQueryParameter("subject", subject);
		}
		if (anyEmail != null) {
			url.addQueryParameter("any_email", anyEmail);
		}
		if (to != null) {
			url.addQueryParameter("to", to);
		}
		if (from != null) {
			url.addQueryParameter("from", from);
		}
		if (cc != null) {
			url.addQueryParameter("cc", cc);
		}
		if (bcc != null) {
			url.addQueryParameter("bcc", bcc);
		}
		if (in != null) {
			url.addQueryParameter("in", in);
		}
		if (unread != null) {
			url.addQueryParameter("unread", unread.toString());
		}
		if (starred != null) {
			url.addQueryParameter("starred", starred.toString());
		}
		if (filename != null) {
			url.addQueryParameter("filename", filename);
		}
		if (lastMessageBefore != null) {
			url.addQueryParameter("last_message_before", lastMessageBefore.toString());
		}
		if (lastMessageAfter != null) {
			url.addQueryParameter("last_message_after", lastMessageAfter.toString());
		}
		if (startedBefore != null) {
			url.addQueryParameter("started_before", startedBefore.toString());
		}
		if (startedAfter != null) {
			url.addQueryParameter("started_after", startedAfter.toString());
		}
	}
	
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
	
	public ThreadQuery anyEmail(String... emails) {
		return anyEmail(Arrays.asList(emails));
	}
	
	public ThreadQuery anyEmail(Iterable<String> emails) {
		if (emails == null) {
			this.anyEmail = null;
		} else {
			this.anyEmail = String.join(",", emails);
			if (this.anyEmail.isEmpty()) {
				this.anyEmail = null;
			}
		}
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
	
	public ThreadQuery lastMessageBefore(Instant lastMessageBefore) {
		return lastMessageBefore(lastMessageBefore.getEpochSecond());
	}
	
	public ThreadQuery lastMessageAfter(long lastMessageAfter) {
		this.lastMessageAfter = lastMessageAfter;
		return this;
	}
	
	public ThreadQuery lastMessageAfter(Instant lastMessageAfter) {
		return lastMessageAfter(lastMessageAfter.getEpochSecond());
	}
	
	public ThreadQuery startedBefore(long startedBefore) {
		this.startedBefore = startedBefore;
		return this;
	}
	
	public ThreadQuery startedBefore(Instant startedBefore) {
		return startedBefore(startedBefore.getEpochSecond());
	}
	
	public ThreadQuery startedAfter(long startedAfter) {
		this.startedAfter = startedAfter;
		return this;
	}
	
	public ThreadQuery startedAfter(Instant startedAfter) {
		return startedAfter(startedAfter.getEpochSecond());
	}
	

}
