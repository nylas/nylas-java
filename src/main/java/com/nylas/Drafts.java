package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.squareup.moshi.Types;

import okhttp3.HttpUrl;

public class Drafts {

	private final NylasClient client;
	private final String accessToken;
	
	Drafts(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	private static final Type DRAFT_LIST_TYPE = Types.newParameterizedType(List.class, Draft.class);
	public List<Draft> list(DraftsQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getDraftsUrl(query, null);
		return client.executeGet(accessToken, url, DRAFT_LIST_TYPE);
	}
	
	private HttpUrl getDraftsUrl(DraftsQuery query, String view) {
		HttpUrl.Builder urlBuilder = client.getBaseUrl().newBuilder("drafts");
		if (query != null) {
			query.addParameters(urlBuilder);
		}
		if (view != null) {
			urlBuilder.addQueryParameter("view", view);
		}
		return urlBuilder.build();
	}
}
