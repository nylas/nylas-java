package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.Types;

import okhttp3.HttpUrl;

public class Threads {

	private final NylasClient client;
	private final String accessToken;
	
	Threads(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}
	
	private static final Type THREAD_LIST_TYPE = Types.newParameterizedType(List.class, Thread.class);
	public List<Thread> list() throws IOException, RequestFailedException {
		HttpUrl threadsUrl = client.getBaseUrl().resolve("threads");
		return client.executeGetWithToken(accessToken, threadsUrl, THREAD_LIST_TYPE);
	}
	
	public List<Thread> expanded(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl threadsUrl = client.getBaseUrl().newBuilder("threads")
				.addQueryParameter("view", "expanded")
				.build();
		return client.executeGetWithToken(accessToken, threadsUrl, THREAD_LIST_TYPE);
	}
	
	private static final Type STRING_LIST_TYPE = Types.newParameterizedType(List.class, String.class);
	public List<String> ids(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl threadsUrl = client.getBaseUrl().newBuilder("threads")
				.addQueryParameter("view", "ids")
				.build();
		return client.executeGetWithToken(accessToken, threadsUrl, STRING_LIST_TYPE);
	}
	
	public long count(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl threadsUrl = client.getBaseUrl().newBuilder("threads")
				.addQueryParameter("view", "count")
				.build();
		Count count = client.executeGetWithToken(accessToken, threadsUrl, Count.class);
		return count.getCount();
	}
	
	public Thread get(String threadId) throws IOException, RequestFailedException {
		HttpUrl threadUrl = getThreadUrl(threadId);
		return client.executeGetWithToken(accessToken, threadUrl, Thread.class);
	}
	
	/**
	 * Set the unread status for the given thread.
	 * 
	 * @return The updated EmailThread as returned by the server.
	 */
	public Thread setUnread(String threadId, boolean unread) throws IOException, RequestFailedException {
		return updateThread(threadId, Maps.of("unread", unread));
	}
	
	/**
	 * Set the starred state for the given thread.
	 * 
	 * @return The updated EmailThread as returned by the server.
	 */
	public Thread setStarred(String threadId, boolean starred) throws IOException, RequestFailedException {
		return updateThread(threadId, Maps.of("starred", starred));
	}
	
	/**
	 * Moves the given thread to the given folder
	 * 
	 * @return The updated EmailThread as returned by the server.
	 */
	public Thread setFolderId(String threadId, String folderId) throws IOException, RequestFailedException {
		return updateThread(threadId, Maps.of("folder_id", folderId));
	}
	
	private Thread updateThread(String threadId, Map<String, Object> params)
			throws IOException, RequestFailedException {
		HttpUrl threadUrl = getThreadUrl(threadId);
		return client.executePutWithToken(accessToken, threadUrl, params, Thread.class);
	}
	
	private HttpUrl getThreadUrl(String threadId) {
		return client.getBaseUrl().newBuilder()
				.addPathSegment("threads")
				.addPathSegment(threadId)
				.build();
	}
}
