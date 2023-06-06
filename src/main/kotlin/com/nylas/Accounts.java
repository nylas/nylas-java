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
		// TODO: Downgrade should return the result of the operation?
		HttpUrl.Builder url = getInstanceUrl(accountId).addPathSegment("downgrade");
		client.executePost(authUser, url, null, null);
	}
	
	public void upgrade(String accountId) throws IOException, RequestFailedException {
		// TODO: Upgrade should return the result of the opreation?
		HttpUrl.Builder url = getInstanceUrl(accountId).addPathSegment("upgrade");
		client.executePost(authUser, url, null, null);
	}
	
	public void revokeAllTokensForAccount(String accountId, String keepAccessToken)
			throws IOException, RequestFailedException {
		// TODO: revoke should return operation result?
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

	/**
	 * Updates the metadata on the given message, overwriting all previous metadata on the message.
	 */
	public Account setMetadata(String accountId, Map<String, String> metadata)
			throws IOException, RequestFailedException {
		return super.update(accountId, Maps.of("metadata", metadata));
	}

	private Map<String, String> getMetadata(String accountId) throws IOException, RequestFailedException {
		Account account = get(accountId);
		return account.getMetadata();
	}

	/**
	 * Add single metadata key-value pair to the event
	 *
	 * @param key The key of the metadata entry
	 * @param value The value of the metadata entry
	 * @return The account object after adding the new metadata
	 */
	public Account addMetadata(String accountId, String key, String value) throws IOException, RequestFailedException {
		Map<String, String> metadata = getMetadata(accountId);
		if(metadata == null) {
			metadata = new HashMap<>();
		}
		metadata.put(key, value);
		return setMetadata(accountId, metadata);
	}

	/**
	 * Convenience method to remove a metadata pair from an account.
	 *
	 * @param accountId The ID of the account
	 * @param key The key to delete
	 * @return true if the metadata pair was removed, and false no action was taken
	 */
	public boolean removeMetadata(String accountId, String key) throws IOException, RequestFailedException {
		Map<String, String> metadata = getMetadata(accountId);
		if(metadata != null && metadata.remove(key) != null) {
			setMetadata(accountId, metadata);
			return true;
		}
		return false;
	}
}
