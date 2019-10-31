package com.nylas;

import java.util.Collections;
import java.util.List;

public class Thread extends AccountOwnedModel {

	private String subject;
	private boolean unread;
	private boolean starred;
	private Long last_message_timestamp;
	private Long last_message_received_timestamp;
	private Long last_message_sent_timestamp;
	private Long first_message_timestamp;
	private List<NameEmail> participants = Collections.emptyList();
	private String snippet;
	private List<String> message_ids = Collections.emptyList();
	private List<String> draft_ids = Collections.emptyList();
	private int version;
	private List<Folder> folders = Collections.emptyList();
	private List<Label> labels = Collections.emptyList();
	private boolean has_attachments;
	
	public String getSubject() {
		return subject;
	}

	public boolean isUnread() {
		return unread;
	}

	public boolean isStarred() {
		return starred;
	}

	public Long getLastMessageTimestamp() {
		return last_message_timestamp;
	}

	public Long getLastMessageReceivedTimestamp() {
		return last_message_received_timestamp;
	}

	public Long getLastMessageSentTimestamp() {
		return last_message_sent_timestamp;
	}

	public Long getFirstMessageTimestamp() {
		return first_message_timestamp;
	}

	public List<NameEmail> getParticipants() {
		return participants;
	}

	public String getSnippet() {
		return snippet;
	}

	public List<String> getMessageIds() {
		return message_ids;
	}

	public List<String> getDraftIds() {
		return draft_ids;
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

	@Override
	public String toString() {
		return "Thread [id=" + getId() + ", account_id=" + getAccountId() + ", subject=" + subject + ", unread=" + unread
				+ ", starred=" + starred + ", last_message_timestamp=" + last_message_timestamp
				+ ", last_message_received_timestamp=" + last_message_received_timestamp
				+ ", last_message_sent_timestamp=" + last_message_sent_timestamp + ", first_message_timestamp="
				+ first_message_timestamp + ", participants=" + participants + ", snippet=" + snippet + ", message_ids="
				+ message_ids + ", draft_ids=" + draft_ids + ", version=" + version + ", folders=" + folders
				+ ", has_attachments=" + has_attachments + "]";
	}
}

