package com.nylas;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.nylas.resources.Authentication;
import com.nylas.util.Maps;
import okhttp3.HttpUrl;

public class NylasApplication {

	private final NylasClient client;
	private final String clientId;
	private final String clientSecret;
	
	NylasApplication(NylasClient client, String clientId, String clientSecret) {
		this.client = client;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}
	
	public NylasClient getClient() {
		return client;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public Authentication authentication() {
		return new Authentication(client, this);
	}
}
