package com.nylas;

public class Application {

	private final NylasClient client;
	private final String clientId;
	private final String clientSecret;
	
	Application(NylasClient client, String clientId, String clientSecret) {
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

}
