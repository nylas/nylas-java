package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.squareup.moshi.JsonAdapter;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;

/**
 * The NylasCLient is the entry point to the Java SDK's API.
 * An instance holds a configured http client pointing to a base URL and is intended to be reused and shared
 * across threads and time.
 */
public class NylasClient {

	public static final String DEFAULT_BASE_URL = "https://api.nylas.com";
	
	private final HttpUrl baseUrl;
	private final OkHttpClient httpClient;
	
	/**
	 * Construct a default NylasClient.
	 */
	public NylasClient() {
		this(DEFAULT_BASE_URL);
	}

	/**
	 * Construct a NylasClient with a custom configured HTTP Client.
	 * Interceptors will be added to include required HTTP headers and for configurable logging.
	 */
	public NylasClient(OkHttpClient.Builder httpClientBuilder) {
		this (DEFAULT_BASE_URL, httpClientBuilder);
	}
	
	/**
	 * Construct a NylasClient pointed to a custom base URL.
	 */
	public NylasClient(String baseUrl) {
		this(baseUrl, new OkHttpClient.Builder()
				.connectTimeout(60, TimeUnit.SECONDS)
				.readTimeout(60, TimeUnit.SECONDS)
				.writeTimeout(60,  TimeUnit.SECONDS));
	}

	/**
	 * Construct a NylasClient with a custom configured HTTP Client pointed to custom base URL.
	 * Interceptors will be added to include required HTTP headers and for configurable logging.
	 */
	public NylasClient(String baseUrl, OkHttpClient.Builder httpClientBuilder) {
		this.baseUrl = HttpUrl.get(baseUrl);
		httpClient = httpClientBuilder
			.addInterceptor(new AddVersionHeadersInterceptor())
			.addNetworkInterceptor(new HttpLoggingInterceptor())
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
	
	/**
	 * Download the given url. If the request is successful, returns the raw response body, exposing useful headers
	 * such as Content-Type and Content-Length.
	 * <p>
	 * The returned ResponseBody must be closed:<br>
	 * <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/#the-response-body-must-be-closed">
	 * https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/#the-response-body-must-be-closed</a>
	 */
	ResponseBody download(String authUser, HttpUrl.Builder url) throws IOException, RequestFailedException {
		Request request = buildRequest(authUser, url, HttpMethod.GET, null);
		Response response = getHttpClient().newCall(request).execute();
		throwAndCloseOnFailedRequest(response);
		return response.body();
	}

	<T> T executeRequestWithAuth(String authUser, HttpUrl.Builder url, HttpMethod method, RequestBody body,
			Type resultType) throws IOException, RequestFailedException {
		Request request = buildRequest(authUser, url, method, body);
		return executeRequest(request, resultType);
	}

	Request buildRequest(String authUser, HttpUrl.Builder url, HttpMethod method, RequestBody body) {
		Request.Builder builder = new Request.Builder().url(url.build());
		if (authUser != null) {
			addAuthHeader(builder, authUser);
		}
		return builder.method(method.toString(), body).build();
	}
	
	void addAuthHeader(Request.Builder request, String authUser) {
		request.addHeader("Authorization", Credentials.basic(authUser, ""));
	}
	
	@SuppressWarnings("unchecked")
	<T> T executeRequest(Request request, Type resultType) throws IOException, RequestFailedException {
		try (Response response = getHttpClient().newCall(request).execute()) {
			throwAndCloseOnFailedRequest(response);
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
	
	private void throwAndCloseOnFailedRequest(Response response) throws IOException, RequestFailedException {
		if (!response.isSuccessful()) {
			String responseBody = response.body().string();
			response.close();
			throw new RequestFailedException(response.code(), responseBody);
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
