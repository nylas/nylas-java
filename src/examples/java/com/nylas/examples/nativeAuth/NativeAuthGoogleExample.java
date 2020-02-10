package com.nylas.examples.nativeAuth;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.google.common.collect.ImmutableMap;
import com.nylas.AccessToken;
import com.nylas.AccountDetail;
import com.nylas.GoogleProviderSettings;
import com.nylas.JsonHelper;
import com.nylas.NativeAuthentication;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.examples.ExampleConf;
import com.nylas.NylasAccount;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.ProviderSettings;
import com.nylas.RequestFailedException;
import com.nylas.Scope;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NativeAuthGoogleExample {

	private static final Logger log = LogManager.getLogger(NativeAuthGoogleExample.class);

	private static final String AUTHORIZED_PATH = "/login/google/authorized";
	private static final String AUTHORIZE_NYLAS_PATH = "/authorize_nylas";
	private static final String EXAMPLE_CSRF_TOKEN = "UNGUESSABLE_CSRF_TOKEN";
	
	private static ExampleConf conf = new ExampleConf();
	private static HtmlTemplateResponder responder = new HtmlTemplateResponder("/html-templates/native-auth-google");
	private static OkHttpClient httpClient = new OkHttpClient();
	private static NylasClient nylasClient = new NylasClient();
	private static String baseUrl;
	private static List<Scope> scopes = Arrays.asList(Scope.values());

	public static void main(String[] args) throws Exception {
		int port = conf.getInt("http.local.port", 5000);
		log.info("Starting http server listening on port {}", port);
		Server server = new Server(port);
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(StartServlet.class, "");
		handler.addServletWithMapping(AuthorizedServlet.class, AUTHORIZED_PATH);
		handler.addServletWithMapping(AuthorizeNylasServlet.class, AUTHORIZE_NYLAS_PATH);
		server.setHandler(handler);
		server.start();
		
		baseUrl = findBaseUrl(conf, port);
		log.info("Using OAuth redirect URI of \"{}\"", getOAuthRedirectUri());
		log.info("Be sure that this is registered as an Authorized redirect URI "
				+ "for your OAuth Client in the Credentials section of the Google developer console.");

		log.info("Visit to {} to view this example", baseUrl);
	}
	
	private static String getOAuthRedirectUri() {
		return baseUrl + AUTHORIZED_PATH;
	}
	
	public static class StartServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
			Set<String> googleScopes = GoogleProviderSettings.getMatchingGoogleScopes(scopes);
			// userinfo.email is required for google userinfo endpoint to return email attribute
			// Nylas also appears to require this when it fetches tokeninfo
			googleScopes.add("https://www.googleapis.com/auth/userinfo.email");
			// userinfo.profile is required for google userinfo endpoint to return name attribute
			// this makes the example nicer in fetching the user name from Google and using it with Nylas, but is not
			// required by Nylas if you have the user name from somewhere else.
			googleScopes.add("https://www.googleapis.com/auth/userinfo.profile");
			
			HttpUrl url = HttpUrl.get("https://accounts.google.com/o/oauth2/v2/auth").newBuilder()
					.addQueryParameter("client_id", getGoogleClientId())
					.addQueryParameter("redirect_uri", getOAuthRedirectUri())
					.addQueryParameter("response_type", "code")
					.addQueryParameter("scope", String.join(" ", googleScopes))
					.addQueryParameter("access_type", "offline")
					// A user prompt is required for Google to grant the refresh_token in addition to the access_token.
					// Google may skip the prompt if the user has  previously granted these scopes to this client.
					// Adding this parameter prompt=consent ensures that the prompt is never skipped.
					.addQueryParameter("prompt", "consent")
					// in a real system, the state parameter should be a unguessable function of the user account
					// or session to prevent CSRF attacks
					.addQueryParameter("state", EXAMPLE_CSRF_TOKEN)
					.build();
			
			Map<String, String> dataModel = ImmutableMap.of("googleAuthUrl", url.toString());
			
			responder.respond("step1.ftlh", dataModel, response);
		}
	}
	
	public static class AuthorizedServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
			String code = request.getParameter("code");
			String state = request.getParameter("state");
			
			// in a real system, verify that the returned state matches the expected function of the current user
			// account/session.  this prevents a CSRF attack where an attacker can associate the wrong google account
			// to this customer's user account.
			if (!EXAMPLE_CSRF_TOKEN.equals(state)) {
				log.error("Unexpected or missing OAuth state parameter in response: " + state);
				return;
			}
			
			HttpUrl tokenUrl = HttpUrl.get("https://oauth2.googleapis.com/token");	
			FormBody params = new FormBody.Builder()
					.add("client_id", getGoogleClientId())
					.add("client_secret", getGoogleClientSecret())
					.add("redirect_uri", getOAuthRedirectUri())
					.add("grant_type", "authorization_code")
					.add("code", code)
					.build();
			
			Request tokenRequest = new Request.Builder().url(tokenUrl).post(params).build();
			Map<String, Object> tokenData = executeRequestForJson(tokenRequest);
			String refreshToken = (String) tokenData.get("refresh_token");
			String accessToken = (String) tokenData.get("access_token");
			
			HttpUrl userInfoUrl = HttpUrl.get("https://www.googleapis.com/oauth2/v3/userinfo").newBuilder()
					.addQueryParameter("access_token", accessToken)
					.build();
			Map<String, Object> userInfoData = executeRequestForJson(new Request.Builder().url(userInfoUrl).build());
			String name = (String) userInfoData.get("name");
			String email = (String) userInfoData.get("email");
			
			HttpUrl authorizeNylasUrl = HttpUrl.get(baseUrl + AUTHORIZE_NYLAS_PATH).newBuilder()
					.addQueryParameter("refresh_token", refreshToken)
					.addQueryParameter("name", name)
					.addQueryParameter("email", email)
					.build();
			
			Map<String, String> dataModel = ImmutableMap.of(
					"accessToken", accessToken,
					"refreshToken", refreshToken,
					"name", name,
					"email", email,
					"authNylasUrl", authorizeNylasUrl.toString());
			responder.respond("step2.ftlh", dataModel, response);
		}
	}
	
	private static Map<String, Object> executeRequestForJson(Request request) throws IOException {
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected response " + response);
			}
			
			return JsonHelper.jsonToMap(response.body().string());
		}
	}
	
	public static class AuthorizeNylasServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
			
			String refreshToken = request.getParameter("refresh_token");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			
			NylasApplication application = nylasClient.application(
					conf.get("nylas.client.id"),
					conf.get("nylas.client.secret"));
			NativeAuthentication authentication = application.nativeAuthentication();
			
			ProviderSettings settings = ProviderSettings.google()
					.googleClientId(getGoogleClientId())
					.googleClientSecret(getGoogleClientSecret())
					.googleRefreshToken(refreshToken);

			AuthRequestBuilder authRequest = authentication.authRequest()
					.name(name)
					.emailAddress(email)
					.providerSettings(settings)
					.scopes(scopes)
					;
					
			try {
				log.info("Making a native authentication request for a Google account.");
				String authorizationCode = authRequest.execute();
				log.info("Succeeded.  Returned authorization code: " + authorizationCode);
				
				log.info("Using authorization code to request long lived access token.");
				AccessToken token = authentication.fetchToken(authorizationCode);
				log.info("Succeeded.  Returned token: " + token);
				
				log.info("Requesting account details with token.");
				NylasAccount account = nylasClient.account(token.getAccessToken());
				AccountDetail accountDetail = account.fetchAccountByAccessToken();
				log.info("Succeeded. Account detail: " + accountDetail);

				responder.respond("step3.ftlh", ImmutableMap.of("accountDetail", accountDetail), response);
			} catch (RequestFailedException e) {
				throw new IOException (e);
			}
		}
	}
	
	static class Tunnel { String proto; String public_url; }
	static class Tunnels { List<Tunnel> tunnels; }
	private static String findBaseUrl(ExampleConf conf, int port) {
		// FIRST check for configuration
		String baseUrl = conf.get("http.base.url", null);
		if (baseUrl != null) {
			log.info("Found base url in example.properties: {}", baseUrl);
			return baseUrl;
		} else {
			log.info("No base url found in example.properties, checking to see if ngrok is running...");
		}
		
		// SECOND see if ngrok is running
		Request request = new Request.Builder().url("http://localhost:4040/api/tunnels").build();
		try (Response response = httpClient.newCall(request).execute()) {
			Tunnels tunnels = JsonHelper.<Tunnels>adapter(Tunnels.class).fromJson(response.body().source());
			for (Tunnel t : tunnels.tunnels) {
				if ("https".equals(t.proto)) {
					baseUrl = t.public_url;
					log.info("Found https base url from ngrok: {}", baseUrl);
					return baseUrl;
				}
			}
			log.debug("No https tunnel found in ngrok response");
		} catch (Exception e) {
			log.debug("Failed to find https tunnel from local ngrok, exception: {}", e.getMessage());
		}
		
		// THIRD fall back to localhost
		baseUrl = "http://localhost:" + port;
		log.info("Falling back to using localhost base url: {}", baseUrl);
		return baseUrl;
	}

	private static String getGoogleClientId() {
		return conf.get("google.client.id");
	}
	
	private static String getGoogleClientSecret() {
		return conf.get("google.client.secret");
	}

	/**
	 * Debugging method for getting a google access token from a refresh token
	 */
	static void getAccessTokenFromRefreshToken(String refreshToken)
			throws IOException {
		HttpUrl url = HttpUrl.get("https://www.googleapis.com/oauth2/v4/token");
		
		FormBody params = new FormBody.Builder()
				.add("client_id", getGoogleClientId())
				.add("client_secret", getGoogleClientSecret())
				.add("refresh_token", refreshToken)
				.add("grant_type", "refresh_token")
				.build();
		
		Request request = new Request.Builder().url(url)
				.post(params)
				.build();
		
		Map<String, Object> accessTokenData = executeRequestForJson(request);
		System.out.println(accessTokenData);
	}

	/**
	 * Debugging method for getting token info from google about an access token
	 */
	static void getTokenInfo(String accessToken) throws IOException {
		HttpUrl url = HttpUrl.get("https://www.googleapis.com/oauth2/v3/tokeninfo").newBuilder()
				.addQueryParameter("access_token", accessToken)
				.build();
		
		Request request = new Request.Builder().url(url).build();
		
		Map<String, Object> tokenInfoData = executeRequestForJson(request);
		System.out.println(tokenInfoData);
	}
	
}
