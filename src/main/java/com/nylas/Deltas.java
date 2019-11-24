package com.nylas;

import java.io.IOException;

import okhttp3.HttpUrl;

public class Deltas extends RestfulCollection<Delta, DeltaQuery> {

	Deltas(NylasClient client, String accessToken) {
		super(client, Delta.class, "delta", accessToken);
	}

	public String fetchLatestCursor() throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("latest_cursor");
		Cursor cursor = client.executePost(authUser, url, null, Cursor.class);
		return cursor.getCursor();
	}
	
	public DeltaSet getDeltas(String cursor) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl().addQueryParameter("cursor", cursor);
		return client.executeGet(authUser, url, DeltaSet.class);
	}

	
}
