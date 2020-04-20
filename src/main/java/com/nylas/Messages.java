package com.nylas;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class Messages extends RestfulDAO<Message> {

	Messages(NylasClient client, String accessToken) {
		super(client, Message.class, "messages", accessToken);
	}

	public RemoteCollection<Message> list(MessageQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Message get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	public RemoteCollection<Message> expanded(MessageQuery query) throws IOException, RequestFailedException {
		return super.expanded(query);
	}
	
	public RemoteCollection<String> ids(MessageQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}
	
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
		Request.Builder builder = new Request.Builder().url(messageUrl.build()).get();
		client.addAuthHeader(builder, authUser);
		Request request = builder
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
	public Message setLabelIds(String messageId, Collection<String> labelIds)
			throws IOException, RequestFailedException {
		return super.update(messageId, Maps.of("label_ids", labelIds));
	}
	
	private Set<String> getLabelIds(String messageId) throws IOException, RequestFailedException {
		Message message = get(messageId);
		return new HashSet<>(RestfulModel.getIds(message.getLabels()));
	}
	
	/**
	 * Convenience method to add a label to a message without overwriting any of the existing set.
	 * 
	 * First, does a GET for the message to find the existing ids, then adds the new one
	 * (if it is not already present).
	 * @return true if the label was newly added, and false if the message already had the label 
	 */
	public boolean addLabel(String messageId, String labelId) throws IOException, RequestFailedException {
		Set<String> labelIds = getLabelIds(messageId);
		if (labelIds.add(labelId)) {
			setLabelIds(messageId, labelIds);
			return true;
		}
		return false;
	}
	
	/**
	 * Convenience method to remove a label from a message without otherwise changing the existing set.
	 * 
	 * First, does a GET for the message to find the existing ids, then removes the label
	 * (if it is present).
	 * @return true if the label was removed, and false if the message did not have the label 
	 */
	public boolean removeLabel(String messageId, String labelId) throws IOException, RequestFailedException {
		Set<String> labelIds = getLabelIds(messageId);
		if (labelIds.remove(labelId)) {
			setLabelIds(messageId, labelIds);
			return true;
		}
		return false;
	}
	
}
