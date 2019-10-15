package com.nylas;

public class AccountDetail {

	private String id;
	private String name;
	private String email_address;
	private String provider;
	private String organization_unit;
	private String sync_state;
	private Long linked_at;
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmailAddress() {
		return email_address;
	}
	
	public String getProvider() {
		return provider;
	}
	
	public String getOrganizationUnit() {
		return organization_unit;
	}
	
	public String getSyncState() {
		return sync_state;
	}
	
	public Long getLinkedAt() {
		return linked_at;
	}

	@Override
	public String toString() {
		return "AccountDetail [id=" + id + ", name=" + name + ", email_address=" + email_address + ", provider=" + provider
				+ ", organization_unit=" + organization_unit + ", sync_state=" + sync_state + ", linked_at=" + linked_at
				+ "]";
	}
	
	
}
