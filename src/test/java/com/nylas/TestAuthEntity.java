package com.nylas;

import okhttp3.HttpUrl;

public class TestAuthEntity extends AuthenticationDAO<TestAuthRest> {

    TestAuthEntity(NylasClient client, Class<TestAuthRest> modelClass, String endpointPath, String authUser, HttpUrl.Builder baseUrl) {
        super(client, modelClass, endpointPath, authUser, baseUrl);
    }
}
