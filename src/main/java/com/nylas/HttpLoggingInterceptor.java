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
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

public class HttpLoggingInterceptor implements Interceptor {

	private static final Logger requestLogs = LoggerFactory.getLogger("com.nylas.http.Summary");
	private static final Logger headersLogs = LoggerFactory.getLogger("com.nylas.http.Headers");
	private static final Logger bodyLogs = LoggerFactory.getLogger("com.nylas.http.Body");
	
	private boolean logAuthHeader = false;
	private int maxBodyBytesToLog = 10_000;
	
	public boolean isLogAuthHeader() {
		return logAuthHeader;
	}

	public void setLogAuthHeader(boolean logAuthHeader) {
		this.logAuthHeader = logAuthHeader;
	}

	public int getMaxBodyBytesToLog() {
		return maxBodyBytesToLog;
	}

	public void setMaxBodyBytesToLog(int maxBodyBytesToLog) {
		this.maxBodyBytesToLog = maxBodyBytesToLog;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		logRequest(request);
		
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
		long durationMillis = (System.nanoTime() - startNanos) / 1_000_000;
		
		logResponse(response, durationMillis);
		return response;
	}

	private void logRequest(Request request) throws IOException {
		RequestBody requestBody = request.body();
		boolean hasBody = requestBody != null;

		// Summary
		if (requestLogs.isDebugEnabled()) {
			requestLogs.debug("=> " + request.method()
					+ " " + request.url()
					+ " reqBodySize=" + (hasBody ? requestBody.contentLength() : 0)
					);
		}
		
		logHeaders("=>", request.headers());
		
		logBody("=>", hasBody, hasBody ? requestBody.contentType() : null, () -> {
			Buffer buf = new Buffer();
			requestBody.writeTo(buf);
			return buf;
		});
	}
	
	private void logResponse(Response response, long durationMillis) throws IOException {
		boolean hasBody = HttpHeaders.hasBody(response);
		ResponseBody responseBody = response.body();
		MediaType contentType = responseBody == null ? null : responseBody.contentType();

		// Summary
		if (requestLogs.isDebugEnabled()) {
			requestLogs.debug("<= " + response.code()
					+ " " + response.message()
					+ " resBodySize=" + HttpHeaders.contentLength(response)
					+ " durationMs=" + durationMillis
					);
		}
		
		logHeaders("<=", response.headers());
		logBody("<=", hasBody, contentType, () -> {
			BufferedSource source = responseBody.source();
			source.request(Long.MAX_VALUE);
			Buffer buf = source.getBuffer().clone();
			if ("gzip".equalsIgnoreCase(response.headers().get("Content-Encoding"))) {
				try (GzipSource gzippedResponseBody = new GzipSource(buf)) {
					buf = new Buffer();
					buf.writeAll(gzippedResponseBody);
				}
			}
			return buf;
		});
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
			headersLog.deleteCharAt(headersLog.length()-1);
			headersLogs.debug(headersLog.toString());
		}
	}
	
	static interface BufferProvider {
		Buffer getBuffer() throws IOException;
	}
	private void logBody(String direction, boolean hasBody, MediaType contentType, BufferProvider body)
			throws IOException {
		if (bodyLogs.isDebugEnabled()) {
			String message;
			if (!hasBody) {
				message = " No body";
			} else if (!isPrintableMediaType(contentType)) {
				message = " Skipped logging body of type that may not be printable: " + contentType;
			} else {
				Buffer buf = body.getBuffer();
				long bytesToLog = buf.size();
				String truncationMessage = "";
				if (bytesToLog > maxBodyBytesToLog) {
					bytesToLog = maxBodyBytesToLog;
					truncationMessage = "\n(truncated " + buf.size() + " byte body after " + bytesToLog + " bytes)";
				}
				Charset charset = contentType.charset(StandardCharsets.UTF_8);
				String bodyString = buf.readString(bytesToLog, charset);
				message = "\n" + bodyString + truncationMessage;
			}
			bodyLogs.debug(direction + message);
		}
	}
	
	private boolean isPrintableMediaType(MediaType type) {
		return type != null
				&& ("text".equals(type.type()) || type.toString().startsWith("application/json"));
	}

}
 	 	