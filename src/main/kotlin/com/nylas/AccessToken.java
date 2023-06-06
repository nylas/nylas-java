package com.nylas;

/**
 * Access token information for an account returned by Nylas after exchanging the authorization code.
 */
public class AccessToken {

	private String access_token;
	private String account_id;
	private String email_address;
	private String provider;

	/**
	 * The actual token. This does not expire and should be saved carefully by the application.
	 */
	public String getAccessToken() {
		return access_token;
	}

	/**
	 * The ID of the account that this token grants access to. 
	 */
	public String getAccountId() {
		return account_id;
	}

	/**
	 * The email address of the account.
	 */
	public String getEmailAddress() {
		return email_address;
	}

	public String getProvider() {
		return provider;
	}

	@Override
	public String toString() {
		return "AccessToken [access_token=" + access_token + ", account_id=" + account_id + ", email_address="
				+ email_address + ", provider=" + provider + "]";
	}

}
