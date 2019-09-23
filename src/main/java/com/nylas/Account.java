package com.nylas;

public class Account {

	private String id;
	private String billing_state;
	private String email;
	private String provider;
	private String sync_state;
	private Boolean trial;
	
	public String getId() {
		return id;
	}
	
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
	
	public Boolean getTrial() {
		return trial;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", billing_state=" + billing_state + ", email=" + email + ", provider=" + provider
				+ ", sync_state=" + sync_state + ", trial=" + trial + "]";
	}
	
}
