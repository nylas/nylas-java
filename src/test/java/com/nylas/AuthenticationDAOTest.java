package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;


public class AuthenticationDAOTest {

    private NylasClient nylasClient;

    private HttpUrl.Builder baseUrl;

    @BeforeEach
    private void init() {
        nylasClient = mock(NylasClient.class);
        baseUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("test.nylas.com");
    }

    @Test
    public void testAuthenticationDAO() {
        AuthenticationDAO<TestAuthRest> grantAuthenticationDAO = new TestAuthEntity(
                nylasClient,
                TestAuthRest.class,
                "test/auth",
                "test-user",
                baseUrl
        );

        HttpUrl.Builder collectionUrl = grantAuthenticationDAO.getCollectionUrl();

        assertNotNull(grantAuthenticationDAO);
        assertEquals(grantAuthenticationDAO.authUser, "test-user");
        assertEquals(collectionUrl.build().toString(), "https://test.nylas.com/test/auth");
    }
}
