package com.nylas;

import java.util.Map;

/**
 * An Account as managed by the Accounts access object for a particular application client_id.
 */
public class Account extends AccountOwnedModel {

	private String billing_state;
	private String email;
	private String provider;
	private String sync_state;
	private String authentication_type;
	private Boolean trial;
	private Map<String, String> metadata;
	
	public String getBillingState() {
		return billing_state;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getProvider() {
		return provider;
	}
	
	public String getSyncState() {
		return sync_state;
	}

	public String getAuthenticationType() {
		return authentication_type;
	}
	
	public Boolean getTrial() {
		return trial;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return "Account [" +
				"id='" + getId() + '\'' +
				", billing_state='" + billing_state + '\'' +
				", email='" + email + '\'' +
				", provider='" + provider + '\'' +
				", sync_state='" + sync_state + '\'' +
				", authentication_type='" + authentication_type + '\'' +
				", trial=" + trial +
				", metadata=" + metadata +
				']';
	}
}
