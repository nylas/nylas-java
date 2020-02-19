package com.nylas;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UserAgentInterceptor implements Interceptor {

	private final String userAgent = "Nylas Java SDK " + BuildVersion.getBuildVersion();
	
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request userAgentRequest = chain.request().newBuilder()
				.header("User-Agent", userAgent)
				.build();
		return chain.proceed(userAgentRequest);
	}

}
