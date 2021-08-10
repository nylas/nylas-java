package com.nylas;

import java.util.List;
import java.util.Map;

public class EventConferencing {
	private String provider;
	private Details details;

	/** For deserialiation only */ public EventConferencing() {}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "EventConferencing [provider=" + provider + ", details=" + details + "]";
	}

	public static class Details {
		private String url;
		private String password;
		private String pin;
		private String meeting_code;
		private List<String> phone;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPin() {
			return pin;
		}

		public void setPin(String pin) {
			this.pin = pin;
		}

		public String getMeetingCode() {
			return meeting_code;
		}

		public void setMeetingCode(String meetingCode) {
			this.meeting_code = meetingCode;
		}

		public List<String> getPhone() {
			return phone;
		}

		public void setPhone(List<String> phone) {
			this.phone = phone;
		}

		@Override
		public String toString() {
			return String.format("EventConferencingDetails [url=%s, password=%s, pin=%s, meeting_code=%s, phone=%s]",
					url, password, pin, meeting_code, phone);
		}
	}
}
