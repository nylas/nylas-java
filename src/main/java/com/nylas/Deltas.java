package com.nylas;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.Map;

public class Deltas {

	private final NylasClient client;
	private final String accessToken;

	public Deltas(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	private HttpUrl.Builder deltaEndpoint() {
		return client.newUrlBuilder().addPathSegment("delta");
	}
}
