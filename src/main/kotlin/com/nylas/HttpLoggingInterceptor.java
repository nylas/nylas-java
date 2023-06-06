package com.nylas;

import java.io.EOFException;
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
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * OkHttp Interceptor which provides 3 loggers for HTTP requests/responses:
 * <dl>
 *   <dt>com.nylas.http.Summary</dt><dd>logs one line for each request, containing method, URI, and content size
and one line for each response containing status code, message, content size and duration.</dd>
 *   <dt>com.nylas.http.Headers</dt><dd>logs request and response headers (except Authorization value by default).</dd>
 *   <dt>com.nylas.http.Body</dt><dd>logs request and response bodies (only the first 10kB by default).</dd>
 * </dl>
 */
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
		
		if (bodyLogs.isDebugEnabled()) {
			String message;
			if (!hasBody) {
				message = " No request body";
			} else {
				MediaType contentType = requestBody.contentType();
				if (!isPrintableMediaType(contentType)) {
					message = " Skipped logging request body of type that may not be printable: " + contentType;
				} else {
					Buffer buf = new Buffer();
					requestBody.writeTo(buf);
					message = bodyBufferToString("", buf, contentType);
				}
			}
			bodyLogs.debug("=>" + message);
		}
	}
	
	private void logResponse(Response response, long durationMillis) throws IOException {

		long contentLength = getContentLength(response);
		// Summary
		if (requestLogs.isDebugEnabled()) {
			requestLogs.debug("<= " + response.code()
					+ " " + response.message()
					+ " resBodySize=" + contentLength
					+ " durationMs=" + durationMillis
					);
		}
		
		logHeaders("<=", response.headers());
		
		if (bodyLogs.isDebugEnabled()) {
			String message;
			if (contentLength == -1) {
				message = " No response body";
			} else {
				MediaType contentType = response.body().contentType();
				if (!isPrintableMediaType(contentType)) {
					message = " Skipped logging response body of type that may not be printable: " + contentType;
				} else {
					BufferedSource source = response.body().source();
					source.request(Long.MAX_VALUE); // if zipped, may need to buffer all of it
					Buffer buf = source.getBuffer().clone();
					String gzippedMessage = "";
					if ("gzip".equalsIgnoreCase(response.headers().get("Content-Encoding"))) {
						long gzippedSize = buf.size();
						try (GzipSource gzippedResponseBody = new GzipSource(buf)) {
							buf = new Buffer();
							buf.writeAll(gzippedResponseBody);
						}
						gzippedMessage = " (decompressed " + gzippedSize + " bytes to " + buf.size() + " bytes)";
					}
					message = bodyBufferToString(gzippedMessage, buf, contentType);
				}
			}
			bodyLogs.debug("<=" + message);
		}
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
	
	private boolean isPrintableMediaType(MediaType type) {
		return type != null
				&& ("text".equals(type.type()) || type.toString().startsWith("application/json"));
	}

	private String bodyBufferToString(String prefix, Buffer buf, MediaType contentType) throws EOFException {
		long bytesToLog = buf.size();
		String truncationMessage = "";
		if (bytesToLog > maxBodyBytesToLog) {
			bytesToLog = maxBodyBytesToLog;
			truncationMessage = "\n(truncated " + buf.size() + " byte body after " + bytesToLog + " bytes)";
		}
		Charset charset = contentType.charset(StandardCharsets.UTF_8);
		return prefix + "\n" + buf.readString(bytesToLog, charset) + truncationMessage;
	}

	private long getContentLength(Response response) {
		if(response.body() == null) {
			return -1;
		}
		return response.body().contentLength();
	}
	
}
 	 	