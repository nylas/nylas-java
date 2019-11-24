package com.nylas;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Message extends AccountOwnedModel implements Delta.Attributes {

	protected String thread_id;
	protected String subject;
	protected List<NameEmail> from = Collections.emptyList();
	protected List<NameEmail> to = Collections.emptyList();
	protected List<NameEmail> cc = Collections.emptyList();
	protected List<NameEmail> bcc = Collections.emptyList();
	protected List<NameEmail> reply_to = Collections.emptyList();
	protected Long date;
	protected Boolean unread;
	protected Boolean starred;
	protected String snippet;
	protected String body;
	protected List<File> files = Collections.emptyList();
	protected List<Event> events = Collections.emptyList();
	protected Folder folder;
	protected List<Label> labels = Collections.emptyList();

	// only available in expanded message view
	private Map<String, Object> headers = Collections.emptyMap();

	@Override
	public String getObjectType() {
		return "message";
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

	public List<File> getFiles() {
		return files;
	}

	public List<Event> getEvents() {
		return events;
	}
	
	public Folder getFolder() {
		return folder;
	}

	public List<Label> getLabels() {
		return labels;
	}

	/**
	 * Return additional RFC2822 headers, only available in expanded view
	 */
	public Map<String, Object> getHeaders() {
		return headers;
	}

	@Override
	public String toString() {
		return "Message [id=" + getId() + ", account_id=" + getAccountId() + ", thread_id=" + thread_id + ", subject="
				+ subject + ", from=" + from + ", to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", reply_to=" + reply_to
				+ ", date=" + date + ", unread=" + unread + ", starred=" + starred + ", snippet=" + snippet
				+ ", body.length=" + body.length() + ", files=" + files + ", events=" + events + ", folder=" + folder
				+ ", labels=" + labels + ", headers=" + headers + "]";
	}
	
}
