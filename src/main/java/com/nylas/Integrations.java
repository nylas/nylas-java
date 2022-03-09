package com.nylas;

import com.nylas.Integration.Provider;
import okhttp3.HttpUrl;

import com.nylas.NylasClient.AuthMethod;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class Integrations extends RestfulDAO<Integration> {

	public enum Region { US, EU }
	private String appName = "beta";
	private String region = Region.US.toString().toLowerCase();
	private static final String endpointPath = "connect/integrations";


	Integrations(NylasClient client, NylasApplication application) {
		super(client, Integration.class, endpointPath, Integrations.authBuilder(application));
		this.authMethod = AuthMethod.BASIC_WITH_CREDENTIALS;
	}

	/**
	 * Set the name of the application to prefix the URL for all integration calls for this instance
	 * resulting in something like {@code https://{appName}.us.nylas.com/connect/integrations}
	 * @param appName The name of the application
	 * @return The {@link Integrations} object with the url prefixed
	 */
	public Integrations appName(String appName) {
		this.appName = appName;
		return this;
	}

	/**
	 * Set the region to prefix the URL for all integration calls for this instance
	 * resulting in something like {@code https://beta.{us/eu}.nylas.com/connect/integrations}
	 * @param region The region
	 * @return The {@link Integrations} object with the url prefixed
	 */
	public Integrations region(Region region) {
		this.region = region.toString().toLowerCase();
		return this;
	}

	/**
	 * List all integrations
	 * <br> {@code query} is set to a default query
	 * @see #list(IntegrationQuery)
	 */
	public RemoteCollection<Integration> list() throws IOException, RequestFailedException {
		return list(new IntegrationQuery());
	}

	/**
	 * List all integrations
	 * @param query Additional query options
	 * @return A collection of integrations for the application
	 * @see <a href="https://developer.nylas.com/docs/api/uas#get/connect/integrations">List Integrations</a>
	 */
	public RemoteCollection<Integration> list(IntegrationQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	/**
	 * Get an existing integration for a provider
	 * @param provider The provider
	 * @return The existing integration
	 * @see <a href="https://developer.nylas.com/docs/api/uas#get/connect/integrations/provider">Get Integration</a>
	 */
	public Integration get(Provider provider) throws IOException, RequestFailedException {
		return super.get(provider.toString());
	}

	/**
	 * Create a new integration
	 * @param integration The integration to create
	 * @param provider The provider the integration is for
	 * @return The newly created integration
	 * @see <a href="https://developer.nylas.com/docs/api/uas#post/connect/integrations">Create Integration</a>
	 */
	public Integration create(Integration integration, Provider provider) throws IOException, RequestFailedException {
		integration.validate();
		Map<String, Object> body = integration.getWritableFields(true);
		body.put("provider", provider.toString());
		return super.create(body);
	}

	/**
	 * Update an existing integration
	 * <br> {@code provider} is pulled from the integration object
	 * @throws IllegalArgumentException if the integration object does not have a provider
	 * @see #update(Integration, Provider)
	 */
	public Integration update(Integration integration) throws IOException, RequestFailedException {
		if(!integration.hasProvider()) {
			throw new IllegalArgumentException("Integration object provided has no provider, unable to update.");
		}

		return update(integration, integration.getProvider());
	}

	/**
	 * Update an existing integration
	 * @param integration The integration with updated values
	 * @param provider The provider the integration is for
	 * @return The updated integration
	 * @see <a href="https://developer.nylas.com/docs/api/uas#patch/connect/integrations/provider">Update Integration</a>
	 */
	public Integration update(Integration integration, Provider provider) throws IOException, RequestFailedException {
		integration.validate();
		return super.updatePatch(provider.toString(), integration.getWritableFields(false), null);
	}

	/**
	 * A helper method that either creates an integration if it doesn't exist or updates it
	 * @param integration The integration to create/update
	 * @param provider The provider the integration is for
	 * @return The newly created/updated integration
	 */
	public Integration save(Integration integration, Provider provider) throws IOException, RequestFailedException {
		return integration.hasId() ? update(integration) : create(integration, provider);
	}

	/**
	 * Deletes an existing integration for a provider
	 * @param provider The provider of the integration to delete
	 * @see <a href="https://developer.nylas.com/docs/api/uas#delete/connect/integrations/provider">Delete Integration</a>
	 */
	public void delete(Provider provider) throws IOException, RequestFailedException {
		super.delete(provider.toString());
	}

	protected HttpUrl.Builder getCollectionUrl() {
		return new HttpUrl.Builder()
				.scheme("https")
				.host(String.format("%s.%s.nylas.com", appName, region))
				.addPathSegments(endpointPath);
	}

	private static String authBuilder(NylasApplication application) {
		String credentials = String.format("%s:%s", application.getClientId(), application.getClientSecret());
		return Base64.getEncoder().encodeToString(credentials.getBytes());
	}
}
