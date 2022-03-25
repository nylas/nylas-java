package com.nylas;

import okhttp3.HttpUrl;

abstract class UASDAO<M extends RestfulModel> extends RestfulDAO<M> {

	private final HttpUrl collectionUrl;

	UASDAO(NylasClient client, Class<M> modelClass, String endpointPath, String authUser, HttpUrl.Builder baseUrl) {
		super(client, modelClass, endpointPath, authUser);
		this.authMethod = NylasClient.AuthMethod.BASIC_WITH_CREDENTIALS;
		this.collectionUrl = baseUrl.addPathSegments(endpointPath).build();
	}

	protected HttpUrl.Builder getCollectionUrl() {
		return collectionUrl.newBuilder();
	}
}
