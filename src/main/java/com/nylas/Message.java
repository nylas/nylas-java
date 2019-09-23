package com.nylas;

import java.util.List;

public class Message extends RestfulModel {

	protected String account_id;
	protected String thread_id;
	protected String subject;
	protected List<NameEmail> from;
	protected List<NameEmail> to;
	protected List<NameEmail> cc;
	protected List<NameEmail> bcc;
	protected List<NameEmail> reply_to;
	protected Long date;
	protected Boolean unread;
	protected Boolean starred;
	protected String snippet;
	protected String body;
	
	// TODO files
	// TODO events
	
	private Folder folder;
	private List<Label> labels;
	
	
	public String getAccountId() {
		return account_id;
	}

	public String getThreadId() {
		return thread_id;
	}

	public String getSubject() {
		return subject;
	}

	public List<NameEmail> getFrom() {
		return from;
	}

	public List<NameEmail> getTo() {
		return to;
	}

	public List<NameEmail> getCc() {
		return cc;
	}

	public List<NameEmail> getBcc() {
		return bcc;
	}

	public List<NameEmail> getReplyTo() {
		return reply_to;
	}

	public Long getDate() {
		return date;
	}

	public Boolean getUnread() {
		return unread;
	}

	public Boolean getStarred() {
		return starred;
	}

	public String getSnippet() {
		return snippet;
	}

	public String getBody() {
		return body;
	}

	public Folder getFolder() {
		return folder;
	}

	public List<Label> getLabels() {
		return labels;
	}

	@Override
	public String toString() {
		return "Message [id=" + getId() + ", account_id=" + account_id + ", thread_id=" + thread_id + ", subject=" + subject
				+ ", from=" + from + ", to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", reply_to=" + reply_to + ", date="
				+ date + ", unread=" + unread + ", starred=" + starred + ", snippet=" + snippet + ", body.length="
				+ body.length() + ", folder=" + folder + ", labels=" + labels + "]";
	}
	
}
