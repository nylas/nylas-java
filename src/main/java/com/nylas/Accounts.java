package com.nylas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

public class Accounts extends RestfulDAO<Account> {

	Accounts(NylasClient client, NylasApplication application) {
		super(client, Account.class, "a/" + application.getClientId() + "/accounts", application.getClientSecret());
	}

	public RemoteCollection<Account> list() throws IOException, RequestFailedException {
		return list(new AccountQuery());
	}
	
	public RemoteCollection<Account> list(AccountQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Account get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	/**
	 * Deletes an account. Accounts deleted using this method are immediately unavailable.
	 * Returns null, since there is no job status for immediate deletion.
	 */
	@Override
	public String delete(String id) throws IOException, RequestFailedException {
		return super.delete(id);
	}

	public void downgrade(String accountId) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(accountId).addPathSegment("downgrade");
		client.executePost(authUser, url, null, null);
	}
	
	public void upgrade(String accountId) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(accountId).addPathSegment("upgrade");
		client.executePost(authUser, url, null, null);
	}
	
	public void revokeAllTokensForAccount(String accountId, String keepAccessToken)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(accountId).addPathSegment("revoke-all");
		Map<String, Object> params = new HashMap<>();
		if (keepAccessToken != null) {
			params.put("keep_access_token", keepAccessToken);
		}
		client.executePost(authUser, url, params, null);
	}

	public TokenInfo tokenInfo(String accountId, String accessToken) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(accountId).addPathSegment("token-info");
		Map<String, Object> params = Maps.of("access_token", accessToken);
		return client.executePost(authUser, url, params, TokenInfo.class);
	}
}
