package com.nylas;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
		  .addInterceptor(logging)
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
	
	public Account account(String accessToken) {
		return new Account(this, accessToken);
	}
	
	public void revoke(String accessToken) throws IOException {
		HttpUrl revokeUrl = getBaseUrl().resolve("oauth/revoke");
		
		FormBody emptyBody = new FormBody.Builder().build();
		
		Request request = new Request.Builder().url(revokeUrl)
				.post(emptyBody)
				.addHeader("Authorization", Credentials.basic(accessToken, ""))
				.build();
		
		try (Response response = getHttpClient().newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}
			String responseString = response.body().string();
			System.out.println(responseString);
		}
	}
	
	public static void main(String[] args) throws IOException {
		new NylasClient().revoke("tfJfCvGQQTfy7jU4zwtTYMGZqZFJNn");

	}

}
