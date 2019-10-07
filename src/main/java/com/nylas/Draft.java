package com.nylas;

import java.util.Arrays;
import java.util.List;

public class Draft extends Message {

	private String reply_to_message_id;
	private Integer version;
	private Tracking tracking;  // used only for direct sending
	
	public String getReplyToMessageId() {
		return reply_to_message_id;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	/**
	 * The subject line of the draft.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * A single name and email pair, to set as the “from” header.
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
	 * The full HTML draft body text.
	 */
	public void setBody(String body) {
		this.body = body;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
	
	public void setReplyToMessageId(String replyToMessageId) {
		this.reply_to_message_id = replyToMessageId;
	}

	/**
	 * Set tracking options for this email.
	 */
	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
	}
	

	@Override
	public String toString() {
		return "Draft [reply_to_message_id=" + reply_to_message_id + ", version=" + version + ", account_id="
				+ account_id + ", thread_id=" + thread_id + ", subject=" + subject + ", from=" + from + ", to=" + to
				+ ", cc=" + cc + ", bcc=" + bcc + ", reply_to=" + reply_to + ", date=" + date + ", unread=" + unread
				+ ", starred=" + starred + ", snippet=" + snippet + ", body=" + body + ", files=" + files + ", folder="
				+ folder + ", labels=" + labels + "]";
	}


	
	
	
}
