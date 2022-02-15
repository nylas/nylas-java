package com.nylas;

import com.nylas.delta.Delta;
import com.nylas.delta.DeltaCursor;
import com.nylas.delta.DeltaStreamListener;
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

	public String latestCursor() throws RequestFailedException, IOException {
		Map<String, String> response = client.executePost(accessToken, deltaEndpoint().addPathSegment("latest_cursor"), null, Map.class);
		String cursor = response.get("cursor");
		if(cursor == null) {
			throw new RuntimeException("Unexpected response from the API server. Returned 200 but no 'cursor' string found.");
		}

		return cursor;
	}

	public DeltaCursor since(String cursor) throws RequestFailedException, IOException {
		HttpUrl.Builder url = deltaEndpoint().addQueryParameter("cursor", cursor);
		return client.executeGet(accessToken, url, DeltaCursor.class);
	}

	private HttpUrl.Builder deltaEndpoint() {
		return client.newUrlBuilder().addPathSegment("delta");
	}
}
