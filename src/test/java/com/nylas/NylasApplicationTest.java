package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.nylas.NylasClientTest.TEST_CLIENT_ID;
import static com.nylas.NylasClientTest.TEST_CLIENT_SECRET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NylasApplicationTest {
    private NylasClient nylasClient;

    private NylasApplication nylasApplication;

    @BeforeEach
    private void init() {
        nylasClient = mock(NylasClient.class);
        nylasApplication = new NylasApplication(nylasClient, TEST_CLIENT_ID, TEST_CLIENT_SECRET);
    }

    @Test
    public void testGetClient() {
        NylasClient client = nylasApplication.getClient();

        assertNotNull(client);
    }

    @Test
    public void testGetClientID() {
        String clientId = nylasApplication.getClientId();

        assertEquals(clientId, TEST_CLIENT_ID);
    }

    @Test
    public void testGetClientSecret() {
        String clientSecret = nylasApplication.getClientSecret();

        assertEquals(clientSecret, TEST_CLIENT_SECRET);
    }

    @Test
    public void testHostedAuthentication() {
        HostedAuthentication hostedAuthentication = nylasApplication.hostedAuthentication();

        assertNotNull(hostedAuthentication);
    }

    @Test
    public void testNativeAuthentication() {
        NativeAuthentication nativeAuthentication = nylasApplication.nativeAuthentication();

        assertNotNull(nativeAuthentication);
    }

    @Test
    public void testGetAccounts() {
        Accounts accounts = nylasApplication.accounts();

        assertNotNull(accounts);
        assertEquals(accounts.authMethod, NylasClient.AuthMethod.BASIC);
    }

    @Test
    public void testGetWebhooks (){
        Webhooks webhooks = nylasApplication.webhooks();

        assertNotNull(webhooks);
        assertEquals(webhooks.authMethod, NylasClient.AuthMethod.BASIC);
        assertEquals(webhooks.collectionPath, "a/kmsdv7809834n98fdvc/webhooks");
    }

    @Test
    public void testGetComponents() {
        Components components = nylasApplication.components();

        assertNotNull(components);
        assertEquals(components.authMethod, NylasClient.AuthMethod.BASIC);
        assertEquals(components.collectionPath, "component/kmsdv7809834n98fdvc");
    }

    @Test
    public void testGetAuthentication() {
        Authentication authentication = nylasApplication.authentication();

        assertNotNull(authentication);
        assertEquals(authentication.appName, "beta");
        assertEquals(authentication.region, "us");
    }

    @Test
    public void testGetApplicationDetail() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        ApplicationDetail expected = new ApplicationDetail();
        List<String> redirectUris = new ArrayList<>();
        redirectUris.add("https://app.nylas.com/auth");

        setField("application_name", "nylas-app", expected);
        setField("icon_url", "https://placehold.it/3x3", expected);
        setField("redirect_uris", redirectUris, expected);

        when(nylasClient.executeGet(anyString(), any(), any())).thenReturn(expected);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        ApplicationDetail actual = nylasApplication.getApplicationDetail();

        assertNotNull(actual);
        assertEquals(actual.getIconUrl(), expected.getIconUrl());
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getRedirectUris(), expected.getRedirectUris());
    }

    @Test
    public void testSetApplicationName() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        ApplicationDetail expected = new ApplicationDetail();
        List<String> redirectUris = new ArrayList<>();
        redirectUris.add("https://app.nylas.com/auth");

        setField("application_name", "mew-nylas-app", expected);
        setField("icon_url", "https://placehold.it/3x3", expected);
        setField("redirect_uris", redirectUris, expected);

        when(nylasClient.executePut(anyString(), any(), any(), any())).thenReturn(expected);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        ApplicationDetail actual = nylasApplication.setName("new-nylas-app");

        assertEquals(actual.getName(), expected.getName());
    }

    @Test
    public void testSetRedirectUris() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        ApplicationDetail expected = new ApplicationDetail();
        List<String> redirectUris = new ArrayList<>();
        redirectUris.add("https://app.nylas.com/auth");

        setField("application_name", "mew-nylas-app", expected);
        setField("icon_url", "https://placehold.it/3x3", expected);
        setField("redirect_uris", redirectUris, expected);

        when(nylasClient.executePut(anyString(), any(), any(), any())).thenReturn(expected);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        ApplicationDetail actual = nylasApplication.setRedirectUris(redirectUris);

        assertEquals(actual.getRedirectUris(), expected.getRedirectUris());
    }

    @Test
    public void testAddRedirectUri() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        ApplicationDetail existing = new ApplicationDetail();
        List<String> redirectUris = new ArrayList<>();
        redirectUris.add("https://app.nylas.com/auth");

        setField("application_name", "mew-nylas-app", existing);
        setField("icon_url", "https://placehold.it/3x3", existing);
        setField("redirect_uris", redirectUris, existing);


        ApplicationDetail updated = new ApplicationDetail();
        List<String> updatedRedirectUris = new ArrayList<>();
        updatedRedirectUris.add("https://app.nylas.com/auth");
        updatedRedirectUris.add("https://app2.nylas.com/auth");

        setField("application_name", "mew-nylas-app", updated);
        setField("icon_url", "https://placehold.it/3x3", updated);
        setField("redirect_uris", updatedRedirectUris, updated);

        // gets exsisting
        when(nylasClient.executeGet(anyString(), any(), any())).thenReturn(existing);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        when(nylasClient.executePut(anyString(), any(), any(), any())).thenReturn(updated);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        ApplicationDetail actual = nylasApplication.addRedirectUri("https://app2.nylas.com/auth");

        assertEquals(actual.getRedirectUris().size(), 2);
    }

    @Test
    public void testAddRedirectUri_idempotency() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        ApplicationDetail existing = new ApplicationDetail();
        List<String> redirectUris = new ArrayList<>();
        redirectUris.add("https://app.nylas.com/auth");

        setField("application_name", "mew-nylas-app", existing);
        setField("icon_url", "https://placehold.it/3x3", existing);
        setField("redirect_uris", redirectUris, existing);


        // gets exsisting
        when(nylasClient.executeGet(anyString(), any(), any())).thenReturn(existing);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        // we add the same url, should return the same app detail
        ApplicationDetail actual = nylasApplication.addRedirectUri("https://app.nylas.com/auth");

        assertEquals(actual.getRedirectUris().size(), 1);
    }

    @Test
    public void testRemoveRedirecrUris() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        ApplicationDetail existing = new ApplicationDetail();
        List<String> redirectUris = new ArrayList<>();
        redirectUris.add("https://app.nylas.com/auth");
        redirectUris.add("https://app2.nylas.com/auth");

        setField("application_name", "mew-nylas-app", existing);
        setField("icon_url", "https://placehold.it/3x3", existing);
        setField("redirect_uris", redirectUris, existing);


        ApplicationDetail updated = new ApplicationDetail();
        List<String> updatedRedirectUris = new ArrayList<>();
        updatedRedirectUris.add("https://app.nylas.com/auth");

        setField("application_name", "mew-nylas-app", updated);
        setField("icon_url", "https://placehold.it/3x3", updated);
        setField("redirect_uris", updatedRedirectUris, updated);

        // gets exsisting
        when(nylasClient.executeGet(anyString(), any(), any())).thenReturn(existing);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        when(nylasClient.executePut(anyString(), any(), any(), any())).thenReturn(updated);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        ApplicationDetail actual = nylasApplication.removeRedirectUri("https://app2.nylas.com/auth");

        assertEquals(actual.getRedirectUris().size(), 1);
    }

    @Test
    public void testRemoveRedirectUrls_removeInexistent() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        ApplicationDetail existing = new ApplicationDetail();
        List<String> redirectUris = new ArrayList<>();
        redirectUris.add("https://app.nylas.com/auth");

        setField("application_name", "mew-nylas-app", existing);
        setField("icon_url", "https://placehold.it/3x3", existing);
        setField("redirect_uris", redirectUris, existing);


        // gets exsisting urls
        when(nylasClient.executeGet(anyString(), any(), any())).thenReturn(existing);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        // tries to remove an url that doesn't exist, returns the same app detail
        ApplicationDetail actual = nylasApplication.removeRedirectUri("https://google.com");

        assertEquals(actual.getRedirectUris().size(), 1);
    }

    @Test
    public void testFetchIpAddressWhiteList() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        IPAddressWhitelist expected = new IPAddressWhitelist();
        List<String> ipAddresses = new ArrayList<>();
        ipAddresses.add("192.169.11.2");

        setField("ip_addresses", ipAddresses, expected);

        when(nylasClient.executeGet(anyString(), any(), any())).thenReturn(expected);
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        IPAddressWhitelist actual = nylasApplication.fetchIpAddressWhitelist();

        assertEquals(expected.getIpAddresses(), actual.getIpAddresses());
    }

    private void setField(String fieldName, Object fieldValue, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field codeField = o.getClass().getDeclaredField(fieldName);
        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }
}
