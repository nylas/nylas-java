package com.nylas;

import java.time.Instant;
import java.util.Arrays;

import okhttp3.HttpUrl;

public class MessageQuery extends PaginatedQuery<MessageQuery> {

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
	
	private String threadId;
	private Long receivedBefore;
	private Long receivedAfter;
	private Boolean hasAttachment;
	
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
		
		if (threadId != null) {
			url.addQueryParameter("thread_id", threadId);
		}
		if (receivedBefore != null) {
			url.addQueryParameter("received_before", receivedBefore.toString());
		}
		if (receivedAfter != null) {
			url.addQueryParameter("received_after", receivedAfter.toString());
		}
		if (hasAttachment != null) {
			url.addQueryParameter("has_attachment", hasAttachment.toString());
		}
	}
	
	public MessageQuery subject(String subject) {
		this.subject = subject;
		return this;
	}
	
	public MessageQuery anyEmail(String... emails) {
		return anyEmail(Arrays.asList(emails));
	}
	
	public MessageQuery anyEmail(Iterable<String> emails) {
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
	
	public MessageQuery to(String to) {
		this.to = to;
		return this;
	}
	
	public MessageQuery from(String from) {
		this.from = from;
		return this;
	}
	
	public MessageQuery cc(String cc) {
		this.cc = cc;
		return this;
	}
	
	public MessageQuery bcc(String bcc) {
		this.bcc = bcc;
		return this;
	}
	
	public MessageQuery in(String in) {
		this.in = in;
		return this;
	}
	
	public MessageQuery unread(boolean unread) {
		this.unread = unread;
		return this;
	}
	
	public MessageQuery starred(boolean starred) {
		this.starred = starred;
		return this;
	}
	
	public MessageQuery filename(String filename) {
		this.filename = filename;
		return this;
	}
	
	public MessageQuery threadId(String threadId) {
		this.threadId = threadId;
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
	
	public MessageQuery hasAttachment(Boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
		return this;
	}

}
