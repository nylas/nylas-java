package com.nylas;

import java.io.IOException;

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
	
	public HostedAuthentication hostedAuthentication() {
		return new HostedAuthentication(this);
	}

	public NativeAuthentication nativeAuthentication() {
		return new NativeAuthentication(this);
	}
	
	public Accounts accounts() {
		return new Accounts(client, this);
	}
	
	public IPAddressWhitelist fetchIpAddressWhitelist() throws IOException, RequestFailedException {
		HttpUrl.Builder url = client.newUrlBuilder()
				.addPathSegment("a")
				.addPathSegment(clientId)
				.addPathSegment("ip_addresses");
		return client.executeGet(clientSecret, url, IPAddressWhitelist.class);
	}

}
