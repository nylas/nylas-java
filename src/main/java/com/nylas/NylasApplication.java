package com.nylas;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;

public class NylasApplication {

	private final NylasClient client;
	private final String clientId;
	private final String clientSecret;
	
	NylasApplication(NylasClient client, String clientId, String clientSecret) {
		this.client = client;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}
	
	public NylasClient getClient() {
		return client;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
	
	public HostedAuthentication hostedAuthentication() {
		return new HostedAuthentication(this);
	}

	public NativeAuthentication nativeAuthentication() {
		return new NativeAuthentication(this);
	}
	
	public Accounts accounts() {
		return new Accounts(client, this);
	}
	
	public Webhooks webhooks() {
		return new Webhooks(client, this);
	}

	public Components components() {
		return new Components(client, this);
	}

	public UAS uas() {
		return new UAS(client, this);
	}
	
	/**
	 * Get the application details for this application
	 */
	public ApplicationDetail getApplicationDetail() throws IOException, RequestFailedException {
		return client.executeGet(clientSecret, getApplicationUrl(), ApplicationDetail.class);
	}
	
	/**
	 * Updates the name of this application.
	 * @return the updated ApplicationDetail
	 */
	public ApplicationDetail setName(String name) throws IOException, RequestFailedException {
		return updateApplicationDetail(Maps.of("application_name", name));
	}
	
	/**
	 * Updates the redirect_uris of this application.
	 * Overwrites any existing redirect_uris with this new list.
	 * @return the updated ApplicationDetail
	 */
	public ApplicationDetail setRedirectUris(List<String> redirectUris) throws IOException, RequestFailedException {
		return updateApplicationDetail(Maps.of("redirect_uris", redirectUris));
	}
	
	/**
	 * Adds a new redirect uri if it is not already present.
	 * @return the updated ApplicationDetail if the uri is new, or the existing ApplicationDetail otherwise
	 */
	public ApplicationDetail addRedirectUri(String uri) throws IOException, RequestFailedException {
		ApplicationDetail existingDetail = getApplicationDetail();
		List<String> existingUris = existingDetail.getRedirectUris();
		if (!existingUris.contains(uri)) {
			existingUris.add(uri);
			return setRedirectUris(existingUris);
		} else {
			return existingDetail;
		}
	}
	
	/**
	 * Removes a redirect uri if it is present.
	 * @return the updated ApplicationDetail if the uri was removed, or the existing ApplicationDetail otherwise
	 */
	public ApplicationDetail removeRedirectUri(String uri) throws IOException, RequestFailedException {
		ApplicationDetail existingDetail = getApplicationDetail();
		List<String> existingUris = existingDetail.getRedirectUris();
		if (existingUris.contains(uri)) {
			existingUris.remove(uri);
			return setRedirectUris(existingUris);
		} else {
			return existingDetail;
		}
	}
	
	private ApplicationDetail updateApplicationDetail(Map<String, Object> params)
			throws IOException, RequestFailedException {
		return client.executePut(clientSecret, getApplicationUrl(), params, ApplicationDetail.class);
	}
	
	/**
	 * Fetch the set of IP Addresses that Nylas may use to access mail servies or other service providers for this
	 * application.
	 */
	public IPAddressWhitelist fetchIpAddressWhitelist() throws IOException, RequestFailedException {
		HttpUrl.Builder url = getApplicationUrl().addPathSegment("ip_addresses");
		return client.executeGet(clientSecret, url, IPAddressWhitelist.class);
	}
	
	private HttpUrl.Builder getApplicationUrl() {
		return client.newUrlBuilder()
				.addPathSegment("a")
				.addPathSegment(clientId);
	}

}
