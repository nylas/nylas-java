package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.nylas.NylasClient.HttpMethod;
import com.squareup.moshi.Types;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class Messages {

	private final NylasClient client;
	private final String accessToken;
	
	Messages(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	private static final Type MESSAGE_LIST_TYPE = Types.newParameterizedType(List.class, Message.class);
	public List<Message> list() throws IOException, RequestFailedException {
		HttpUrl messagesUrl = client.getBaseUrl().resolve("messages");
		return client.executeGetWithToken(accessToken, messagesUrl, MESSAGE_LIST_TYPE);
	}
	
	private static final Type EXPANDED_MESSAGE_LIST_TYPE
		= Types.newParameterizedType(List.class, ExpandedMessage.class);
	public List<ExpandedMessage> expanded(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl messagesUrl = client.getBaseUrl().newBuilder("messages")
				.addQueryParameter("view", "expanded")
				.build();
		List<ExpandedMessage> expandedMessages
			= client.executeGetWithToken(accessToken, messagesUrl, EXPANDED_MESSAGE_LIST_TYPE);
		
		return expandedMessages;
	}
	
	private static final Type STRING_LIST_TYPE = Types.newParameterizedType(List.class, String.class);
	public List<String> ids(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl messagesUrl = client.getBaseUrl().newBuilder("messages")
				.addQueryParameter("view", "ids")
				.build();
		return client.executeGetWithToken(accessToken, messagesUrl, STRING_LIST_TYPE);
	}
	
	public long count(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl messagesUrl = client.getBaseUrl().newBuilder("messages")
				.addQueryParameter("view", "count")
				.build();
		Count count = client.executeGetWithToken(accessToken, messagesUrl, Count.class);
		return count.getCount();
	}
	
	public Message get(String messageId) throws IOException, RequestFailedException {
		HttpUrl messageUrl = getMessageUrl(messageId);
		return client.executeGetWithToken(accessToken, messageUrl, Message.class);
	}
	
	public String getRaw(String messageId) throws IOException, RequestFailedException {
		HttpUrl messageUrl = getMessageUrl(messageId);
		Request.Builder builder = new Request.Builder().url(messageUrl);
		client.addAccessTokenHeader(builder, accessToken);
		Request request = builder.method(HttpMethod.GET.toString(), null)
				.addHeader("Accept", "message/rfc822")
				.build();
		return client.executeRequest(request, String.class);
	}

	/**
	 * Set the unread status for the given message.
	 * 
	 * @return The updated Message as returned by the server.
	 */
	public Message setUnread(String messageId, boolean unread) throws IOException, RequestFailedException {
		return updateMessage(messageId, Maps.of("unread", unread));
	}
	
	/**
	 * Set the starred state for the given thread.
	 * 
	 * @return The updated Message as returned by the server.
	 */
	public Message setStarred(String messageId, boolean starred) throws IOException, RequestFailedException {
		return updateMessage(messageId, Maps.of("starred", starred));
	}
	
	/**
	 * Moves the given thread to the given folder
	 * 
	 * @return The updated Message as returned by the server.
	 */
	public Message setFolderId(String messageId, String folderId) throws IOException, RequestFailedException {
		return updateMessage(messageId, Maps.of("folder_id", folderId));
	}
	
	private Message updateMessage(String messageId, Map<String, Object> params)
			throws IOException, RequestFailedException {
		HttpUrl messageUrl = getMessageUrl(messageId);
		return client.executePutWithToken(accessToken, messageUrl, params, Message.class);
	}
	private HttpUrl getMessageUrl(String messageId) {
		return client.getBaseUrl().newBuilder()
				.addPathSegment("messages")
				.addPathSegment(messageId)
				.build();
	}
	
}
