package com.nylas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;

public class Accounts extends RestfulCollection<Account, AccountQuery> {

	Accounts(NylasClient client, NylasApplication application) {
		super(client, Account.class, "a/" + application.getClientId() + "/accounts", application.getClientSecret());
	}

	@Override
	public List<Account> list() throws IOException, RequestFailedException {
		return super.list();
	}

	@Override
	public List<Account> list(AccountQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Account get(String id) throws IOException, RequestFailedException {
		return super.get(id);
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
