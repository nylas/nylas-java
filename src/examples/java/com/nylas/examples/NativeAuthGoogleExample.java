package com.nylas.examples;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.nylas.GoogleProviderSettings;
import com.nylas.JsonHelper;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NativeAuthGoogleExample {

	private static final Logger log = LogManager.getLogger(NativeAuthGoogleExample.class);

	private static String baseUrl;
	
	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		int port = conf.getInt("http.local.port", 5000);
		log.info("Starting http server listening on port {}", port);
		Server server = new Server(port);
		server.setHandler(new Handler());
		server.start();
		
		baseUrl = findBaseUrl(conf, port);
		log.info("Please visit {} to view this example", baseUrl);
		
		GoogleProviderSettings settings = new GoogleProviderSettings()
				.googleClientId(conf.get("google.client.id"))
				.googleClientSecret(conf.get("google.client.secret"))
				.googleRefreshToken(conf.get("google.refresh.token"))
				;
		
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		
		
//		fetchGoogleRefreshToken(settings);
		
		String code = "4/vgEyqIhXOoWKiar79xhDM-P6FphQ9wooGCkHYqEYlCf6AbZRoZamq4qGMKkVH-JZKl7i7uj_AMOi7Ioer4yOlBs";
//		fetch2(client, settings, code);
		
//		getAccessTokenFromRefreshToken(client, settings);
//		getTokenInfo(client);
		
//		NativeAuthentication authentication = application.nativeAuthentication();
//		AuthRequestBuilder authRequest = authentication.authRequest()
//				.name(conf.get("imap.name"))
//				.emailAddress(conf.get("imap.email"))
//				.providerSettings(settings)
////				.scopes(Scope.EMAIL_DRAFTS, Scope.EMAIL_FOLDERS_AND_LABELS, Scope.EMAIL_METADATA, Scope.EMAIL_MODIFY);
//		.scopes(Scope.EMAIL, Scope.CALENDAR, Scope.CONTACTS, Scope.ROOM_RESOURCES_READ_ONLY);
//				
//		System.out.println("Making a native authentication request for a Google account.");
//		String authorizationCode = authRequest.execute();
//		System.out.println("Succeeded.  Returned authorization code: " + authorizationCode);
//		
//		System.out.println("Using authorization code to request long lived access token.");
//		AccessToken token = authentication.fetchToken(authorizationCode);
//		System.out.println("Succeeded.  Returned token: " + token);
//		
//		System.out.println("Requesting account details with token.");
//		NylasAccount account = client.account(token.getAccessToken());
//		AccountDetail accountDetail = account.fetchAccountByAccessToken();
//		System.out.println("Succeeded.  Account details: " + accountDetail);
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
		OkHttpClient client = new OkHttpClient();
		
		Request request = new Request.Builder().url("http://localhost:4040/api/tunnels").build();
		try (Response response = client.newCall(request).execute()) {
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

	public static class Handler extends AbstractHandler {

	
		@Override
		public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException {
			
			String queryString = request.getQueryString();
			log.info(request.getMethod() + " " + request.getRequestURL()
				+ (queryString == null ? "" : "?" + queryString));
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				log.info(headerName + ": " + request.getHeader(headerName));
			}
			String body = CharStreams.toString(new InputStreamReader(
					request.getInputStream(), Charsets.UTF_8));
			log.info(body);
			log.info("---");
			
			// ack callbacks
			response.setContentLength(0);
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
		}

	}
	

	public static HttpResponse executeGet(HttpTransport transport, JsonFactory jsonFactory, String accessToken,
			GenericUrl url) throws IOException {
		Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
				.setAccessToken(accessToken);
		HttpRequestFactory requestFactory = transport.createRequestFactory(credential);
		return requestFactory.buildGetRequest(url).execute();
	}

	private static void fetchGoogleRefreshToken(GoogleProviderSettings settings) throws IOException {
		List<String> scopes = Arrays.asList(
				"https://www.googleapis.com/auth/gmail.compose",
				"https://www.googleapis.com/auth/gmail.modify",
				"https://www.googleapis.com/auth/gmail.labels",
				"https://www.googleapis.com/auth/gmail.metadata",
				"https://www.googleapis.com/auth/gmail.send",
				"https://www.googleapis.com/auth/calendar",
				"https://www.googleapis.com/auth/contacts",
				"https://www.googleapis.com/auth/admin.directory.resource.calendar.readonly",
				"https://www.googleapis.com/auth/userinfo.email",
				"https://www.googleapis.com/auth/userinfo.profile",
				"https://mail.google.com",
				"email"
				);

		
		HttpUrl url = HttpUrl.get("https://accounts.google.com/o/oauth2/v2/auth").newBuilder()
				.addQueryParameter("client_id", settings.getGoogleClientId())
				.addQueryParameter("redirect_uri", "https://b679d725.ngrok.io/login/google/authorized")
				.addQueryParameter("response_type", "code")
				.addQueryParameter("scope", String.join(" ", scopes))
				.addQueryParameter("access_type", "offline")
				.addQueryParameter("prompt", "consent")
				.addQueryParameter("login_hint", "davenylastest@gmail.com")
				.build();
		
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    Desktop.getDesktop().browse(url.uri());
		}
		
		System.out.println(url);
	}
	
	private static void fetch2(NylasClient client, GoogleProviderSettings settings, String code) throws IOException {
		HttpUrl url = HttpUrl.get("https://oauth2.googleapis.com/token");
		
		FormBody params = new FormBody.Builder()
				.add("client_id", settings.getGoogleClientId())
				.add("client_secret", settings.getGoogleClientSecret())
				.add("redirect_uri", "https://b679d725.ngrok.io/login/google/authorized")
				.add("grant_type", "authorization_code")
				.add("code", code)
				.build();
		
		OkHttpClient httpClient = client.getHttpClient();
		
		
		Request request = new Request.Builder().url(url)
				.post(params)
				.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}
			
			System.out.println("response: " + response.body().string());
		}
		
	}
	
	private static void getAccessTokenFromRefreshToken(NylasClient client, GoogleProviderSettings settings)
			throws IOException {
		//HttpUrl url = HttpUrl.get("https://oauth2.googleapis.com/token");
		HttpUrl url = HttpUrl.get("https://www.googleapis.com/oauth2/v4/token");
		
		FormBody params = new FormBody.Builder()
				.add("client_id", settings.getGoogleClientId())
				.add("client_secret", settings.getGoogleClientSecret())
				.add("refresh_token", settings.getGoogleRefreshToken())
				.add("grant_type", "refresh_token")
				.build();
		
		OkHttpClient httpClient = client.getHttpClient();
		
		
		Request request = new Request.Builder().url(url)
				.post(params)
				.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}
			
			System.out.println("response: " + response.body().string());
		}
	}
	
	private static void getTokenInfo(NylasClient client) throws IOException {
		String token = "ya29.Il-6B0NVq7U2TWEBjlhropbSHzAKCCmwwBtTt3KAtRF9bvMrFJvGVhbtI2RE1CySKoz7Ztykj8O8Lot_oGO9YowAEefh0i1Q4cCa-FRn8QadPeELPUf1RvpWG2AB59AgwQ";
		HttpUrl url = HttpUrl.get("https://www.googleapis.com/oauth2/v3/tokeninfo").newBuilder()
				.addQueryParameter("access_token", token)
				.build();
		
		Request request = new Request.Builder().url(url).get()
				.build();
		
		try (Response response = client.getHttpClient().newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}
			
			System.out.println("response: " + response.body().string());
		}
	}
	
	
}
