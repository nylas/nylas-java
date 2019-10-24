package com.nylas;

public class ContactGroup extends AccountOwnedModel {

	private String name;
	private String path;

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "ContactGroup [name=" + name + ", path=" + path + ", accountId=" + getAccountId() + ", id="
				+ getId() + "]";
	}

	
}
