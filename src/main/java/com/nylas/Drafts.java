package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.nylas.NylasClient.HttpMethod;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Drafts extends RestfulCollection<Draft, DraftQuery> {

	Drafts(NylasClient client, String accessToken) {
		super(client, Draft.class, "drafts", accessToken);
	}
	
	/**
	 * Create the given draft on the server.
	 * Returns the new draft object with an assigned ID as returned by the server.
	 * Does not modify the passed in draft object.
	 */
	public Draft create(Draft draft) throws IOException, RequestFailedException {
		return super.create(draft);
	}
	
	/**
	 * Update the given draft on the server.
	 * Requires that the version of the given draft matches the latest version returned by the server.  This can fail
	 * if any client (including this one) has updated the draft since this was fetched from the server.
	 * 
	 * Returns the updated draft object as returned by the server.
	 * @throws RequestFailedException if the version of the given draft does not match the latest version on the server.
	 */
	public Draft update(Draft draft) throws IOException, RequestFailedException {
		return super.update(draft);
	}
	
	/**
	 * Convenience method to create or update the given draft.
	 * If it has an existing ID, update it, otherwise create it.
	 * Returns the draft as returned by the server.
	 */
	public Draft createOrUpdate(Draft draft) throws IOException, RequestFailedException {
		return draft.hasId() ? update(draft) : create(draft);
	}

	/**
	 * Sends the given draft.
	 * 
	 * Note - If the draft was already saved to the server and has an id, then sends using the server's copy.
	 * If there are local changes, first call update(), then send().
	 * In this case, the server returns only a 200 success and no message object
	 * 
	 * If the draft was not saved and has no id, then sends using its fields directly.
	 * 
	 * @return a Message representing the sent message, only if it is sent directly, not from a saved draft
	 */
	public Message send(Draft draft) throws IOException, RequestFailedException {
		Map<String, Object> params;
		Type resultType;
		if (draft.hasId()) {
			params = Maps.of("draft_id", draft.getId(), "version", draft.getVersion());
			resultType = null;
		} else {
			params = draft.getWritableFields(true);
			resultType = Message.class;
		}
		HttpUrl.Builder url = getSendUrl();
		RequestBody jsonBody = JsonHelper.jsonRequestBody(params);
		return client.executeRequestWithAuth(authUser, url, HttpMethod.POST, jsonBody, resultType);
	}

	private HttpUrl.Builder getSendUrl() {
		return getBaseUrlBuilder().addPathSegment("send");
	}

	/**
	 * Sends the given mime message with Content-Type: message/rfc822
	 * @return a Message representing the sent message
	 */
	public Message sendRawMime(String mimeMessage) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getSendUrl();
		RequestBody requestBody = RequestBody.create(MediaType.get("message/rfc822"), mimeMessage);
		return client.executeRequestWithAuth(authUser, url, HttpMethod.POST, requestBody, Message.class);
	}
	
}

