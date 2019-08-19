package com.nylas;

public class AccessToken {

	private String access_token;
	private String account_id;
	private String email_address;
	private String provider;
	private String token_type;

	public String getAccessToken() {
		return access_token;
	}

	public String getAccountId() {
		return account_id;
	}

	public String getEmailAddress() {
		return email_address;
	}

	public String getProvider() {
		return provider;
	}

	public String getTokenType() {
		return token_type;
	}

	@Override
	public String toString() {
		return "AccessToken [access_token=" + access_token + ", account_id=" + account_id + ", email_address="
				+ email_address + ", provider=" + provider + ", token_type=" + token_type + "]";
	}

}
