package com.nylas;

import java.io.IOException;
import java.util.List;

public class Threads extends RestfulCollection<Thread, ThreadQuery> {

	Threads(NylasClient client, String accessToken) {
		super(client, Thread.class, "threads", accessToken);
	}
	
	public List<ExpandedThread> expanded(ThreadQuery query) throws IOException, RequestFailedException {
		return super.expanded(query, ExpandedThread.class);
	}
	
	public List<String> ids(ThreadQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}
	
	public long count(ThreadQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	/**
	 * Set the unread status for the given thread.
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setUnread(String threadId, boolean unread) throws IOException, RequestFailedException {
		return super.put(threadId, Maps.of("unread", unread));
	}
	
	/**
	 * Set the starred state for the given thread.
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setStarred(String threadId, boolean starred) throws IOException, RequestFailedException {
		return super.put(threadId, Maps.of("starred", starred));
	}
	
	/**
	 * Moves the given thread to the given folder
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setFolderId(String threadId, String folderId) throws IOException, RequestFailedException {
		return super.put(threadId, Maps.of("folder_id", folderId));
	}
	
	/**
	 * Updates the labels on the given thread, overwriting all previous labels on the thread.
	 */
	public Thread setLabelIds(String threadId, Iterable<String> labelIds) throws IOException, RequestFailedException {
		return super.put(threadId, Maps.of("label_ids", String.join(",", labelIds)));
	}
}
