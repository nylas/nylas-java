package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class AuthenticationTest {

    private Authentication authentication;

    private NylasClient nylasClient;
    private NylasApplication nylasApplication;

    @BeforeEach
    private void init() {
        nylasClient = mock(NylasClient.class);
        nylasApplication = mock(NylasApplication.class);
    }

    @Test
    public void testProvidersEnum() {
        authentication = new Authentication(nylasClient, nylasApplication);

        assertEquals("google", Authentication.Provider.GOOGLE.toString());
        assertEquals("microsoft", Authentication.Provider.MICROSOFT.toString());
        assertEquals("imap", Authentication.Provider.IMAP.toString());
        assertEquals("zoom", Authentication.Provider.ZOOM.toString());
    }

    @Test
    public void testAuthenticationBuilder() {
        authentication = new Authentication(nylasClient, nylasApplication);

        verify(nylasApplication).getClientId();
        verify(nylasApplication).getClientSecret();
    }

    @Test
    public void testAuthenticationSetsAppName() {
        authentication = new Authentication(nylasClient, nylasApplication);
        authentication.appName("test-app");

        assertEquals(authentication.appName, "test-app");
    }

    @Test
    public void testAuthenticationSetsRegion() {
        authentication = new Authentication(nylasClient, nylasApplication);
        authentication.region(Authentication.Region.US);

        assertEquals(authentication.region, "us");
    }

    @Test
    public void testAuthenticationIntegrations() {
        authentication = new Authentication(nylasClient, nylasApplication);
        Integrations integrations = authentication.integrations();

        assertNotNull(integrations);
        assertEquals(integrations.collectionPath, "connect/integrations");
    }

    @Test
    public void testAuthenticationGrants() {
        authentication = new Authentication(nylasClient, nylasApplication);
        Grants grant = authentication.grants();

        assertNotNull(grant);
        assertEquals(grant.collectionPath, "connect/grants");
    }

    @Test
    public void testIntegrationHostedAuth() {
        authentication = new Authentication(nylasClient, nylasApplication);
        IntegrationHostedAuthentication integrationHostedAuthentication = authentication.hostedAuthentication();

        assertNotNull(integrationHostedAuthentication);
    }
}
