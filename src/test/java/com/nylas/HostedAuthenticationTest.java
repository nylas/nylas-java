package com.nylas;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HostedAuthenticationTest {

	private MockNylas mockNylas;
	private HostedAuthentication hostedAuth;
	
	@BeforeEach
	private void init() {
		mockNylas = new MockNylas();
		hostedAuth = mockNylas.client.application("clientId", "clientSecret").hostedAuthentication();
	}
	
	@AfterEach
	private void cleanup() {
		mockNylas.cleanup();
	}
	
	@Test
	public void testFetchToken() throws Exception {
		String sampleToken = "GrDpZINsqw9ofPvfLjcAtuFnS5kNHo";
		
		mockNylas.server.stubFor(post(urlPathEqualTo("/oauth/token"))
				.withRequestBody(matchingJsonPath("$.client_id"))
				.withRequestBody(matchingJsonPath("$.client_secret"))
				.withRequestBody(matchingJsonPath("$.code"))
				.withRequestBody(matchingJsonPath("$.grant_type"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody("{\"access_token\":\"" + sampleToken + "\","
								+ "\"account_id\":\"uk03ame4ugzwk1s0uvqkew7ys\","
								+ "\"email_address\":\"test@example.com\","
								+ "\"provider\":\"gmail\","
								+ "\"token_type\":\"bearer\"}"
								)
						)
				);
		
		AccessToken token = hostedAuth.fetchToken("mycode");
		
		assertEquals(sampleToken, token.getAccessToken());
	}
	
	@Test
	public void buildUrl() throws Exception {
		// should throw with no redirect URI specified
		assertThrows(IllegalStateException.class, () ->
			hostedAuth.urlBuilder().scopes(Scope.CALENDAR).buildUrl()
		);
		
		// should throw with no scopes specified
		assertThrows(IllegalStateException.class, () ->
			hostedAuth.urlBuilder().redirectUri("https://example.com/auth").buildUrl()
		);

	}
}
