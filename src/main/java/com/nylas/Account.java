package com.nylas;

public class Account {

	private final NylasClient nylasClient;
	private final String accessToken;
	
	public Account(NylasClient nylasClient, String accessToken) {
		this.nylasClient = nylasClient;
		this.accessToken = accessToken;
	}

	
	
}
