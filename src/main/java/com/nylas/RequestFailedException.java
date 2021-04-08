package com.nylas;

import java.util.Map;

/**
 * This exception represents an http response without a 2xx status code
 */
public class RequestFailedException extends Exception {

	private final int statusCode;
	private final String errorMessage;
	private final String errorType;
	
	public RequestFailedException(int statusCode, String errorMessage, String errorType) {
		super(formatError(statusCode, errorType, errorMessage));
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
		this.errorType = errorType;
	}

	/**
	 * Use parseErrorResponse instead.
	 */
	@Deprecated
	public RequestFailedException(int statusCode, String responseBody) {
		super("statusCode=" + statusCode);
		this.statusCode = statusCode;
		
		String errorMessage = null;
		String errorType = null;
		if (responseBody != null && responseBody.length() > 0) {
			try {
				Map<String, Object> responseFields = JsonHelper.jsonToMap(responseBody);
				errorMessage = (String) responseFields.get("message");
				errorType = (String) responseFields.get("type");
			} catch (Throwable t) {
				// swallow
			}
		}
		this.errorMessage = errorMessage;
		this.errorType = errorType;
	}
	
	/**
	 * Use getStatusCode instead.
	 */
	@Deprecated
	public int getStatus() {
		return statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Raw response body has been removed.  See getErrorMessage and getErrorType instead.
	 */
	@Deprecated
	public String getResponseBody() {
		return "";
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public static RequestFailedException parseErrorResponse(int statusCode, String responseBody) {
		String errorMessage = null;
		String errorType = null;
		if (responseBody != null && responseBody.length() > 0) {
			try {
				Map<String, Object> responseFields = JsonHelper.jsonToMap(responseBody);
				errorMessage = (String) responseFields.get("message");
				errorType = (String) responseFields.get("type");
			} catch (Throwable t) {
				// swallow
			}
		}
		return new RequestFailedException(statusCode, errorMessage, errorType);
	}

	private static String formatError(int statusCode, String type, String message) {
		return "statusCode=" + statusCode + ", type=" + type + ", message=" + message;
	}
	
	@Override
	public String toString() {
		return "RequestFailedException [" + formatError(statusCode, errorType, errorMessage) + "]";
	}
	
}
