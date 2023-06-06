package com.nylas;

public class NameEmail {
	private String name;
	private String email;
	
	/** for deserialization only */ public NameEmail() {} 
	
	public NameEmail(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "NameEmail [name=" + name + ", email=" + email + "]";
	}
}
