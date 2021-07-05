package com.nylas;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddVersionHeadersInterceptor implements Interceptor {

	private static final String API_VERSION = "2.2";
	private static final String COMMIT_HASH = BuildInfo.COMMIT_ID;
	private static final String USER_AGENT
		= "Nylas Java SDK " + (BuildInfo.VERSION != null ? BuildInfo.VERSION : "unknown");
	
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request.Builder requestBuilder = chain.request().newBuilder()
				.header("Nylas-API-Version", API_VERSION)
				.header("User-Agent", USER_AGENT);
		if (COMMIT_HASH != null) {
			requestBuilder.header("X-Nylas-SDK-Commit-Hash", COMMIT_HASH);
		}
		return chain.proceed(requestBuilder.build());
	}
}
