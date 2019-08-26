package com.nylas;

import java.util.Date;

import com.squareup.moshi.JsonAdapter;

public class Account {

	private String id;
	private String name;
	private String emailAddress;
	private String provider;
	private String organizationUnit;
	private String syncState;
	private Date linkedAt;
	
	public Account(JsonBean account) {
		this.id = account.id;
		this.name = account.name;
		this.emailAddress = account.email_address;
		this.provider = account.provider;
		this.organizationUnit = account.organization_unit;
		this.syncState = account.sync_state;
		this.linkedAt = new Date(account.linked_at * 1000L);
	}
	
	
	
	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + ", emailAddress=" + emailAddress + ", provider=" + provider
				+ ", organizationUnit=" + organizationUnit + ", syncState=" + syncState + ", linkedAt=" + linkedAt
				+ "]";
	}



	static class JsonBean {
		
		static final JsonAdapter<JsonBean> ADAPTER = JsonHelper.moshi().adapter(JsonBean.class);
		
		String id;
		String object;
		String account_id;
		String name;
		String email_address;
		String provider;
		String organization_unit;
		String sync_state;
		long linked_at;
	}
}
