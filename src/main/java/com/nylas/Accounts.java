package com.nylas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

public class Accounts extends RestfulCollection<Account, AccountQuery> {

	private final String clientId;
	
	Accounts(NylasClient client, Application application) {
		super(client, Account.class, "accounts", application.getClientSecret());
		this.clientId = application.getClientId();
	}

	public void downgrade(String accountId) throws IOException, RequestFailedException {
		HttpUrl url = getAccountUrl(accountId, "downgrade");
		client.executePost(authUser, url, null, null);
	}
	
	public void upgrade(String accountId) throws IOException, RequestFailedException {
		HttpUrl url = getAccountUrl(accountId, "upgrade");
		client.executePost(authUser, url, null, null);
	}
	
	public void revokeAllTokensForAccount(String accountId, String keepAccessToken)
			throws IOException, RequestFailedException {
		HttpUrl url = getAccountUrl(accountId, "revoke-all");
		Map<String, Object> params = new HashMap<>();
		if (keepAccessToken != null) {
			params.put("keep_access_token", keepAccessToken);
		}
		client.executePost(authUser, url, params, null);
	}

	@Override
	protected HttpUrl.Builder getBaseUrlBuilder() {
		return super.getBaseUrlBuilder()
				.addPathSegment("a")
				.addPathSegment(clientId);
	}
	
	private HttpUrl getAccountUrl(String accountId, String accountEndpoint) {
		return getInstanceUrl(accountId).newBuilder().addPathSegment(accountEndpoint).build();
	}
}
