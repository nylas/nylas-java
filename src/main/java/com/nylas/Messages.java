package com.nylas;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nylas.NylasClient.HttpHeaders;
import com.nylas.NylasClient.MediaType;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class Messages extends RestfulDAO<Message> {

	Messages(NylasClient client, String accessToken) {
		super(client, Message.class, "messages", accessToken);
	}

	public RemoteCollection<Message> list() throws IOException, RequestFailedException {
		return list(new MessageQuery());
	}
	
	public RemoteCollection<Message> list(MessageQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	/**
	 * Get a single message by ID
	 * @param id The message ID
	 * @return The requested message
	 */
	@Override
	public Message get(String id) throws IOException, RequestFailedException {
		return get(id, false);
	}

	/**
	 * Get a single message by ID
	 * @param id The message ID
	 * @param expanded If true, the message will return with additional RFC2822 headers
	 * @return The requested message
	 */
	public Message get(String id, boolean expanded) throws IOException, RequestFailedException {
		if(expanded) {
			setView(getCollectionUrl(), "expanded");
		}
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
	
	public RemoteCollection<Message> search(String query) throws IOException, RequestFailedException {
		return super.search(new SearchQuery(query));
	}
	
	public RemoteCollection<Message> search(String query, int limit, int offset) throws IOException, RequestFailedException {
		return super.search(new SearchQuery(query, limit, offset));
	}
	
	public String getRaw(String messageId) throws IOException, RequestFailedException {
		HttpUrl.Builder messageUrl = getInstanceUrl(messageId);
		Request.Builder builder = new Request.Builder().url(messageUrl.build()).get();
		client.addAuthHeader(builder, authUser);
		Request request = builder
				.addHeader(HttpHeaders.ACCEPT.name(), MediaType.MESSAGE_RFC822.getName())
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

	/**
	 * Updates the metadata on the given message, overwriting all previous metadata on the message.
	 */
	public Message setMetadata(String messageId, Map<String, String> metadata)
			throws IOException, RequestFailedException {
		return super.update(messageId, Maps.of("metadata", metadata));
	}

	private Map<String, String> getMetadata(String messageId) throws IOException, RequestFailedException {
		Message message = get(messageId);
		return message.getMetadata();
	}

	/**
	 * Convenience method to add a metadata pair to a message.
	 *
	 * @return true if the metadata was newly added, and false if overwriteIfExists is false the metadata key already exists
	 */
	public boolean addMetadata(String messageId, String key, String value, boolean overwriteIfExists)
			throws IOException, RequestFailedException {
		Map<String, String> metadata = getMetadata(messageId);
		if(!overwriteIfExists && metadata.containsKey(key)) {
			return false;
		}
		metadata.put(key, value);
		setMetadata(messageId, metadata);
		return true;
	}

	public boolean addMetadata(String messageId, String key, String value) throws IOException, RequestFailedException {
		return addMetadata(messageId, key, value, true);
	}

	/**
	 * Convenience method to remove a metadata pair from a message.
	 *
	 * @return true if the metadata pair was removed, and false if the message did not have the metadata key
	 */
	public boolean removeMetadata(String messageId, String key) throws IOException, RequestFailedException {
		Map<String, String> metadata = getMetadata(messageId);
		if(metadata.remove(key) != null) {
			setMetadata(messageId, metadata);
			return true;
		}
		return false;
	}
	
}
