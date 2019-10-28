package com.nylas;

import java.util.Collections;
import java.util.Map;

public class ExpandedMessage extends Message {

	private Map<String, Object> headers = Collections.emptyMap();

	public Map<String, Object> getHeaders() {
		return headers;
	}

	@Override
	public String toString() {
		return "ExpandedMessage [headers=" + headers + ", id=" + getId() + ", account_id=" + getAccountId() + ", thread_id="
				+ thread_id + ", subject=" + subject + ", from=" + from + ", to=" + to + ", cc=" + cc + ", bcc=" + bcc
				+ ", reply_to=" + reply_to + ", date=" + date + ", unread=" + unread + ", starred=" + starred
				+ ", snippet=" + snippet + ", body.length=" + body.length() + "]";
	}
	
}
