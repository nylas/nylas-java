package com.nylas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

public class Accounts extends RestfulCollection<Account, AccountQuery> {

	private final String clientId;
	
	Accounts(NylasClient client, NylasApplication application) {
		super(client, Account.class, "accounts", application.getClientSecret());
		this.clientId = application.getClientId();
	}

	public void downgrade(String accountId) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrlBuilder(accountId).addPathSegment("downgrade");
		client.executePost(authUser, url, null, null);
	}
	
	public void upgrade(String accountId) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrlBuilder(accountId).addPathSegment("upgrade");
		client.executePost(authUser, url, null, null);
	}
	
	public void revokeAllTokensForAccount(String accountId, String keepAccessToken)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrlBuilder(accountId).addPathSegment("revoke-all");
		Map<String, Object> params = new HashMap<>();
		if (keepAccessToken != null) {
			params.put("keep_access_token", keepAccessToken);
		}
		client.executePost(authUser, url, params, null);
	}

	public TokenInfo tokenInfo(String accountId, String accessToken) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrlBuilder(accountId).addPathSegment("token-info");
		Map<String, Object> params = Maps.of("access_token", accessToken);
		return client.executePost(authUser, url, params, TokenInfo.class);
	}
	
	@Override
	protected HttpUrl.Builder getBaseUrlBuilder() {
		return super.getBaseUrlBuilder()
				.addPathSegment("a")
				.addPathSegment(clientId);
	}
}
