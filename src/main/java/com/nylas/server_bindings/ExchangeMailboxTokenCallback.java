package com.nylas.server_bindings;

import com.nylas.AccessToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for classes to implement callbacks for exchanging a code for a token.
 * See: {@link NylasFilter}
 */
public interface ExchangeMailboxTokenCallback {

	/**
	 * Callback invoked after a successful token exchange
	 * @param request Incoming request data
	 * @param response Response object that can be written to
	 * @param accessToken The object containing the access token
	 */
	void onTokenExchange(HttpServletRequest request, HttpServletResponse response, AccessToken accessToken);

	/**
	 * Callback invoked after an error encountered during the token exchange
	 * @param request Incoming request data
	 * @param response Response object that can be written to
	 * @param error The error object
	 */
	void onTokenExchangeError(HttpServletRequest request, HttpServletResponse response, Exception error);
}
