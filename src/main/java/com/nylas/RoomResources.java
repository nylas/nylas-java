package com.nylas;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class RoomResources {
	protected final String resourcesPath = "resources";
	protected final NylasClient client;
	protected final String accessToken;

	RoomResources(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	/**
	 * Lists all the room resources associated with the account.
	 * Note that currently this is the only operation that room resources supports
	 */
	public List<RoomResource> list() throws IOException, RequestFailedException {
		HttpUrl.Builder url = client.newUrlBuilder().addPathSegment(resourcesPath);
		Type listType = JsonHelper.listTypeOf(RoomResource.class);
		return client.executeGet(accessToken, url, listType);
	}
}
