package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.Types;

import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;

public abstract class RestfulCollection<M extends RestfulModel, Q extends RestfulQuery<Q>> {

	protected final NylasClient client;
	protected final Class<M> modelClass;
	protected final String collectionPath;
	protected final String authUser;
	
	protected RestfulCollection(NylasClient client, Class<M> modelClass, String collectionPath, String authUser) {
		this.client = client;
		this.modelClass = modelClass;
		this.collectionPath = collectionPath;
		this.authUser = authUser;
	}
	
	protected Type getModelListType() {
		return Types.newParameterizedType(List.class, modelClass);
	}
	
	protected List<M> list() throws IOException, RequestFailedException {
		return list(null);
	}
	
	protected List<M> list(Q query) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl();
		setQuery(url, query);
		Type listType = getModelListType();
		return client.executeGet(authUser, url, listType);
	}
	
	protected List<M> expanded(Q query) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl();
		setQuery(url, query);
		setView(url, "expanded");
		Type listType = getModelListType();
		return client.executeGet(authUser, url, listType);
	}

	private static final Type STRING_LIST_TYPE = Types.newParameterizedType(List.class, String.class);
	protected List<String> ids(Q query) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl();
		setQuery(url, query);
		setView(url, "ids");
		return client.executeGet(authUser, url, STRING_LIST_TYPE);
	}
	
	protected long count(Q query) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl();
		setQuery(url, query);
		setView(url, "count");
		Count count = client.executeGet(authUser, url, Count.class);
		return count.getCount();
	}
	
	protected List<M> search(SearchQuery query) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("search");
		setQuery(url, query);
		Type listType = getModelListType();
		return client.executeGet(authUser, url, listType);
	}
	
	protected M get(String id) throws IOException, RequestFailedException {
		HttpUrl.Builder messageUrl = getInstanceUrl(id);
		return client.executeGet(authUser, messageUrl, modelClass);
	}
	
	protected M create(M model) throws IOException, RequestFailedException {
		return create(model, null);
	}
	
	protected M create(M model, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
		if (model.hasId()) {
			throw new UnsupportedOperationException("Cannot create object with an existing id. Use update instead.");
		}
		Map<String, Object> params = model.getWritableFields(true);
		return create(params, extraQueryParams);
	}
	
	protected M create(Map<String, Object> params) throws IOException, RequestFailedException {
		return create(params, null);
	}

	protected M create(Map<String, Object> params, Map<String, String> extraQueryParams)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl();
		url = addQueryParams(url, extraQueryParams);
		return client.executePost(authUser, url, params, modelClass);
	}

	protected M update(M model) throws IOException, RequestFailedException {
		return update(model, null);
	}
	
	protected M update(M model, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
		if (!model.hasId()) {
			throw new UnsupportedOperationException("Cannot update an object without an id. Use create instead.");
		}
		Map<String, Object> params = model.getWritableFields(false);
		return update(model.getId(), params, extraQueryParams);
	}
	
	protected M update(String id, Map<String, Object> params) throws IOException, RequestFailedException {
		return update(id, params, null);
	}
	
	protected M update(String id, Map<String, Object> params, Map<String, String> extraQueryParams)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(id);
		addQueryParams(url, extraQueryParams);
		return client.executePut(authUser, url, params, modelClass);
	}
	
	protected void delete(String id) throws IOException, RequestFailedException {
		delete(id, null);
	}
	
	protected void delete(String id, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(id);
		addQueryParams(url, extraQueryParams);
		client.executeDelete(authUser, url, null);
	}
	
	protected HttpUrl.Builder getCollectionUrl() {
		return client.newUrlBuilder().addPathSegments(collectionPath);
	}
	
	protected HttpUrl.Builder getInstanceUrl(String id) {
		return getCollectionUrl().addPathSegment(id);
	}
	
	protected Builder addQueryParams(Builder url, Map<String, String> params) {
		if (params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				url.addQueryParameter(param.getKey(), param.getValue());
			}
		}
		return url;
	}

	private static void setQuery(HttpUrl.Builder url, RestfulQuery<?> query) {
		if (query != null) {
			query.addParameters(url);
		}
	}
	
	private static void setView(HttpUrl.Builder url, String view) {
		url.addQueryParameter("view", view);
	}

}
