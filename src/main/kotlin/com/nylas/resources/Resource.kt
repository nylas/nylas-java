package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.ListResponse
import com.nylas.models.RequestFailedException
import com.nylas.models.Response
import com.squareup.moshi.Types
import okhttp3.HttpUrl
import java.io.IOException
import java.lang.reflect.Type;

abstract class Resource<T> protected constructor(
    protected val client: NylasClient,
    modelClass: Class<T>
) {
    private val responseType: Type
    private val listResponseType: Type
    init {
        responseType = Types.newParameterizedType(Response::class.java, modelClass)
        listResponseType = Types.newParameterizedType(ListResponse::class.java, modelClass)
    }

    @Throws(IOException::class, RequestFailedException::class)
    protected fun listResource(path: String, queryParams: Map<String, String>?): ListResponse<T> {
        var url = client.newUrlBuilder().addPathSegments(path)
        url = addQueryParams(url, queryParams)
        return client.executeGet(url, listResponseType)
    }

    //	protected RemoteCollection<M> list(RestfulQuery<?> query) throws IOException, RequestFailedException {
    //		return new RemoteCollection<M>(this, null, getModelListType(), query);
    //	}
    //	protected RemoteCollection<M> expanded(RestfulQuery<?> query) throws IOException, RequestFailedException {
    //		return new RemoteCollection<M>(this, "expanded", getModelListType(), query);
    //	}
    //
    //	private static final Type STRING_LIST_TYPE = JsonHelper.listTypeOf(String.class);
    //	protected RemoteCollection<String> ids(RestfulQuery<?> query) throws IOException, RequestFailedException {
    //		return new RemoteCollection<String>(this, "ids", STRING_LIST_TYPE, query);
    //	}
    //
    //	protected RemoteCollection<M> search(SearchQuery query) throws IOException, RequestFailedException {
    //		return new RemoteCollection<M>(this, null, getModelListType(), query);
    //	}
    //
    //	protected long count(RestfulQuery<?> query) throws IOException, RequestFailedException {
    //		Count count = fetchQuery(query, "count", Count.class);
    //		return count.getCount();
    //	}
    //
    //	<T> T fetchQuery(RestfulQuery<?> query, String view, Type resultType) throws IOException, RequestFailedException {
    //		HttpUrl.Builder url = getCollectionUrl();
    //		setQuery(url, query);
    //		if (view != null) {
    //			setView(url, view);
    //		}
    //		return client.executeGet(authUser, url, resultType, authMethod);
    //	}
    //
    //	protected M get(String id) throws IOException, RequestFailedException {
    //		HttpUrl.Builder messageUrl = getInstanceUrl(id);
    //		return client.executeGet(authUser, messageUrl, modelClass, authMethod);
    //	}
    //
    //	protected M create(M model) throws IOException, RequestFailedException {
    //		return create(model, null);
    //	}
    //
    //	protected M create(M model, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
    //		if (model.hasId()) {
    //			throw new UnsupportedOperationException("Cannot create object with an existing id. Use update instead.");
    //		}
    //		Map<String, Object> params = model.getWritableFields(true);
    //		return create(params, extraQueryParams);
    //	}
    //
    //	protected M create(Map<String, Object> params) throws IOException, RequestFailedException {
    //		return create(params, null);
    //	}
    //
    //	protected M create(Map<String, Object> params, Map<String, String> extraQueryParams)
    //			throws IOException, RequestFailedException {
    //		HttpUrl.Builder url = getCollectionUrl();
    //		url = addQueryParams(url, extraQueryParams);
    //		return client.executePost(authUser, url, params, modelClass, authMethod);
    //	}
    //
    //	protected M update(M model) throws IOException, RequestFailedException {
    //		return update(model, null);
    //	}
    //
    //	protected M update(M model, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
    //		if (!model.hasId()) {
    //			throw new UnsupportedOperationException("Cannot update an object without an id. Use create instead.");
    //		}
    //		Map<String, Object> params = model.getWritableFields(false);
    //		return update(model.getId(), params, extraQueryParams);
    //	}
    //
    //	protected M update(String id, Map<String, Object> params) throws IOException, RequestFailedException {
    //		return update(id, params, null);
    //	}
    //
    //	protected M update(String id, Map<String, Object> params, Map<String, String> extraQueryParams)
    //			throws IOException, RequestFailedException {
    //		return update(id, params, extraQueryParams, HttpMethod.PUT);
    //	}
    //
    //	protected M updatePatch(String id, Map<String, Object> params, Map<String, String> extraQueryParams)
    //			throws IOException, RequestFailedException {
    //		return update(id, params, extraQueryParams, HttpMethod.PATCH);
    //	}
    //
    //	protected M update(String id, Map<String, Object> params, Map<String, String> extraQueryParams, HttpMethod method)
    //			throws IOException, RequestFailedException {
    //		HttpUrl.Builder url = getInstanceUrl(id);
    //		addQueryParams(url, extraQueryParams);
    //		switch (method) {
    //			case PATCH:
    //				return client.executePatch(authUser, url, params, modelClass, authMethod);
    //			case PUT:
    //			default:
    //				return client.executePut(authUser, url, params, modelClass, authMethod);
    //		}
    //	}
    //
    //	/**
    //	 * Delete the object with the given id.
    //	 * Returns a job_status_id if available, otherwise returns null.
    //	 */
    //	protected String delete(String id) throws IOException, RequestFailedException {
    //		return delete(id, null);
    //	}
    //
    //	/**
    //	 * Delete the object with the given id using given extra query params.
    //	 * Returns a job_status_id if available, otherwise returns null.
    //	 */
    //	protected String delete(String id, Map<String, String> extraQueryParams) throws IOException, RequestFailedException {
    //		HttpUrl.Builder url = getInstanceUrl(id);
    //		addQueryParams(url, extraQueryParams);
    //		String result = client.executeDelete(authUser, url, String.class, authMethod);
    //		if (result != null) {
    //			Map<String, Object> resultMap = JsonHelper.jsonToMap(result);
    //			if (resultMap != null) {
    //				return (String) resultMap.get("job_status_id");
    //			}
    //		}
    //		return null;
    //	}
    //
    //	protected HttpUrl.Builder getCollectionUrl() {
    //		return client.newUrlBuilder().addPathSegments(collectionPath);
    //	}
    //
    //	protected HttpUrl.Builder getInstanceUrl(String id) {
    //		return getCollectionUrl().addPathSegment(id);
    //	}
    //
    private fun addQueryParams(url: HttpUrl.Builder, params: Map<String, String>?): HttpUrl.Builder {
        if (params != null) {
            for ((key, value) in params) {
                url.addQueryParameter(key, value)
            }
        }
        return url
    } //
    //	private static void setQuery(HttpUrl.Builder url, RestfulQuery<?> query) {
    //		if (query != null) {
    //			query.addParameters(url);
    //		}
    //	}
    //
    //	protected static void setView(HttpUrl.Builder url, String view) {
    //		url.addQueryParameter("view", view);
    //	}
}
