package com.nylas;

import okhttp3.HttpUrl;

import java.util.Base64;

abstract class UASDAO<M extends RestfulModel, D extends UASDAO<M, D>> extends RestfulDAO<M> {

	public enum Region { US, EU }
	protected String appName = "beta";
	protected String region = Region.US.toString().toLowerCase();

	UASDAO(NylasClient client, NylasApplication application, Class<M> modelClass, String endpointPath) {
		super(client, modelClass, endpointPath, UASDAO.authBuilder(application));
		this.authMethod = NylasClient.AuthMethod.BASIC_WITH_CREDENTIALS;
	}

	/**
	 * Set the name of the application to prefix the URL for all integration calls for this instance
	 * resulting in something like {@code https://{appName}.us.nylas.com/connect/integrations}
	 * @param appName The name of the application
	 * @return The {@link Integrations} object with the url prefixed
	 */
	public D appName(String appName) {
		this.appName = appName;
		return self();
	}

	/**
	 * Set the region to prefix the URL for all integration calls for this instance
	 * resulting in something like {@code https://beta.{us/eu}.nylas.com/connect/integrations}
	 * @param region The region
	 * @return The {@link Integrations} object with the url prefixed
	 */
	public D region(Region region) {
		this.region = region.toString().toLowerCase();
		return self();
	}

	protected HttpUrl.Builder getCollectionUrl() {
		return new HttpUrl.Builder()
				.scheme("https")
				.host(String.format("%s.%s.nylas.com", appName, region))
				.addPathSegments(this.collectionPath);
	}

	// helper method for fluent builder style without type warnings
	@SuppressWarnings("unchecked")
	protected final D self() {
		return (D) this;
	}

	private static String authBuilder(NylasApplication application) {
		String credentials = String.format("%s:%s", application.getClientId(), application.getClientSecret());
		return Base64.getEncoder().encodeToString(credentials.getBytes());
	}
}
