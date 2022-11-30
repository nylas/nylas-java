package com.nylas.server_bindings;

import com.nylas.*;
import com.nylas.services.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A filter for backed frameworks that enables seamless integration with useful
 * Nylas functionality such as authentication. Currently supported routes are:
 *
 * <br> 1. '/nylas/generate-auth-url': Building the URL for authenticating users to
 * 		your application via Hosted Authentication
 * <br> 2. '/nylas/exchange-mailbox-token': Exchange an authorization code for an access token
 * <br> 3. '/nylas/webhook': Verifies incoming webhook authenticity, parses it as an object,
 * 			then can pass it to a callback function (if set)
 */
public class NylasFilter implements Filter {
	private final Routes routes;
	private final Scope[] defaultScopes;
	private final ExchangeMailboxTokenCallback exchangeMailboxTokenCallback;
	private WebhookNotificationCallback webhookNotificationCallback;
	private String clientUri;
	private String buildAuthUrl = Routes.Constants.BUILD_AUTH_URL;
	private String exchangeCodeForTokenUrl = Routes.Constants.EXCHANGE_CODE_FOR_TOKEN;
	private String webhooksUrl = Routes.Constants.WEBHOOKS;
	private static final Logger log = LoggerFactory.getLogger(NylasFilter.class);

	/**
	 * Constructor for NylasFilter
	 * @param nylasApplication The configured Nylas application
	 * @param defaultScopes The authentication scopes to request from the authenticating user
	 * @param exchangeMailboxTokenCallback A class configured with a callback after exchanging token
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
	 * Set the class responsible for processing webhook notifications
	 * @param webhookNotificationCallback class configured with a callback after receiving a webhook notification
	 */
	public void setWebhookNotificationCallback(WebhookNotificationCallback webhookNotificationCallback) {
		this.webhookNotificationCallback = webhookNotificationCallback;
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

	/**
	 * Override the serving path of the webhooks endpoint.
	 * Defaults to {@value Routes.Constants#WEBHOOKS}
	 * @param webhooksUrl The new path to set webhooks to
	 */
	public void overrideWebhooksUrl(String webhooksUrl) {
		this.webhooksUrl = webhooksUrl;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.trace("Setting up the Nylas Filter");
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
			} else if(requestURI.equals(this.webhooksUrl)) {
				handeWebhooks(httpServletRequest, httpServletResponse);
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		log.trace("Destroying the Nylas Filter");
	}

	private void handleBuildAuthUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> json = parseRequestBody(request);
		String emailAddress = (String) json.get("email_address");
		String successUrl = (String) json.get("success_url");
		String authUrl = this.routes.buildAuthUrl(defaultScopes, emailAddress, successUrl, this.clientUri, null);
		response.getWriter().write(authUrl);
		response.setStatus(200);
	}

	private void handleExchangeCodeForToken(HttpServletRequest request, HttpServletResponse response)
			throws IOException, RequestFailedException {
		Map<String, Object> json = parseRequestBody(request);
		String token = (String) json.get("token");
		AccessToken accessToken = this.routes.exchangeCodeForToken(token);
		this.exchangeMailboxTokenCallback.onTokenExchange(request, response, accessToken);
	}

	private void handeWebhooks(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String jsonString = request.getReader().lines().collect(Collectors.joining (System.lineSeparator()));
		String nylasSignatureHeader = request.getHeader(Notification.NYLAS_SIGNATURE_HEADER);
		if(nylasSignatureHeader != null && this.routes.verifyWebhookSignature(nylasSignatureHeader, jsonString)) {
			if(webhookNotificationCallback != null) {
				Notification parsedNotification = Notification.parseNotification(jsonString);
				webhookNotificationCallback.onNotification(request, response, parsedNotification);
			} else {
				response.getWriter().write(jsonString);
				response.setContentType("application/json");
				response.setStatus(200);
			}
		} else {
			if(webhookNotificationCallback != null) {
				webhookNotificationCallback.onUnverifiedNotification(request, response);
			} else {
				String errorMessage = String.format(
						"Webhook notification received but was not verified. %s value: %s",
						Notification.NYLAS_SIGNATURE_HEADER,
						nylasSignatureHeader
				);
				Map<String, Object> errorResponse = Collections.singletonMap("error", errorMessage);
				response.getWriter().write(JsonHelper.mapToJson(errorResponse));
				response.setContentType("application/json");
				response.setStatus(400);
			}
		}
	}

	private Map<String, Object> parseRequestBody(HttpServletRequest request) throws IOException {
		String json = request.getReader().lines().collect(Collectors.joining (System.lineSeparator()));
		return JsonHelper.jsonToMap(json);
	}
}
