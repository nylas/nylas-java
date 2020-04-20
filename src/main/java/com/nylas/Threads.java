package com.nylas;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Threads extends RestfulDAO<Thread> {

	Threads(NylasClient client, String accessToken) {
		super(client, Thread.class, "threads", accessToken);
	}
	
	public RemoteCollection<Thread> list(ThreadQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Thread get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	public RemoteCollection<Thread> expanded(ThreadQuery query) throws IOException, RequestFailedException {
		return super.expanded(query);
	}
	
	public RemoteCollection<String> ids(ThreadQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}
	
	public long count(ThreadQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	public RemoteCollection<Thread> search(String query) throws IOException, RequestFailedException {
		return super.search(new SearchQuery(query));
	}
	
	public RemoteCollection<Thread> search(String query, int limit, int offset) throws IOException, RequestFailedException {
		return super.search(new SearchQuery(query, limit, offset));
	}
	
	/**
	 * Set the unread status for the given thread.
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setUnread(String threadId, boolean unread) throws IOException, RequestFailedException {
		return super.update(threadId, Maps.of("unread", unread));
	}
	
	/**
	 * Set the starred state for the given thread.
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setStarred(String threadId, boolean starred) throws IOException, RequestFailedException {
		return super.update(threadId, Maps.of("starred", starred));
	}
	
	/**
	 * Moves the given thread to the given folder
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setFolderId(String threadId, String folderId) throws IOException, RequestFailedException {
		return super.update(threadId, Maps.of("folder_id", folderId));
	}
	
	/**
	 * Updates the labels on the given thread, overwriting all previous labels on the thread.
	 */
	public Thread setLabelIds(String threadId, Iterable<String> labelIds) throws IOException, RequestFailedException {
		return super.update(threadId, Maps.of("label_ids", String.join(",", labelIds)));
	}
	
	private Set<String> getLabelIds(String threadId) throws IOException, RequestFailedException {
		Thread thread = get(threadId);
		return new HashSet<>(RestfulModel.getIds(thread.getLabels()));
	}
	
	/**
	 * Convenience method to add a label to a thread without overwriting any of the existing set.
	 * 
	 * First, does a GET for the thread to find the existing ids, then adds the new one
	 * (if it is not already present).
	 * @return true if the label was newly added, and false if the thread already had the label 
	 */
	public boolean addLabel(String threadId, String labelId) throws IOException, RequestFailedException {
		Set<String> labelIds = getLabelIds(threadId);
		if (labelIds.add(labelId)) {
			setLabelIds(threadId, labelIds);
			return true;
		}
		return false;
	}
	
	/**
	 * Convenience method to remove a label from a thread without otherwise changing the existing set.
	 * 
	 * First, does a GET for the thread to find the existing ids, then removes the label
	 * (if it is present).
	 * @return true if the label was removed, and false if the thread did not have the label 
	 */
	public boolean removeLabel(String threadId, String labelId) throws IOException, RequestFailedException {
		Set<String> labelIds = getLabelIds(threadId);
		if (labelIds.remove(labelId)) {
			setLabelIds(threadId, labelIds);
			return true;
		}
		return false;
	}
}
