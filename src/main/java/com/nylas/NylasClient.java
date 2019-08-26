package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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

	public HttpUrl getBaseUrl() {
		return baseUrl;
	}
	
	public OkHttpClient getHttpClient() {
		return httpClient;
	}
	
	public Application application(String clientId, String clientSecret) {
		return new Application(this, clientId, clientSecret);
	}
	
	public Threads threads(String accessToken) {
		return new Threads(this, accessToken);
	}
	
	public Folders folders(String accessToken) {
		return new Folders(this, accessToken);
	}
	
	public Account fetchAccountByAccessToken(String accessToken) throws IOException, RequestFailedException {
		HttpUrl accountUrl = getBaseUrl().resolve("account");
		Account.JsonBean accountJson = executeGetWithToken(accessToken, accountUrl, Account.JsonBean.class);
		return new Account(accountJson);
	}
	
	public void revoke(String accessToken) throws IOException, RequestFailedException {
		HttpUrl revokeUrl = getBaseUrl().resolve("oauth/revoke");
		executePostWithToken(accessToken, revokeUrl, Collections.emptyMap(), null);
	}
	
	<T> T executeGetWithToken(String accessToken, HttpUrl url, Type resultType)
			throws IOException, RequestFailedException {
		return executeRequestWithToken(accessToken, url, HttpMethod.GET, null, resultType);
	}
	
	<T> T executePutWithToken(String accessToken, HttpUrl url, Map<String, Object> params, Type resultType)
			throws IOException, RequestFailedException {
		RequestBody jsonBody = JsonHelper.jsonRequestBody(params);
		return executeRequestWithToken(accessToken, url, HttpMethod.PUT, jsonBody, resultType);
	}
	
	<T> T executePostWithToken(String accessToken, HttpUrl url, Map<String, Object> params, Type resultType)
			throws IOException, RequestFailedException {
		RequestBody jsonBody = JsonHelper.jsonRequestBody(params);
		return executeRequestWithToken(accessToken, url, HttpMethod.POST, jsonBody, resultType);
	}
	
	<T> T executeDeleteWithToken(String accessToken, HttpUrl url, Type resultType)
			throws IOException, RequestFailedException {
		return executeRequestWithToken(accessToken, url, HttpMethod.DELETE, null, resultType);
	}
	
	private <T> T executeRequestWithToken(String accessToken, HttpUrl url, HttpMethod method, RequestBody body,
			Type resultType) throws IOException, RequestFailedException {
		Request request = new Request.Builder().url(url)
				.addHeader("Authorization", Credentials.basic(accessToken, ""))
				.method(method.toString(), body)
				.build();
		return executeRequest(request, resultType);
	}
	
	<T> T executeRequest(Request request, Type resultType) throws IOException, RequestFailedException {
		try (Response response = getHttpClient().newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new RequestFailedException(response.code(), response.body().string());
			}
			if (resultType == null) {
				return null;
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
