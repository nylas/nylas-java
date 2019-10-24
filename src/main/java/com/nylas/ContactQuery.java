package com.nylas;

import okhttp3.HttpUrl;

public class ContactQuery extends RestfulQuery<ContactQuery> {

	private String email;
	private String phoneNumber;
	private String streetAddress;
	private String postalCode;
	private String state;
	private String country;
	private String source;
	private String groupId;
	private Boolean recurse;
	
	public void addParameters(HttpUrl.Builder url) {
		super.addParameters(url);  // must call through
		
		if (email != null) {
			url.addQueryParameter("email", email);
		}
		if (phoneNumber != null) {
			url.addQueryParameter("phone_number", phoneNumber);
		}
		if (streetAddress != null) {
			url.addQueryParameter("street_address", streetAddress);
		}
		if (postalCode != null) {
			url.addQueryParameter("postal_code", postalCode);
		}
		if (state != null) {
			url.addQueryParameter("state", state);
		}
		if (country != null) {
			url.addQueryParameter("country", country);
		}
		if (source != null) {
			url.addQueryParameter("source", source);
		}
		if (groupId != null) {
			url.addQueryParameter("group", groupId);
		}
		if (recurse != null) {
			url.addQueryParameter("recurse", recurse.toString());
		}
	}
	
	public ContactQuery email(String email) {
		this.email = email;
		return this;
	}
	
	public ContactQuery phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}
	
	public ContactQuery streetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
		return this;
	}
	
	public ContactQuery postalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}
	
	public ContactQuery state(String state) {
		this.state = state;
		return this;
	}
	
	public ContactQuery country(String country) {
		this.country = country;
		return this;
	}
	
	public ContactQuery source(String source) {
		this.source = source;
		return this;
	}
	
	public ContactQuery groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}
	
	public ContactQuery recurse(Boolean recurse) {
		this.recurse = recurse;
		return this;
	}
}
