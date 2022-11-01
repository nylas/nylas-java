package com.nylas;

import java.util.Map;

/**
 * This exception represents a 429 error response, with details on the rate limit
 */
public class RateLimitException extends RequestFailedException {

	public static final int RATE_LIMIT_STATUS_CODE = 429;
	public static final String RATE_LIMIT_LIMIT_HEADER = "X-RateLimit-Limit";
	public static final String RATE_LIMIT_RESET_HEADER = "X-RateLimit-Reset";

	/**
	 * The rate limit
	 */
	private final int rateLimit;
	/**
	 * The rate limit expiration time, in seconds
	 */
	private final int rateLimitReset;

	public RateLimitException(String errorMessage, String errorType, int rateLimit, int rateLimitReset) {
		super(RATE_LIMIT_STATUS_CODE, errorMessage, errorType);
		this.rateLimit = rateLimit;
		this.rateLimitReset = rateLimitReset;
	}

	public int getRateLimit() {
		return rateLimit;
	}

	public int getRateLimitReset() {
		return rateLimitReset;
	}

	/**
	 * Creates an error response
	 * @param rateLimitHeader The header value of the rate limit
	 * @param rateLimitResetHeader The header value of the rate limit reset
	 * @param responseBody The error payload to parse details from
	 * @return The error with all the rate limit details
	 */
	public static RateLimitException parseErrorResponse(String rateLimitHeader, String rateLimitResetHeader, String responseBody) {
		String errorMessage = null;
		String errorType = null;
		int rateLimit = 0;
		int rateLimitReset = 0;
		if (responseBody != null && responseBody.length() > 0) {
			try {
				Map<String, Object> responseFields = JsonHelper.jsonToMap(responseBody);
				errorMessage = (String) responseFields.get("message");
				errorType = (String) responseFields.get("type");
			} catch (Throwable t) {
				// swallow
			}
		}
		try {
			rateLimit = Integer.parseInt(rateLimitHeader);
		} catch (NumberFormatException e) {
			// swallow
		}
		try {
			rateLimitReset = Integer.parseInt(rateLimitResetHeader);
		} catch (NumberFormatException e) {
			// swallow
		}
		return new RateLimitException(errorMessage, errorType, rateLimit, rateLimitReset);
	}

	@Override
	public String toString() {
		return "com.nylas.RateLimitException [" + formatError(getStatusCode(), getErrorType(), getErrorMessage()) + "]";
	}
}
