package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.Types;

import okhttp3.HttpUrl;

public abstract class RestfulCollection<M extends RestfulModel, Q extends RestfulQuery<Q>> {

	protected final NylasClient client;
	protected final Class<M> modelClass;
	protected final String urlPath;
	protected final String authUser;
	
	protected RestfulCollection(NylasClient client, Class<M> modelClass, String urlPath, String authUser) {
		this.client = client;
		this.modelClass = modelClass;
		this.urlPath = urlPath;
		this.authUser = authUser;
	}
	
	public List<M> list() throws IOException, RequestFailedException {
		return list(null);
	}
	
	public List<M> list(Q query) throws IOException, RequestFailedException {
		HttpUrl url = getCollectionUrl(query, null);
		Type listType = Types.newParameterizedType(List.class, modelClass);
		return client.executeGet(authUser, url, listType);
	}
	
	protected <E extends M> List<E> expanded(Q query, Class<E> expandedModelClass)
			throws IOException, RequestFailedException {
		HttpUrl url = getCollectionUrl(query, "expanded");
		Type listType = Types.newParameterizedType(List.class, expandedModelClass);
		return client.executeGet(authUser, url, listType);
	}

	private static final Type STRING_LIST_TYPE = Types.newParameterizedType(List.class, String.class);
	protected List<String> ids(Q query) throws IOException, RequestFailedException {
		HttpUrl url = getCollectionUrl(query, "ids");
		return client.executeGet(authUser, url, STRING_LIST_TYPE);
	}
	
	protected long count(Q query) throws IOException, RequestFailedException {
		HttpUrl url = getCollectionUrl(query, "count");
		Count count = client.executeGet(authUser, url, Count.class);
		return count.getCount();
	}
	
	public M get(String id) throws IOException, RequestFailedException {
		HttpUrl messageUrl = getInstanceUrl(id);
		return client.executeGet(authUser, messageUrl, modelClass);
	}
	
	protected M put(String id, Map<String, Object> params) throws IOException, RequestFailedException {
		HttpUrl url = getInstanceUrl(id);
		return client.executePut(authUser, url, params, modelClass);
	}
	
	protected M create(Map<String, Object> params) throws IOException, RequestFailedException {
		HttpUrl url = getCollectionUrl(null, null);
		return client.executePost(authUser, url, params, modelClass);
	}
	
	protected void delete(String id) throws IOException, RequestFailedException {
		HttpUrl url = getInstanceUrl(id);
		client.executeDelete(authUser, url, null);
	}
	
	protected HttpUrl.Builder getBaseUrlBuilder() {
		return client.getBaseUrl().newBuilder();
	}
	
	protected HttpUrl getCollectionUrl(Q query, String view) {
		HttpUrl.Builder urlBuilder = getBaseUrlBuilder().addPathSegment(urlPath);
		if (query != null) {
			query.addParameters(urlBuilder);
		}
		if (view != null) {
			urlBuilder.addQueryParameter("view", view);
		}
		return urlBuilder.build();
	}
	
	protected HttpUrl getInstanceUrl(String id) {
		return getCollectionUrl(null, null).newBuilder()
				.addPathSegment(id)
				.build();
	}
	
	protected HttpUrl getInstancePathUrl(String id, String path) {
		return getInstanceUrl(id).newBuilder().addPathSegment(path).build();
	}

}
