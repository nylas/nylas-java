package com.nylas;

public class Draft extends Message {

	private String reply_to_message_id;
	private Integer version;
	
	public String getReplyToMessageId() {
		return reply_to_message_id;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		return "Draft [getReplyToMessageId()=" + getReplyToMessageId() + ", getVersion()=" + getVersion() + ", getId()="
				+ getId() + ", getAccountId()=" + getAccountId() + ", getThreadId()=" + getThreadId()
				+ ", getSubject()=" + getSubject() + ", getFrom()=" + getFrom() + ", getTo()=" + getTo() + ", getCc()="
				+ getCc() + ", getBcc()=" + getBcc() + ", getReplyTo()=" + getReplyTo() + ", getDate()=" + getDate()
				+ ", getUnread()=" + getUnread() + ", getStarred()=" + getStarred() + ", getSnippet()=" + getSnippet()
				+ ", getBody()=" + getBody() + ", getFolder()=" + getFolder() + ", getLabels()=" + getLabels() + "]";
	}
	
	
	
}
