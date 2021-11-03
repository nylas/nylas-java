package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.nylas.NylasClient.HttpMethod;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Drafts extends RestfulDAO<Draft> {

	private String sendEndpoint = "send";
	
	Drafts(NylasClient client, String accessToken) {
		super(client, Draft.class, "drafts", accessToken);
	}
	
	public RemoteCollection<Draft> list() throws IOException, RequestFailedException {
		return list(new DraftQuery());
	}
	
	public RemoteCollection<Draft> list(DraftQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Draft get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	/**
	 * Create the given draft on the server.
	 * Returns the new draft object with an assigned ID as returned by the server.
	 * Does not modify the passed in draft object.
	 */
	@Override
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
	@Override
	public Draft update(Draft draft) throws IOException, RequestFailedException {
		return super.update(draft);
	}
	
	/**
	 * Convenience method to create or update the given draft.
	 * If it has an existing ID, update it, otherwise create it.
	 * Returns the draft as returned by the server.
	 */
	public Draft save(Draft draft) throws IOException, RequestFailedException {
		return draft.hasId() ? update(draft) : create(draft);
	}

	/**
	 * Delete the given draft from the server.
	 * Requires that the version of the given draft matches the latest version on the server.
	 * This can fail if any client (including this one) has updated the draft since this was fetched from the server.
	 */
	public void delete(Draft draft) throws IOException, RequestFailedException {
		if (!draft.hasId()) {
			throw new IllegalArgumentException("Cannot delete a draft that has not yet been created.");
		}
		RequestBody jsonBody = JsonHelper.jsonRequestBody(Maps.of("version", draft.getVersion()));
		client.executeRequestWithAuth(authUser, getInstanceUrl(draft.getId()), HttpMethod.DELETE, jsonBody, null);
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
		if (draft.getTracking() != null) {
			params = new HashMap<>(params);  // mutable copy
			params.put("tracking", draft.getTracking());
		}
		HttpUrl.Builder url = getSendUrl();
		RequestBody jsonBody = JsonHelper.jsonRequestBody(params);
		return client.executeRequestWithAuth(authUser, url, HttpMethod.POST, jsonBody, resultType);
	}

	/**
	 * Sends the given mime message with Content-Type: message/rfc822
	 * @return a Message representing the sent message
	 */
	public Message sendRawMime(String mimeMessage) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getSendUrl();
		/*
		 * The server has an issue where if the content-type has a charset, for example:
		 *   Content-Type: message/rfc822; charset=utf-8
		 *   
		 * Then it responds with this error:
		 *  {"message":"400 Bad Request: The browser (or proxy) sent a request that this server could not understand.",
		 *  "type":"api_error"}
		 * 
		 * To workaround that, we pre-encode the text to utf-8. Otherwise, when OkHttp encodes to utf-8
		 * it also sets the charset, which causes the server error above.
		 */
		byte[] messageBytes = mimeMessage.getBytes(StandardCharsets.UTF_8);
		RequestBody requestBody = RequestBody.create(MediaType.get("message/rfc822"), messageBytes);
		return client.executeRequestWithAuth(authUser, url, HttpMethod.POST, requestBody, Message.class);
	}

	public void setSendEndpoint(String sendEndpoint) {
		this.sendEndpoint = sendEndpoint;
	}
	
	private HttpUrl.Builder getSendUrl() {
		return client.newUrlBuilder().addPathSegment(sendEndpoint);
	}
	
}

