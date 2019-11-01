package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;

public class NylasClient {

	private static final String DEFAULT_BASE_URL = "https://api.nylas.com";
	
	private final HttpUrl baseUrl;
	private final OkHttpClient httpClient;
	
	public NylasClient() {
		this(DEFAULT_BASE_URL);
	}

	public NylasClient(String baseUrl) {
		this.baseUrl = HttpUrl.get(baseUrl);
		
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		httpClient = new OkHttpClient.Builder()
				.addInterceptor(new UserAgentInterceptor())
				.addNetworkInterceptor(logging)
				.build();
	}

	public HttpUrl.Builder newUrlBuilder() {
		return baseUrl.newBuilder();
	}
	
	public OkHttpClient getHttpClient() {
		return httpClient;
	}
	
	public NylasApplication application(String clientId, String clientSecret) {
		return new NylasApplication(this, clientId, clientSecret);
	}
	
	public NylasAccount account(String accessToken) {
		return new NylasAccount(this, accessToken);
	}
	
	<T> T executeGet(String authUser, HttpUrl.Builder url, Type resultType)
			throws IOException, RequestFailedException {
		return executeRequestWithAuth(authUser, url, HttpMethod.GET, null, resultType);
	}
	
	<T> T executePut(String authUser, HttpUrl.Builder url, Map<String, Object> params, Type resultType)
			throws IOException, RequestFailedException {
		RequestBody jsonBody = JsonHelper.jsonRequestBody(params);
		return executeRequestWithAuth(authUser, url, HttpMethod.PUT, jsonBody, resultType);
	}
	
	<T> T executePost(String authUser, HttpUrl.Builder url, Map<String, Object> params, Type resultType)
			throws IOException, RequestFailedException {
		RequestBody jsonBody = Util.EMPTY_REQUEST;
		if (params != null) {
			jsonBody = JsonHelper.jsonRequestBody(params);
		}
		return executeRequestWithAuth(authUser, url, HttpMethod.POST, jsonBody, resultType);
	}
	
	<T> T executeDelete(String authUser, HttpUrl.Builder url, Type resultType)
			throws IOException, RequestFailedException {
		return executeRequestWithAuth(authUser, url, HttpMethod.DELETE, null, resultType);
	}
	
	<T> T executeRequestWithAuth(String authUser, HttpUrl.Builder url, HttpMethod method, RequestBody body,
			Type resultType) throws IOException, RequestFailedException {
		Request request = buildRequest(authUser, url, method, body);
		return executeRequest(request, resultType);
	}

	Request buildRequest(String authUser, HttpUrl.Builder url, HttpMethod method, RequestBody body) {
		Request.Builder builder = new Request.Builder().url(url.build());
		addAuthHeader(builder, authUser);
		return builder.method(method.toString(), body).build();
	}
	
	void addAuthHeader(Request.Builder request, String authUser) {
		request.addHeader("Authorization", Credentials.basic(authUser, ""));
	}
	
	@SuppressWarnings("unchecked")
	<T> T executeRequest(Request request, Type resultType) throws IOException, RequestFailedException {
		try (Response response = getHttpClient().newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new RequestFailedException(response.code(), response.body().string());
			}
			if (resultType == null) {
				return null;
			} else if (resultType == String.class) {
				return (T) response.body().string();
			} else {
				JsonAdapter<T> adapter = JsonHelper.moshi().adapter(resultType);
				return adapter.fromJson(response.body().source());
			}
		}
	}
	
	static enum HttpMethod {
		GET,
		PUT,
		POST,
		DELETE,
		;
	}

}
