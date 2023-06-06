package com.nylas;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class Thread extends AccountOwnedModel implements JsonObject {

	private String subject;
	private boolean unread;
	private boolean starred;
	private Long last_message_timestamp;
	private Long last_message_received_timestamp;
	private Long last_message_sent_timestamp;
	private Long first_message_timestamp;
	private List<NameEmail> participants = Collections.emptyList();
	private String snippet;
	private int version;
	private List<Folder> folders = Collections.emptyList();
	private List<Label> labels = Collections.emptyList();
	private boolean has_attachments;
	
	// available only in regular thread view
	private List<String> message_ids = Collections.emptyList();
	private List<String> draft_ids = Collections.emptyList();

	// available only in expanded thread view
	private List<Message> messages = Collections.emptyList();
	private List<Draft> drafts = Collections.emptyList();
	
	@Override
	public String getObjectType() {
		return "thread";
	}
	
	public String getSubject() {
		return subject;
	}

	public boolean isUnread() {
		return unread;
	}

	public boolean isStarred() {
		return starred;
	}

	public Instant getLastMessageTimestamp() {
		return Instants.toNullableInstant(last_message_timestamp);
	}

	public Instant getLastMessageReceivedTimestamp() {
		return Instants.toNullableInstant(last_message_received_timestamp);
	}

	public Instant getLastMessageSentTimestamp() {
		return Instants.toNullableInstant(last_message_sent_timestamp);
	}

	public Instant getFirstMessageTimestamp() {
		return Instants.toNullableInstant(first_message_timestamp);
	}

	public List<NameEmail> getParticipants() {
		return participants;
	}

	public String getSnippet() {
		return snippet;
	}

	public int getVersion() {
		return version;
	}

	public List<Folder> getFolders() {
		return folders;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public boolean hasAttachments() {
		return has_attachments;
	}
	
	/**
	 * Get contained message ids.
	 * Available only in regular (non-expanded) thread view
	 */
	public List<String> getMessageIds() {
		return message_ids;
	}

	/**
	 * Get contained draft ids.
	 * Available only in regular (non-expanded) thread view
	 */
	public List<String> getDraftIds() {
		return draft_ids;
	}

	/**
	 * Get full contained message objects.
	 * Available only in expanded thread view
	 */
	public List<Message> getMessages() {
		return messages;
	}

	/**
	 * Get full contained draft objects.
	 * Available only in expanded thread view
	 */
	public List<Draft> getDrafts() {
		return drafts;
	}

	/**
	 * Convenience method to create a draft for a reply to this thread.
	 * Sets the threadId and the subject to those of the thread.
	 * Does not populate other draft fields, including to/cc/bcc
	 */
	public Draft createReply() {
		Draft reply = new Draft();
		reply.setThreadId(getId());
		reply.setSubject(subject);
		return reply;
	}

	@Override
	public String toString() {
		return "Thread [id=" + getId() + ", account_id=" + getAccountId() + 
				",subject=" + subject + ", unread=" + unread + ", starred=" + starred
				+ ", last_message_timestamp=" + getLastMessageTimestamp() + ", last_message_received_timestamp="
				+ getLastMessageReceivedTimestamp() + ", last_message_sent_timestamp=" + getLastMessageSentTimestamp()
				+ ", first_message_timestamp=" + getFirstMessageTimestamp() + ", participants=" + participants
				+ ", snippet=" + snippet + ", version=" + version + ", folders=" + folders + ", labels=" + labels
				+ ", has_attachments=" + has_attachments + ", message_ids=" + message_ids + ", draft_ids=" + draft_ids
				+ ", messages=" + messages + ", drafts=" + drafts + "]";
	}
}

