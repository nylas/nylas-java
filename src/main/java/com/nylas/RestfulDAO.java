package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;

public abstract class RestfulDAO<M extends RestfulModel> {

	protected final NylasClient client;
	protected final Class<M> modelClass;
	protected final String collectionPath;
	protected final String authUser;
	
	protected RestfulDAO(NylasClient client, Class<M> modelClass, String collectionPath, String authUser) {
		this.client = client;
		this.modelClass = modelClass;
		this.collectionPath = collectionPath;
		this.authUser = authUser;
	}
	
	protected Type getModelListType() {
		return JsonHelper.listTypeOf(modelClass);
	}
	
	protected RemoteCollection<M> list(RestfulQuery<?> query) throws IOException, RequestFailedException {
		return new RemoteCollection<M>(this, null, getModelListType(), query);
	}
	
	protected RemoteCollection<M> expanded(RestfulQuery<?> query) throws IOException, RequestFailedException {
		return new RemoteCollection<M>(this, "expanded", getModelListType(), query);
	}

	private static final Type STRING_LIST_TYPE = JsonHelper.listTypeOf(String.class);
	protected RemoteCollection<String> ids(RestfulQuery<?> query) throws IOException, RequestFailedException {
		return new RemoteCollection<String>(this, "ids", STRING_LIST_TYPE, query);
	}
	
	protected RemoteCollection<M> search(SearchQuery query) throws IOException, RequestFailedException {
		return new RemoteCollection<M>(this, null, getModelListType(), query);
	}
	
	protected long count(RestfulQuery<?> query) throws IOException, RequestFailedException {
		Count count = fetchQuery(query, "count", Count.class);
		return count.getCount();
	}
	
	<T> T fetchQuery(RestfulQuery<?> query, String view, Type resultType) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl();
		setQuery(url, query);
		if (view != null) {
			setView(url, view);
		}
		return client.executeGet(authUser, url, resultType);
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
	
	/**
	 * Delete the object with the given id.
	 * Returns a job_status_id if available, otherwise returns null.
	 */
	protected String delete(String id) throws IOException, RequestFailedException {
		return delete(id, null);
	}
	
	/**
	 * Delete the object with the given id using given extra query params.
	 * Returns a job_status_id if available, otherwise returns null.
	 */
	protected String delete(String id, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(id);
		addQueryParams(url, extraQueryParams);
		String result = client.executeDelete(authUser, url, String.class);
		if (result != null) {
			Map<String, Object> resultMap = JsonHelper.jsonToMap(result);
			if (resultMap != null) {
				return (String) resultMap.get("job_status_id");
			}
		}
		return null;
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
