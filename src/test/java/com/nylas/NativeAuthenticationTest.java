package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NativeAuthenticationTest {
    private NylasApplication nylasApplication;
    private NylasClient nylasClient;

    @BeforeEach
    public void init() {
        nylasApplication = mock(NylasApplication.class);
        nylasClient = mock(NylasClient.class);
    }

    @Test
    public void testProviderEnum() {
        assertEquals("gmail", NativeAuthentication.Provider.GMAIL.getName());
        assertEquals("GMAIL", NativeAuthentication.Provider.GMAIL.name());

        assertEquals(NativeAuthentication.Provider.GMAIL, NativeAuthentication.Provider.getProviderByName("gmail"));
    }

    @Test
    public void testProviderByNameReturnsNull() {
        assertNull(NativeAuthentication.Provider.getProviderByName("zoho-mail"));
    }

    @Test
    public void testConstructor() {
        NativeAuthentication nativeAuthentication = new NativeAuthentication(nylasApplication);

        assertNotNull(nativeAuthentication);
    }

    @Test
    public void testAuthBuilder() {
        NativeAuthentication nativeAuthentication = new NativeAuthentication(nylasApplication);
        NativeAuthentication.AuthRequestBuilder authRequestBuilder = nativeAuthentication.authRequest();

        assertNotNull(authRequestBuilder);
    }

    @Test
    public void testAuthRequestBuilder() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        AuthorizationCode code = new AuthorizationCode();
        setField("code", "123", code);

        when(nylasApplication.getClient()).thenReturn(nylasClient);
        when(nylasApplication.getClientId()).thenReturn("123456");
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder().scheme("https").host("api.nylas.com"));
        when(nylasClient.executeRequest(any(), any())).thenReturn(code);

        ProviderSettings providerSettings = new ProviderSettings("gmail");

        NativeAuthentication.AuthRequestBuilder authRequestBuilder = new NativeAuthentication.AuthRequestBuilder(nylasApplication);

        authRequestBuilder.name("Jhon")
                .emailAddress("jhon@google.com")
                .providerSettings(providerSettings)
                .scopes(Scope.EMAIL, Scope.CALENDAR)
                .scopes(Arrays.asList(Scope.EMAIL_METADATA))
                .scopes("EMAIL,CALENDAR,EMAIL_METADATA")
                .reauthAccountId("78901");


        String result = authRequestBuilder.execute();

        assertEquals(result, "123");
    }

    @Test
    public void testFetchToken() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        AccessToken accessToken = new AccessToken();
        setField("access_token", "lskdjnv", accessToken);
        setField("account_id", "wdoivj", accessToken);
        setField("email_address", "ric@nylas.com", accessToken);
        setField("provider", "google", accessToken);

        when(nylasApplication.getClient()).thenReturn(nylasClient);
        when(nylasApplication.getClientId()).thenReturn("abc");
        when(nylasApplication.getClientSecret()).thenReturn("123");
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder().scheme("https").host("api.nylas.com"));
        when(nylasClient.executePost(any(), any(), any(), any())).thenReturn(accessToken);

        NativeAuthentication nativeAuthentication = new NativeAuthentication(nylasApplication);
        AccessToken result = nativeAuthentication.fetchToken("abcdlasd");

        assertEquals(accessToken.getAccessToken(), "lskdjnv");
    }

    @Test
    public void testDetectProvider() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        NativeAuthentication.DetectedProvider expected = new NativeAuthentication.DetectedProvider();
        setField("auth_name", "gmail", expected);
        setField("email_address", "ric@nylas.com", expected);
        setField("provider_name", "google", expected);
        setField("detected", true, expected);
        setField("is_imap", false, expected);


        when(nylasApplication.getClient()).thenReturn(nylasClient);
        when(nylasApplication.getClientId()).thenReturn("abc");
        when(nylasApplication.getClientSecret()).thenReturn("123");
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder().scheme("https").host("api.nylas.com"));
        when(nylasClient.executePost(any(), any(), any(), any())).thenReturn(expected);

        NativeAuthentication nativeAuthentication = new NativeAuthentication(nylasApplication);

        NativeAuthentication.DetectedProvider provider = nativeAuthentication.detectProvider("ric@nylas.com");

        assertEquals(expected.getAuthName(), provider.getAuthName());
        assertEquals(expected.getProviderName(), provider.getProviderName());
        assertEquals(expected.isDetected(), provider.isDetected());
        assertEquals(expected.getEmailAddress(), provider.getEmailAddress());
        assertEquals(expected.isImap(), provider.isImap());
    }

    @Test
    public void testGetDetectedProviderSettings() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        NativeAuthentication.DetectedProvider detectedProvider = new NativeAuthentication.DetectedProvider();
        setField("auth_name", "gmail", detectedProvider);
        setField("email_address", "ric@nylas.com", detectedProvider);
        setField("provider_name", "google", detectedProvider);
        setField("detected", true, detectedProvider);
        setField("is_imap", false, detectedProvider);


        when(nylasApplication.getClient()).thenReturn(nylasClient);
        when(nylasApplication.getClientId()).thenReturn("abc");
        when(nylasApplication.getClientSecret()).thenReturn("123");
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder().scheme("https").host("api.nylas.com"));
        when(nylasClient.executePost(any(), any(), any(), any())).thenReturn(detectedProvider);

        NativeAuthentication nativeAuthentication = new NativeAuthentication(nylasApplication);
        ProviderSettings providerSettings = nativeAuthentication.getDetectedProviderSettings("ric@nylas.com");

        assertEquals(providerSettings.toString(), "ProviderSettings [providerName=gmail, settings={}]");
    }


    private void setField(String fieldName, Object fieldValue, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field codeField = o.getClass().getDeclaredField(fieldName);
        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }
}
