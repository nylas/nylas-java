package com.nylas;

import java.io.IOException;
import java.util.List;

public class Threads extends RestfulCollection<Thread, ThreadQuery> {

	Threads(NylasClient client, String accessToken) {
		super(client, Thread.class, "threads", accessToken);
	}
	
	@Override
	public List<Thread> list() throws IOException, RequestFailedException {
		return super.list();
	}

	@Override
	public List<Thread> list(ThreadQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Thread get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	public List<ExpandedThread> expanded(ThreadQuery query) throws IOException, RequestFailedException {
		return super.expanded(query, ExpandedThread.class);
	}
	
	@Override
	public List<String> ids(ThreadQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}
	
	@Override
	public long count(ThreadQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	public List<Thread> search(String query) throws IOException, RequestFailedException {
		return super.search(new SearchQuery(query));
	}
	
	public List<Thread> search(String query, int limit, int offset) throws IOException, RequestFailedException {
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
}
