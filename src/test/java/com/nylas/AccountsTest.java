package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountsTest {
    private NylasClient nylasClient;
    private NylasApplication nylasApplication;

    private Accounts accounts;
    private Account account;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        nylasClient = mock(NylasClient.class);
        nylasApplication = mock(NylasApplication.class);
        accounts = new Accounts(nylasClient, nylasApplication);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("label_count", "3");

        account = new Account();
        FieldSetter.setField("billing_state", "paid", account); // can be: cancelled, deleted
        FieldSetter.setField("email", "ric@nylas.com", account);
        FieldSetter.setField("provider", "google", account);
        FieldSetter.setField("sync_state", "update", account);
        FieldSetter.setField("authentication_type", "code", account);
        FieldSetter.setField("trial", false, account);
        FieldSetter.setField("metadata", metadata, account);
    }

    @Test
    public void testConstructor() {
        Accounts accounts = new Accounts(nylasClient, nylasApplication);

        assertNotNull(accounts);
    }

    @Test
    public void testList() throws RequestFailedException, IOException {
        RemoteCollection<Account> list = accounts.list();

        assertNotNull(list);
    }

    @Test
    public void testGet() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(account);

        Account result = accounts.get("123");

        assertNotNull(result);
        assertEquals(result.getBillingState(), "paid");
        assertEquals(result.getEmail(), "ric@nylas.com");
        assertEquals(result.getProvider(), "google");
        assertEquals(result.getSyncState(), "update");
        assertEquals(result.getAuthenticationType(), "code");
        assertEquals(result.getTrial(), false);
        assertEquals(result.getMetadata().get("label_count"), "3");
        assertEquals(result.toString(), "Account [id='null', billing_state='paid', email='ric@nylas.com', provider='google', sync_state='update', authentication_type='code', trial=false, metadata={label_count=3}]");
    }

    @Test
    public void testDelete() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeDelete(any(), any(), any(), any())).thenReturn(null);

        String deleted = accounts.delete("123");

        assertNull(deleted);
    }

    @Test
    public void testDowngrade() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any(), any())).thenReturn(null);

        accounts.downgrade("123");
    }

    @Test
    public void testUpgrade() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any(), any())).thenReturn(null);

        accounts.upgrade("123");
    }

    @Test
    public void testRevokeAllAccessToken_keepAccessToken() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any(), any())).thenReturn(null);

        accounts.revokeAllTokensForAccount("123", "keep-123");
    }

    @Test
    public void testRevokeAllAccessToken_dontKeepAccessToken() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any(), any())).thenReturn(null);

        accounts.revokeAllTokensForAccount("123", null);
    }

    @Test
    public void testTokenInfo() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        TokenInfo expected = new TokenInfo();
        FieldSetter.setField("created_at", 1666637474L, expected);
        FieldSetter.setField("updated_at", 1666637480L, expected);
        FieldSetter.setField("scopes", "EMAIL,CALENDAR", expected);
        FieldSetter.setField("state", "valid", expected);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any())).thenReturn(expected);

        TokenInfo tokenInfo = accounts.tokenInfo("123", "laskdcvnlosdhn==");

        assertNotNull(tokenInfo);
        assertEquals(tokenInfo.toString(), "TokenInfo [created_at=2022-10-24T18:51:14Z, updated_at=2022-10-24T18:51:20Z, scopes=EMAIL,CALENDAR, state=valid]");
    }

    @Test
    public void testSetMetadata() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(account);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("label_count", "3");

        Account result = accounts.setMetadata("123", metadata);

        assertEquals(result.getMetadata().get("label_count"), "3");
    }

    @Test
    public void testAddMetadata() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(account);
        account.getMetadata().put("is_admin", "true");
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(account);

        Account result = accounts.addMetadata("123", "is_admin",  "true");

        assertEquals(result.getMetadata().size(), 2);
    }

    @Test
    public void testAddMetadata_nullMetadata() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        FieldSetter.setField("metadata", null, account);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("is_admin", "true");

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(account);

        Account updatedAccount = new Account();
        FieldSetter.setField("metadata", metadata, updatedAccount);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(updatedAccount);

        Account result = accounts.addMetadata("123", "is_admin",  "true");

        assertEquals(result.getMetadata().size(), 1);
    }

    @Test
    public void testRemoveMetadata() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(account);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(account);

        Boolean result = accounts.removeMetadata("123", "label_count");

        assertEquals(result, true);
    }

    @Test
    public void testRemoveMetadata_fails() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(account);
        FieldSetter.setField("metadata", null, account);

        Boolean result = accounts.removeMetadata("123", "label_count");

        assertEquals(result, false);
    }
}
