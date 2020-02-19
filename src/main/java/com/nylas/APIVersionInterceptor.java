package com.nylas;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class APIVersionInterceptor implements Interceptor {

	private static final String API_VERSION = "2.1";
	
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request userAgentRequest = chain.request().newBuilder()
				.header("Nylas-API-Version", API_VERSION)
				.build();
		return chain.proceed(userAgentRequest);
	}
}
