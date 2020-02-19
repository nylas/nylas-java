package com.nylas;

import java.util.Map;

/**
 * This exception represents an http response without a 2xx status code
 */
public class RequestFailedException extends Exception {

	private final int status;
	private final String responseBody;
	private final String errorMessage;
	private final String errorType;
	
	public RequestFailedException(int status, String responseBody) {
		this.status = status;
		this.responseBody = responseBody;
		
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

	public int getStatus() {
		return status;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	@Override
	public String toString() {
		return "RequestFailedException [status=" + status + ", errorMessage=" + errorMessage + ", errorType="
				+ errorType + "]";
	}
	
}
