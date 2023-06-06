package com.nylas;

import java.io.IOException;

import com.nylas.resources.Calendars;
import com.nylas.resources.Events;
import okhttp3.HttpUrl;

public class NylasAccount {

	private final NylasClient client;
	private final String accessToken;
	
	NylasAccount(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	public NylasClient getClient() {
		return client;
	}

	public String getAccessToken() {
		return accessToken;
	}
	
	public Calendars calendars() {
		return new Calendars(client, accessToken);
	}
	
	public Events events() {
		return new Events(client, accessToken);
	}
}
