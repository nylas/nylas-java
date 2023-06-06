package com.nylas;

import java.util.Arrays;

import okhttp3.HttpUrl;

/**
 * A shared abstract query object with the filters that work for both Message and Draft objects
 */
public abstract class MessageObjectQuery<Q extends MessageObjectQuery<Q>> extends RestfulQuery<Q> {

	private String subject;
	private String anyEmail;
	private String to;
	private String cc;
	private String bcc;
	private String in;
	private Boolean unread;
	private Boolean starred;
	private String threadId;
	private Boolean hasAttachment;
	private String filename;
	
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
		if (threadId != null) {
			url.addQueryParameter("thread_id", threadId);
		}
		if (hasAttachment != null) {
			url.addQueryParameter("has_attachment", hasAttachment.toString());
		}
		if (filename != null) {
			url.addQueryParameter("filename", filename);
		}
	}
	
	public Q subject(String subject) {
		this.subject = subject;
		return self();
	}
	
	public Q anyEmail(String... emails) {
		return anyEmail(Arrays.asList(emails));
	}
	
	public Q anyEmail(Iterable<String> emails) {
		this.anyEmail = String.join(",", emails);
		if (this.anyEmail.isEmpty()) {
			this.anyEmail = null;
		}

		return self();
	}
	
	public Q to(String to) {
		this.to = to;
		return self();
	}
	
	public Q cc(String cc) {
		this.cc = cc;
		return self();
	}
	
	public Q bcc(String bcc) {
		this.bcc = bcc;
		return self();
	}
	
	public Q in(String in) {
		this.in = in;
		return self();
	}
	
	public Q unread(boolean unread) {
		this.unread = unread;
		return self();
	}
	
	public Q starred(boolean starred) {
		this.starred = starred;
		return self();
	}
	
	public Q threadId(String threadId) {
		this.threadId = threadId;
		return self();
	}
	
	public Q hasAttachment(Boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
		return self();
	}

	public Q filename(String filename) {
		this.filename = filename;
		return self();
	}
	
}
