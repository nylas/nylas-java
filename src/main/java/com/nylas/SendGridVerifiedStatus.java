package com.nylas;

public class SendGridVerifiedStatus {

	private boolean domain_verified;
	private boolean sender_verified;

	public boolean isDomainVerified() {
		return domain_verified;
	}

	public boolean isSenderVerified() {
		return sender_verified;
	}

	@Override
	public String toString() {
		return "SendGridVerifiedStatus [" + "domain_verified=" + domain_verified + ", sender_verified="
			+ sender_verified + ']';
	}
}
