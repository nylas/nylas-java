package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
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
	public List<Message> list(MessageQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getMessagesUrl(query, null);
		return client.executeGet(accessToken, url, MESSAGE_LIST_TYPE);
	}
	
	private static final Type EXPANDED_MESSAGE_LIST_TYPE
		= Types.newParameterizedType(List.class, ExpandedMessage.class);
	public List<ExpandedMessage> expanded(MessageQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getMessagesUrl(query, "expanded");
		List<ExpandedMessage> expandedMessages
			= client.executeGet(accessToken, url, EXPANDED_MESSAGE_LIST_TYPE);
		return expandedMessages;
	}
	
	private static final Type STRING_LIST_TYPE = Types.newParameterizedType(List.class, String.class);
	public List<String> ids(MessageQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getMessagesUrl(query, "ids");
		return client.executeGet(accessToken, url, STRING_LIST_TYPE);
	}
	
	public long count(MessageQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getMessagesUrl(query, "count");
		Count count = client.executeGet(accessToken, url, Count.class);
		return count.getCount();
	}
	
	public Message get(String messageId) throws IOException, RequestFailedException {
		HttpUrl messageUrl = getMessageUrl(messageId);
		return client.executeGet(accessToken, messageUrl, Message.class);
	}
	
	public String getRaw(String messageId) throws IOException, RequestFailedException {
		HttpUrl messageUrl = getMessageUrl(messageId);
		Request.Builder builder = new Request.Builder().url(messageUrl);
		client.addAuthHeader(builder, accessToken);
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
	
	/**
	 * Updates the labels on the given message, overwriting all previous labels on the message.
	 */
	public Message setLabelIds(String threadId, Collection<String> labelIds) throws IOException, RequestFailedException {
		return updateMessage(threadId, Maps.of("label_ids", labelIds));
	}
	
	private Message updateMessage(String messageId, Map<String, Object> params)
			throws IOException, RequestFailedException {
		HttpUrl messageUrl = getMessageUrl(messageId);
		return client.executePut(accessToken, messageUrl, params, Message.class);
	}
	
	private HttpUrl getMessagesUrl(MessageQuery query, String view) {
		HttpUrl.Builder urlBuilder = client.getBaseUrl().newBuilder("messages");
		if (query != null) {
			query.addParameters(urlBuilder);
		}
		if (view != null) {
			urlBuilder.addQueryParameter("view", view);
		}
		return urlBuilder.build();
	}
	
	private HttpUrl getMessageUrl(String messageId) {
		return client.getBaseUrl().newBuilder()
				.addPathSegment("messages")
				.addPathSegment(messageId)
				.build();
	}
	
}
