package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.Types;

import okhttp3.HttpUrl;

public class Accounts {

	private final NylasClient client;
	private final Application application;
	
	Accounts(NylasClient client, Application application) {
		this.client = client;
		this.application = application;
	}

	private static final Type ACCOUNT_LIST_TYPE = Types.newParameterizedType(List.class, Account.class);
	public List<Account> list(AccountsQuery query) throws IOException, RequestFailedException {
		HttpUrl url = getAccountsUrl(query);
		return client.executeGet(application.getClientSecret(), url, ACCOUNT_LIST_TYPE);
	}
	
	public Account get(String accountId) throws IOException, RequestFailedException {
		HttpUrl url = getAccountUrl(accountId, null);
		return client.executeGet(application.getClientSecret(), url, Account.class);
	}

	public void downgrade(String accountId) throws IOException, RequestFailedException {
		HttpUrl url = getAccountUrl(accountId, "downgrade");
		client.executePost(application.getClientSecret(), url, null, null);
	}
	
	public void upgrade(String accountId) throws IOException, RequestFailedException {
		HttpUrl url = getAccountUrl(accountId, "upgrade");
		client.executePost(application.getClientSecret(), url, null, null);
	}
	
	public void revokeAllTokensForAccount(String accountId, String keepAccessToken)
			throws IOException, RequestFailedException {
		HttpUrl url = getAccountUrl(accountId, "revoke-all");
		Map<String, Object> params = new HashMap<>();
		if (keepAccessToken != null) {
			params.put("keep_access_token", keepAccessToken);
		}
		client.executePost(application.getClientSecret(), url, params, null);
	}
	
	private HttpUrl getAccountsUrl(AccountsQuery query) {
		HttpUrl.Builder urlBuilder = client.getBaseUrl().newBuilder()
			.addPathSegment("a")
			.addPathSegment(application.getClientId())
			.addPathSegment("accounts");
			if (query != null) {
				query.addParameters(urlBuilder);
			}
		return urlBuilder.build();
	}

	private HttpUrl getAccountUrl(String accountId, String accountEndpoint) {
		HttpUrl.Builder urlBuilder = getAccountsUrl(null).newBuilder().addPathSegment(accountId);
		if (accountEndpoint != null) {
			urlBuilder.addPathSegment(accountEndpoint);
		}
		return urlBuilder.build();
	}
	
}
