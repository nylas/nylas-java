package com.nylas;

import java.util.Date;
import java.util.Map;

public class OutboxMessage extends Draft {

	private Long send_at;
	private Long retry_limit_datetime;
	private Long original_send_at;

	public OutboxMessage() {
	}

	public OutboxMessage(Draft draft) {
		this.to = draft.to;
		this.from = draft.from;
		this.subject = draft.subject;
		this.cc = draft.cc;
		this.bcc = draft.bcc;
		this.reply_to = draft.reply_to;
		this.body = draft.body;
		this.files = draft.files;
		this.reply_to_message_id = draft.reply_to_message_id;
		this.thread_id = draft.thread_id;
		this.metadata = draft.metadata;
		this.tracking = draft.tracking;
	}

	public Long getSendAt() {
		return send_at;
	}

	public Long getRetryLimitDatetime() {
		return retry_limit_datetime;
	}

	public Long getOriginalSendAt() {
		return original_send_at;
	}

	/**
	 * The date and time to send the message. If set to 0, Outbox will send this message immediately.
	 * @param sendAt The Unix timestamp to send the message
	 */
	public void setSendAt(Long sendAt) {
		this.send_at = sendAt;
	}

	/**
	 * The date and time to send the message.
	 */
	public void setSendAt(Date sendAt) {
		this.send_at = sendAt.getTime() / 1000;
	}

	/**
	 * Optional date and time to stop retry attempts for a message.
	 * If not set, it would stop retrying after 24 hours from the {@code send_at} date.
	 * If set to 0, Outbox will not retry to send the message.
	 * The maximum value set is 7 days from the scheduled time.
	 * @param retryLimitDatetime The Unix timestamp to stop retry attempts for the message
	 */
	public void setRetryLimitDatetime(Long retryLimitDatetime) {
		this.retry_limit_datetime = retryLimitDatetime;
	}

	/**
	 * Optional date and time to stop retry attempts for a message.
	 * If not set, it would stop retrying after 24 hours from the {@code send_at} date.
	 * The maximum value set is 7 days from the scheduled time.
	 */
	public void setRetryLimitDatetime(Date retryLimitDatetime) {
		this.retry_limit_datetime = retryLimitDatetime.getTime() / 1000;
	}

	protected void validateFields() {
		Date sendAt;
		if(send_at == null || send_at == 0) {
			sendAt = new Date();
		} else {
			sendAt = new Date(send_at * 1000);
			if(sendAt.before(new Date())) {
				throw new IllegalArgumentException("Cannot set message to be sent at a time before the current time.");
			}
		}

		if(retry_limit_datetime != null && retry_limit_datetime != 0) {
			Date retryLimitDatetime = new Date(retry_limit_datetime * 1000);
			if(retryLimitDatetime.before(sendAt)) {
				throw new IllegalArgumentException("Cannot set message to stop retrying before time to send at.");
			}
		}
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = super.getWritableFields(creation);
		Maps.putIfNotNull(params, "send_at", send_at);
		Maps.putIfNotNull(params, "retry_limit_datetime", retry_limit_datetime);
		return params;
	}

	@Override
	public String toString() {
		return "OutboxMessage [reply_to_message_id=" + reply_to_message_id + ", version=" + version + ", account_id="
			+ getAccountId() + ", thread_id=" + thread_id + ", subject=" + subject + ", from=" + from + ", to=" + to
			+ ", cc=" + cc + ", bcc=" + bcc + ", reply_to=" + reply_to + ", date=" + date + ", unread=" + unread
			+ ", starred=" + starred + ", snippet=" + snippet + ", body=" + body + ", files=" + files + ", folder="
			+ folder + ", labels=" + labels + ", tracking=" + tracking + ", metadata=" + metadata + ", send_at="
			+ send_at + ", retry_limit_datetime=" + retry_limit_datetime + "]";
	}
}
