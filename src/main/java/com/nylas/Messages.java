package com.nylas;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.nylas.NylasClient.HttpMethod;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class Messages extends RestfulCollection<Message, MessageQuery> {

	Messages(NylasClient client, String accessToken) {
		super(client, Message.class, "messages", accessToken);
	}

	public List<ExpandedMessage> expanded(MessageQuery query) throws IOException, RequestFailedException {
		return super.expanded(query, ExpandedMessage.class);
	}
	
	@Override
	public List<String> ids(MessageQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}
	
	@Override
	public long count(MessageQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	public List<Message> search(String query) throws IOException, RequestFailedException {
		return super.search(new SearchQuery(query));
	}
	
	public List<Message> search(String query, int limit, int offset) throws IOException, RequestFailedException {
		return super.search(new SearchQuery(query, limit, offset));
	}
	
	public String getRaw(String messageId) throws IOException, RequestFailedException {
		HttpUrl.Builder messageUrl = getInstanceUrl(messageId);
		Request.Builder builder = new Request.Builder().url(messageUrl.build());
		client.addAuthHeader(builder, authUser);
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
		return super.update(messageId, Maps.of("unread", unread));
	}
	
	/**
	 * Set the starred state for the given thread.
	 * 
	 * @return The updated Message as returned by the server.
	 */
	public Message setStarred(String messageId, boolean starred) throws IOException, RequestFailedException {
		return super.update(messageId, Maps.of("starred", starred));
	}
	
	/**
	 * Moves the given thread to the given folder
	 * 
	 * @return The updated Message as returned by the server.
	 */
	public Message setFolderId(String messageId, String folderId) throws IOException, RequestFailedException {
		return super.update(messageId, Maps.of("folder_id", folderId));
	}
	
	/**
	 * Updates the labels on the given message, overwriting all previous labels on the message.
	 */
	public Message setLabelIds(String threadId, Collection<String> labelIds)
			throws IOException, RequestFailedException {
		return super.update(threadId, Maps.of("label_ids", labelIds));
	}
	
}
