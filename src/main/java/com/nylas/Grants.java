package com.nylas;

import okhttp3.HttpUrl;

import java.io.IOException;

public class Grants extends UASDAO<Grant, Grants> {

	Grants(NylasClient client, NylasApplication application) {
		super(client, application, Grant.class, "connect/grants");
	}

	/**
	 * List all grants
	 * <br> {@code query} is set to a default query
	 * @see #list(GrantQuery)
	 */
	public RemoteCollection<Grant> list() throws IOException, RequestFailedException {
		return list(new GrantQuery());
	}

	/**
	 * List all grants
	 * @param query Additional query options
	 * @return A collection of grants for the application
	 * @see <a href="https://developer.nylas.com/docs/api/uas#get/connect/grants">List Grants</a>
	 */
	public RemoteCollection<Grant> list(GrantQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	/**
	 * Get an existing grant
	 * @param id The grant ID
	 * @return The existing grant
	 * @see <a href="https://developer.nylas.com/docs/api/uas#get/connect/grants/grantID">Get Grant</a>
	 */
	public Grant get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	/**
	 * Create a new grant
	 * @param grant The grant to create
	 * @return The newly created grant
	 * @see <a href="https://developer.nylas.com/docs/api/uas#post/connect/grants">Create Grant</a>
	 */
	public Grant create(Grant grant) throws IOException, RequestFailedException {
		grant.validate();
		return super.create(grant);
	}

	/**
	 * Update an existing grant
	 * @param grant The grant with updated values
	 * @return The updated grant
	 * @see <a href="https://developer.nylas.com/docs/api/uas#patch/connect/grants/provider">Update Grant</a>
	 */
	public Grant update(Grant grant) throws IOException, RequestFailedException {
		if (!grant.hasId()) {
			throw new UnsupportedOperationException("Cannot update grant without an id. Use create instead.");
		}
		grant.validate();
		return super.updatePatch(grant.getId(), grant.getWritableFields(false), null);
	}

	/**
	 * A helper method that either creates an grant if it doesn't exist or updates it
	 * @param grant The grant to create/update
	 * @return The newly created/updated grant
	 */
	public Grant save(Grant grant) throws IOException, RequestFailedException {
		return grant.hasId() ? update(grant) : create(grant);
	}

	/**
	 * Deletes an existing grant for a provider
	 * @see <a href="https://developer.nylas.com/docs/api/uas#delete/connect/grants/provider">Delete Grant</a>
	 * @return A message from the server after deletion
	 */
	public String delete(String id) throws IOException, RequestFailedException {
		return super.delete(id);
	}

	/**
	 * Trigger a grant sync on demand
	 * <br> {@code syncFrom} is set to null
	 * @see #onDemandSync(String, Long) 
	 */
	public Grant onDemandSync(String id) throws IOException, RequestFailedException {
		return onDemandSync(id, null);
	}

	/**
	 * Trigger a grant sync on demand
	 * @param id The grant ID to sync
	 * @param syncFrom Optionally specify an epoch timestamp when the sync starts from
	 * @return The grant after triggering the sync
	 */
	public Grant onDemandSync(String id, Long syncFrom) throws IOException, RequestFailedException {
		HttpUrl.Builder url = super.getCollectionUrl();
		if(syncFrom != null) {
			url.addQueryParameter("sync_from", String.valueOf(syncFrom));
		}

		return client.executeGet(authUser, url, modelClass, authMethod);
	}
}
