package com.nylas;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Draft extends Message {

	protected Map<String, String> metadata = new HashMap<>() ;
	private String reply_to_message_id;
	private Integer version;
	private Tracking tracking;
	
	@Override
	public String getObjectType() {
		return "draft";
	}
	
	public String getReplyToMessageId() {
		return reply_to_message_id;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	public Tracking getTracking() {
		return tracking;
	}

	/**
	 * Enable message tracking options.
	 * <p>
	 * NOTE - this field is only used when invoking Drafts.send
	 * tracking information is not persisted when creating or updating drafts to be sent later.
	 * <p>
	 * <a href="https://docs.nylas.com/reference#message-tracking-overview">
	 * https://docs.nylas.com/reference#message-tracking-overview</a>
	 */
	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
	}

	/**
	 * The subject line of the draft.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * A single name and email pair, to set as the "from" header.
	 * Note that not all providers support setting this in a draft.
	 */
	public void setFrom(NameEmail from) {
		this.from = Arrays.asList(from);
	}

	/**
	 * The name-email pairs of the recipients.
	 */
	public void setTo(List<NameEmail> to) {
		this.to = to;
	}

	/**
	 * The name-email pairs of the recipients to be cc’d.
	 */
	public void setCc(List<NameEmail> cc) {
		this.cc = cc;
	}

	/**
	 * The name-email pairs of the recipients to be bcc’d.
	 */
	public void setBcc(List<NameEmail> bcc) {
		this.bcc = bcc;
	}

	/**
	 * An array of name and email pairs, to set an alternative Reply-To header in the final sent message.
	 * Note that not all providers support setting this in a draft.
	 */
	public void setReplyTo(List<NameEmail> replyTo) {
		this.reply_to = replyTo;
	}
	
	/**
	 * A single name/email pair, to set an alternative Reply-To header in the final sent message.
	 * Note that not all providers support setting this in a draft.
	 */
	public void setReplyTo(NameEmail replyTo) {
		setReplyTo(Arrays.asList(replyTo));
	}

	/**
	 * The full HTML draft body text.
	 */
	public void setBody(String body) {
		this.body = body;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	@Override
	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void attach(File file) {
		this.files.add(file);
	}
	
	public void detach(File file) {
		for (int i = 0; i < this.files.size(); i++) {
			File f = this.files.get(i);
			if (f == file
					|| (f.getId() != null && f.getId().equals(file.getId()))) {
				this.files.remove(i);
				i--; // back up a step since we removed an item
			}
		}
	}
	
	public void setReplyToMessageId(String replyToMessageId) {
		if (hasId()) {
			throw new UnsupportedOperationException("Cannot set or modify a reply_to_message_id on an existing draft");
		}
		this.reply_to_message_id = replyToMessageId;
	}
	
	public void setThreadId(String threadId) {
		if (hasId()) {
			throw new UnsupportedOperationException("Cannot set or modify a thread_id on an existing draft");
		}
		this.thread_id = threadId;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/**
	 * Add single metadata key-value pair to the event
	 * @param key The key of the metadata entry
	 * @param value The value of the metadata entry
	 */
	public void addMetadata(String key, String value) {
		this.metadata.put(key, value);
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		Maps.putIfNotNull(params, "subject", getSubject());
		Maps.putIfNotNull(params, "from", getFrom());
		Maps.putIfNotNull(params, "reply_to", getReplyTo());
		Maps.putIfNotNull(params, "to", getTo());
		Maps.putIfNotNull(params, "cc", getCc());
		Maps.putIfNotNull(params, "bcc", getBcc());
		Maps.putIfNotNull(params, "body", getBody());
		Maps.putIfNotNull(params, "version", getVersion());
		Maps.putIfNotNull(params, "metadata", getMetadata());
		List<String> fileIds = getFiles().stream().map(f -> f.getId()).collect(Collectors.toList());
		params.put("file_ids", fileIds);
		
		if (creation) {
			Maps.putIfNotNull(params, "thread_id", getThreadId());
			Maps.putIfNotNull(params, "reply_to_message_id", getReplyToMessageId());
		}
		return params;
	}
	
	@Override
	public String toString() {
		return "Draft [reply_to_message_id=" + reply_to_message_id + ", version=" + version + ", account_id="
				+ getAccountId() + ", thread_id=" + thread_id + ", subject=" + subject + ", from=" + from + ", to=" + to
				+ ", cc=" + cc + ", bcc=" + bcc + ", reply_to=" + reply_to + ", date=" + date + ", unread=" + unread
				+ ", starred=" + starred + ", snippet=" + snippet + ", body=" + body + ", files=" + files + ", folder="
				+ folder + ", labels=" + labels + ", tracking=" + tracking + ", metadata=" + metadata + "]";
	}
	
	
}
