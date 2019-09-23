package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.Types;

import okhttp3.HttpUrl;

/**
 * @see https://docs.nylas.com/reference#labels
 */
public class Labels {
	
	private final NylasClient client;
	private final String accessToken;
	
	Labels(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}
	
	private static final Type LABEL_LIST_TYPE = Types.newParameterizedType(List.class, Label.class);
	public List<Label> list() throws IOException, RequestFailedException {
		HttpUrl url = client.getBaseUrl().resolve("labels");
		return client.executeGet(accessToken, url, LABEL_LIST_TYPE);
	}
	
	public Label get(String labelId) throws IOException, RequestFailedException {
		HttpUrl url = getLabelUrl(labelId);
		return client.executeGet(accessToken, url, Label.class);
	}
	
	public Label create(String displayName) throws IOException, RequestFailedException {
		HttpUrl url = client.getBaseUrl().resolve("labels");
		Map<String, Object> params = Maps.of("display_name", displayName);
		return client.executePost(accessToken, url, params, Label.class);
	}
	
	public void delete(String labelId) throws IOException, RequestFailedException {
		HttpUrl url = getLabelUrl(labelId);
		client.executeDelete(accessToken, url, null);
	}
	
	/**
	 * Change the display name of the given label.
	 * These changes will propagate back to the account provider.
	 * Note that the core labels such as Inbox, Trash, etc. cannot be renamed.
	 */
	public Label setDisplayName(String labelId, String displayName) throws IOException, RequestFailedException {
		return updateLabel(labelId, Maps.of("display_name", displayName));
	}
	
	private Label updateLabel(String labelId, Map<String, Object> params)
			throws IOException, RequestFailedException {
		HttpUrl url = getLabelUrl(labelId);
		return client.executePut(accessToken, url, params, Label.class);
	}
	
	private HttpUrl getLabelUrl(String labelId) {
		return client.getBaseUrl().newBuilder()
				.addPathSegment("labels")
				.addPathSegment(labelId)
				.build();
	}
}
