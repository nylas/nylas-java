package com.nylas.server_bindings;

import com.nylas.*;
import com.nylas.services.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A filter for backed frameworks that enables seamless integration with useful
 * Nylas functionality such as authentication. Currently supported routes are:
 *
 * <br> 1. '/nylas/generate-auth-url': Building the URL for authenticating users to
 * your application via Hosted Authentication
 * <br> 2. '/nylas/exchange-mailbox-token': Exchange an authorization code for an access token
 */
public class NylasFilter implements Filter {
	private final Routes routes;
	private final Scope[] defaultScopes;
	private final ExchangeMailboxTokenCallback exchangeMailboxTokenCallback;
	private String clientUri;
	private String buildAuthUrl = Routes.Constants.BUILD_AUTH_URL;
	private String exchangeCodeForTokenUrl = Routes.Constants.EXCHANGE_CODE_FOR_TOKEN;
	private static final Logger LOGGER = LoggerFactory.getLogger(NylasFilter.class);

	/**
	 * Constructor for NylasFilter
	 * @param nylasApplication The configured Nylas application
	 * @param defaultScopes The authentication scopes to request from the authenticating user
	 * @param exchangeMailboxTokenCallback A class configured with methods to handle a callback after exchanging token
	 */
	public NylasFilter(
			NylasApplication nylasApplication,
			Scope[] defaultScopes,
			ExchangeMailboxTokenCallback exchangeMailboxTokenCallback
	) {
		this.routes = new Routes(nylasApplication);
		this.defaultScopes = defaultScopes;
		this.exchangeMailboxTokenCallback = exchangeMailboxTokenCallback;
	}

	/**
	 * Set the base route for the client
	 * @param clientUri The route of the client
	 */
	public void setClientUri(String clientUri) {
		this.clientUri = clientUri;
	}

	/**
	 * Override the serving path of the buildAuthUrl endpoint.
	 * Defaults to {@value Routes.Constants#BUILD_AUTH_URL}
	 * @param buildAuthUrl The new path to set buildAuthUrl to
	 */
	public void overrideBuildAuthUrl(String buildAuthUrl) {
		this.buildAuthUrl = buildAuthUrl;
	}

	/**
	 * Override the serving path of the exchangeCodeForToken endpoint.
	 * Defaults to {@value Routes.Constants#EXCHANGE_CODE_FOR_TOKEN}
	 * @param exchangeCodeForTokenUrl The new path to set exchangeCodeForToken to
	 */
	public void overrideExchangeCodeForToken(String exchangeCodeForTokenUrl) {
		this.exchangeCodeForTokenUrl = exchangeCodeForTokenUrl;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.trace("Setting up the Nylas Filter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestURI = httpServletRequest.getRequestURI();
		if(httpServletRequest.getMethod().equals("POST")) {
			if(requestURI.equals(this.buildAuthUrl)) {
				handleBuildAuthUrl(httpServletRequest, httpServletResponse);
			} else if(requestURI.equals(this.exchangeCodeForTokenUrl)) {
				try {
					handleExchangeCodeForToken(httpServletRequest, httpServletResponse);
				} catch (RequestFailedException e) {
					this.exchangeMailboxTokenCallback.onTokenExchangeError(httpServletRequest, httpServletResponse, e);
				}
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		LOGGER.trace("Destroying the Nylas Filter");
	}

	private void handleBuildAuthUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> json = parseRequestBody(request);
		String emailAddress = (String) json.get("email_address");
		String successUrl = (String) json.get("success_url");
		String authUrl = this.routes.buildAuthUrl(defaultScopes, emailAddress, successUrl, this.clientUri, null);
		response.getWriter().write(authUrl);
		response.setStatus(200);
	}

	private void handleExchangeCodeForToken(HttpServletRequest request, HttpServletResponse response) throws IOException, RequestFailedException {
		Map<String, Object> json = parseRequestBody(request);
		String token = (String) json.get("token");
		AccessToken accessToken = this.routes.exchangeCodeForToken(token);
		this.exchangeMailboxTokenCallback.onTokenExchange(request, response, accessToken);
	}

	private Map<String, Object> parseRequestBody(HttpServletRequest request) throws IOException {
		String json = request.getReader().lines().collect(Collectors.joining (System.lineSeparator()));
		return JsonHelper.jsonToMap(json);
	}
}
