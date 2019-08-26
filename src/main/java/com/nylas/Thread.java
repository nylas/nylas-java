package com.nylas;

import java.util.List;

public class Thread {

	private String id;
	private String account_id;
	private String subject;
	private boolean unread;
	private boolean starred;
	private Long last_message_timestamp;
	private Long last_message_received_timestamp;
	private Long last_message_sent_timestamp;
	private Long first_message_timestamp;
	private List<Participant> participants;
	private String snippet;
	private List<String> message_ids;
	private List<String> draft_ids;
	private int version;
	private List<Folder> folders;
	private List<Label> labels;
	private boolean has_attachments;
	
	public String getId() {
		return id;
	}

	public String getAccountId() {
		return account_id;
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

	public List<Participant> getParticipants() {
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
		return "Thread [id=" + id + ", account_id=" + account_id + ", subject=" + subject + ", unread=" + unread
				+ ", starred=" + starred + ", last_message_timestamp=" + last_message_timestamp
				+ ", last_message_received_timestamp=" + last_message_received_timestamp
				+ ", last_message_sent_timestamp=" + last_message_sent_timestamp + ", first_message_timestamp="
				+ first_message_timestamp + ", participants=" + participants + ", snippet=" + snippet + ", message_ids="
				+ message_ids + ", draft_ids=" + draft_ids + ", version=" + version + ", folders=" + folders
				+ ", has_attachments=" + has_attachments + "]";
	}

	public static class Participant {
		private String name;
		private String email;
		
		@Override
		public String toString() {
			return "Participant [name=" + name + ", email=" + email + "]";
		}
	}

	
	// TODO - how is label different from FoldeR?
	public static class Label {
		private String id;
		private String name;
		private String display_name;
		
		@Override
		public String toString() {
			return "Label [id=" + id + ", name=" + name + ", display_name=" + display_name + "]";
		}
	}
}

