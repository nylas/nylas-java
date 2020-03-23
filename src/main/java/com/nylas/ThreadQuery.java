package com.nylas;

import java.time.Instant;
import java.util.Arrays;

import okhttp3.HttpUrl;

public class ThreadQuery extends PaginatedQuery<ThreadQuery> {

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
	private Instant lastMessageBefore;
	private Instant lastMessageAfter;
	private Instant startedBefore;
	private Instant startedAfter;
	
	@Override
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		
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
			url.addQueryParameter("last_message_before", Instants.formatEpochSecond(lastMessageBefore));
		}
		if (lastMessageAfter != null) {
			url.addQueryParameter("last_message_after", Instants.formatEpochSecond(lastMessageAfter));
		}
		if (startedBefore != null) {
			url.addQueryParameter("started_before", Instants.formatEpochSecond(startedBefore));
		}
		if (startedAfter != null) {
			url.addQueryParameter("started_after", Instants.formatEpochSecond(startedAfter));
		}
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
	
	public ThreadQuery lastMessageBefore(Instant lastMessageBefore) {
		this.lastMessageBefore = lastMessageBefore;
		return this;
	}
	
	public ThreadQuery lastMessageAfter(Instant lastMessageAfter) {
		this.lastMessageAfter = lastMessageAfter;
		return this;
	}
	
	public ThreadQuery startedBefore(Instant startedBefore) {
		this.startedBefore = startedBefore;
		return this;
	}
	
	public ThreadQuery startedAfter(Instant startedAfter) {
		this.startedAfter = startedAfter;
		return this;
	}

}
