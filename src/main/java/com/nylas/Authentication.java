package com.nylas;

import okhttp3.HttpUrl;

import java.util.Base64;

public class Authentication {

	/** Available regions for UAS */
	public enum Region { US, EU }
	protected String appName = "beta";
	protected String region = Authentication.Region.US.toString().toLowerCase();
	protected final NylasClient client;
	protected final String authUser;

	/** Supported providers for UAS integrations */
	public enum Provider {
		GOOGLE,
		MICROSOFT,
		IMAP,
		ZOOM,

		;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public Authentication(NylasClient client, NylasApplication application) {
		this.client = client;
		this.authUser = Authentication.authBuilder(application);
	}

	/**
	 * Set the name of the application to prefix the URL for all integration calls for this instance
	 * resulting in something like {@code https://{appName}.us.nylas.com/connect/integrations}
	 * @param appName The name of the application
	 * @return The {@link Integrations} object with the url prefixed
	 */
	public Authentication appName(String appName) {
		this.appName = appName;
		return this;
	}

	/**
	 * Set the region to prefix the URL for all integration calls for this instance
	 * resulting in something like {@code https://beta.{us/eu}.nylas.com/connect/integrations}
	 * @param region The region
	 * @return The {@link Integrations} object with the url prefixed
	 */
	public Authentication region(Region region) {
		this.region = region.toString().toLowerCase();
		return this;
	}

	/**
	 * Integrations API for integrating a provider to the Nylas application
	 * @return The Integration API configured with the {@link #appName} and {@link #region}
	 * @see <a href="https://developer.nylas.com/docs/api/uas#tag--Integrations">UAS - Integrations</a>
	 */
	public Integrations integrations() {
		return new Integrations(this.client, this.authUser, buildUASUrl());
	}

	/**
	 * Native Authentication for the integrated provider
	 * @return The Grants API configured with the {@link #appName} and {@link #region}
	 * @see <a href="https://developer.nylas.com/docs/api/uas#tag--Grants">UAS - Grants</a>
	 */
	public Grants grants() {
		return new Grants(this.client, this.authUser, buildUASUrl());
	}

	/**
	 * Hosted Authentication for the integrated provider
	 * @return Hosted Authentication configured with the {@link #appName} and {@link #region}
	 * @see <a href="https://developer.nylas.com/docs/api/uas#tag--Hosted-Auth">UAS - Hosted Auth</a>
	 */
	public IntegrationHostedAuthentication hostedAuthentication() {
		return new IntegrationHostedAuthentication(this.client, this.authUser, buildUASUrl());
	}

	private HttpUrl.Builder buildUASUrl() {
		return new HttpUrl.Builder()
				.scheme("https")
				.host(String.format("%s.%s.nylas.com", appName, region));
	}

	private static String authBuilder(NylasApplication application) {
		String credentials = String.format("%s:%s", application.getClientId(), application.getClientSecret());
		return Base64.getEncoder().encodeToString(credentials.getBytes());
	}
}
