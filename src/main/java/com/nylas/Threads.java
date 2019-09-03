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
	public List<Thread> list(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getThreadsUrl(query, null);
		return client.executeGetWithToken(accessToken, url, THREAD_LIST_TYPE);
	}
	
	private static final Type EXPANDED_THREAD_LIST_TYPE = Types.newParameterizedType(List.class, ExpandedThread.class);
	public List<ExpandedThread> expanded(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getThreadsUrl(query, "expanded");
		return client.executeGetWithToken(accessToken, url, EXPANDED_THREAD_LIST_TYPE);
	}
	
	private static final Type STRING_LIST_TYPE = Types.newParameterizedType(List.class, String.class);
	public List<String> ids(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getThreadsUrl(query, "ids");
		return client.executeGetWithToken(accessToken, url, STRING_LIST_TYPE);
	}
	
	public long count(ThreadQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getThreadsUrl(query, "count");
		Count count = client.executeGetWithToken(accessToken, url, Count.class);
		return count.getCount();
	}
	
	public Thread get(String threadId) throws IOException, RequestFailedException {
		HttpUrl threadUrl = getThreadUrl(threadId);
		return client.executeGetWithToken(accessToken, threadUrl, Thread.class);
	}
	
	/**
	 * Set the unread status for the given thread.
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setUnread(String threadId, boolean unread) throws IOException, RequestFailedException {
		return updateThread(threadId, Maps.of("unread", unread));
	}
	
	/**
	 * Set the starred state for the given thread.
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setStarred(String threadId, boolean starred) throws IOException, RequestFailedException {
		return updateThread(threadId, Maps.of("starred", starred));
	}
	
	/**
	 * Moves the given thread to the given folder
	 * 
	 * @return The updated Thread as returned by the server.
	 */
	public Thread setFolderId(String threadId, String folderId) throws IOException, RequestFailedException {
		return updateThread(threadId, Maps.of("folder_id", folderId));
	}
	
	private Thread updateThread(String threadId, Map<String, Object> params)
			throws IOException, RequestFailedException {
		HttpUrl threadUrl = getThreadUrl(threadId);
		return client.executePutWithToken(accessToken, threadUrl, params, Thread.class);
	}
	
	private HttpUrl getThreadsUrl(ThreadQuery query, String view) {
		HttpUrl.Builder urlBuilder = client.getBaseUrl().newBuilder("threads");
		if (query != null) {
			query.addParameters(urlBuilder);
		}
		if (view != null) {
			urlBuilder.addQueryParameter("view", "expanded");
		}
		return urlBuilder.build();
	}
	
	private HttpUrl getThreadUrl(String threadId) {
		return client.getBaseUrl().newBuilder()
				.addPathSegment("threads")
				.addPathSegment(threadId)
				.build();
	}
}
