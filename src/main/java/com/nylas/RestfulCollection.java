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
		HttpUrl.Builder url = getCollectionUrlBuilder(query, null);
		Type listType = Types.newParameterizedType(List.class, modelClass);
		return client.executeGet(authUser, url, listType);
	}
	
	protected <E extends M> List<E> expanded(Q query, Class<E> expandedModelClass)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrlBuilder(query, "expanded");
		Type listType = Types.newParameterizedType(List.class, expandedModelClass);
		return client.executeGet(authUser, url, listType);
	}

	private static final Type STRING_LIST_TYPE = Types.newParameterizedType(List.class, String.class);
	protected List<String> ids(Q query) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrlBuilder(query, "ids");
		return client.executeGet(authUser, url, STRING_LIST_TYPE);
	}
	
	protected long count(Q query) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrlBuilder(query, "count");
		Count count = client.executeGet(authUser, url, Count.class);
		return count.getCount();
	}
	
	protected List<M> search(SearchQuery query) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrlBuilder(query, null)
				.addPathSegment("search");
		Type listType = Types.newParameterizedType(List.class, modelClass);
		return client.executeGet(authUser, url, listType);
	}
	
	public M get(String id) throws IOException, RequestFailedException {
		HttpUrl.Builder messageUrl = getInstanceUrlBuilder(id);
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
		HttpUrl.Builder url = getCollectionUrlBuilder();
		url = addQueryParams(url, extraQueryParams);
		return client.executePost(authUser, url, params, modelClass);
	}

	protected M update(M model) throws IOException, RequestFailedException {
		return update(model, null);
	}
	
	protected M update(M model, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
		if (model.hasId()) {
			throw new UnsupportedOperationException("Cannot create object with an existing id. Use update instead.");
		}
		Map<String, Object> params = model.getWritableFields(false);
		return update(model.getId(), params, extraQueryParams);
	}
	
	protected M update(String id, Map<String, Object> params) throws IOException, RequestFailedException {
		return update(id, params, null);
	}
	
	protected M update(String id, Map<String, Object> params, Map<String, String> extraQueryParams)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrlBuilder(id);
		addQueryParams(url, extraQueryParams);
		return client.executePut(authUser, url, params, modelClass);
	}
	
	protected void delete(String id) throws IOException, RequestFailedException {
		delete(id, null);
	}
	
	protected void delete(String id, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrlBuilder(id);
		addQueryParams(url, extraQueryParams);
		client.executeDelete(authUser, url, null);
	}
	
	protected HttpUrl.Builder getBaseUrlBuilder() {
		return client.newUrlBuilder();
	}
	
	protected HttpUrl.Builder getCollectionUrlBuilder() {
		return getCollectionUrlBuilder(null, null);
	}
	
	protected HttpUrl.Builder getCollectionUrlBuilder(RestfulQuery<?> query, String view) {
		HttpUrl.Builder urlBuilder = getBaseUrlBuilder().addPathSegment(urlPath);
		if (query != null) {
			query.addParameters(urlBuilder);
		}
		if (view != null) {
			urlBuilder.addQueryParameter("view", view);
		}
		return urlBuilder;
	}
	
	protected HttpUrl.Builder getInstanceUrlBuilder(String id) {
		return getCollectionUrlBuilder().addPathSegment(id);
	}
	
	protected Builder addQueryParams(Builder url, Map<String, String> params) {
		if (params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				url.addQueryParameter(param.getKey(), param.getValue());
			}
		}
		return url;
	}

}
