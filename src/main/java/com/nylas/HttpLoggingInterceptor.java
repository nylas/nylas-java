package com.nylas;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class HttpLoggingInterceptor implements Interceptor {

	private static final Logger requestLogs = LoggerFactory.getLogger("com.nylas.http.Summary");
	private static final Logger headersLogs = LoggerFactory.getLogger("com.nylas.http.Headers");
	private static final Logger bodyLogs = LoggerFactory.getLogger("com.nylas.http.Body");
	
	private boolean logAuthHeader = false;
	private int maxBodySize = 20_000;
	
	public boolean isLogAuthHeader() {
		return logAuthHeader;
	}

	public void setLogAuthHeader(boolean logAuthHeader) {
		this.logAuthHeader = logAuthHeader;
	}

	public int getMaxBodySize() {
		return maxBodySize;
	}

	public void setMaxBodySize(int maxBodySize) {
		this.maxBodySize = maxBodySize;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		RequestBody requestBody = request.body();
		long reqBodySize = 0;
		if (requestBody != null) {
			reqBodySize = requestBody.contentLength();
		}
		
		// Request summary
		if (requestLogs.isDebugEnabled()) {
			requestLogs.debug("=> " + request.method()
					+ " " + request.url()
					+ " reqBodySize=" + reqBodySize
					);
		}
		
		logHeaders("=>", request.headers());
		logBody("=>", () -> {
			Buffer buf = new Buffer();
			requestBody.writeTo(buf);
			return buf;
		}, reqBodySize, requestBody == null ? null : requestBody.contentType());

		// execute request
		long startNanos = System.nanoTime();
		Response response = null;
		try {
			response = chain.proceed(request);
		} catch (Throwable t) {
			if (requestLogs.isDebugEnabled()) {
				requestLogs.debug("<= Exception=" + t);
			}
			throw t;
		}
		ResponseBody responseBody = response.body();
		long resBodySize = responseBody.contentLength();
		if (resBodySize == -1) {
			resBodySize = 0;
		}

		// Response summary
		if (requestLogs.isDebugEnabled()) {
			long durationMillis = (System.nanoTime() - startNanos) / 1_000_000;
			requestLogs.debug("<= " + response.code()
					+ " " + response.message()
					+ " resBodySize=" + resBodySize
					+ " durationMs=" + durationMillis
					);
		}
		
		// Response headers
		logHeaders("<=", response.headers());
		logBody("<=", () -> {
			BufferedSource source = responseBody.source();
			source.request(Long.MAX_VALUE);
			return source.getBuffer().clone();
		}, resBodySize, responseBody == null ? null : responseBody.contentType());

		return response;
	}
	
	private void logHeaders(String direction, Headers headers) {
		if (headersLogs.isDebugEnabled()) {
			StringBuilder headersLog = new StringBuilder().append(direction).append("\n");
			for (int i = 0; i < headers.size(); i++) {
				String name = headers.name(i);
				String value = headers.value(i);
				if (!logAuthHeader && "Authorization".equals(name)) {
					value = "<not logged>";
				}
				headersLog.append("  ").append(name).append(": ").append(value).append("\n");
			}
			headersLogs.debug(headersLog.toString());
		}
	}
	
	static interface BufferProvider {
		Buffer getBuffer() throws IOException;
	}
	private void logBody(String direction, BufferProvider body, long bodySize, MediaType contentType)
			throws IOException {
		if (bodyLogs.isDebugEnabled()) {
			if (bodySize <= 0) {
				bodyLogs.debug(direction + " No body to log");
			} else if (bodySize > maxBodySize) {
				bodyLogs.debug(direction + " Skipped logging body too large (" + bodySize + " > " + maxBodySize + ")");
			} else if (!isPrintableMediaType(contentType)) {
				bodyLogs.debug(direction + " Skipped logging body of type that may not be printable: " + contentType);
			} else {
				Buffer buf = body.getBuffer();
				Charset charset = StandardCharsets.UTF_8;
				if (contentType != null) {
					charset = contentType.charset(charset);
				}
				String bodyString = buf.readString(charset);
				bodyLogs.debug(direction + "\n" + bodyString);
			}
		}
	}
	
	private boolean isPrintableMediaType(MediaType type) {
		return type != null
				&& ("text".equals(type.type()) || type.toString().startsWith("application/json"));
	}

}
 	 	