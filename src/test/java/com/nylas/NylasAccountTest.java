package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.nylas.AccessTokenTest.TEST_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NylasAccountTest {
    private NylasClient nylasClient;

    @BeforeEach
    private void init() {
        nylasClient = mock(NylasClient.class);
    }

    @Test
    public void testNylasAccountBootstraping() {
        final NylasAccount nylasAccount = new NylasAccount(nylasClient, TEST_ACCESS_TOKEN);

        assertEquals(nylasAccount.getAccessToken(), TEST_ACCESS_TOKEN);
        assertEquals(nylasAccount.getClient(), nylasClient);
        assertNotNull(nylasAccount.threads());
        assertNotNull(nylasAccount.messages());
        assertNotNull(nylasAccount.folders());
        assertNotNull(nylasAccount.labels());
        assertNotNull(nylasAccount.drafts());
        assertNotNull(nylasAccount.outbox());
        assertNotNull(nylasAccount.calendars());
        assertNotNull(nylasAccount.files());
        assertNotNull(nylasAccount.contacts());
        assertNotNull(nylasAccount.contactGroups());
        assertNotNull(nylasAccount.deltas());
        assertNotNull(nylasAccount.events());
        assertNotNull(nylasAccount.jobStatuses());
        assertNotNull(nylasAccount.roomResources());
        assertNotNull(nylasAccount.neural());
        assertNotNull(nylasAccount.schedulers());
    }

    @Test
    public void testFetchAccountByAccessToken() throws RequestFailedException, IOException {
        final NylasAccount nylasAccount = new NylasAccount(nylasClient, TEST_ACCESS_TOKEN);
        final AccountDetail expectedAccountDetail = new AccountDetail();

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(anyString(), any(), any())).thenReturn(expectedAccountDetail);

        AccountDetail actualAccountDetail = nylasAccount.fetchAccountByAccessToken();

        verify(nylasClient).executeGet(anyString(), any(), any());
        assertNotNull(actualAccountDetail);
    }

    @Test
    public void testRevokeAccessToken() throws RequestFailedException, IOException {
        final NylasAccount nylasAccount = new NylasAccount(nylasClient, TEST_ACCESS_TOKEN);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        nylasAccount.revokeAccessToken();

        verify(nylasClient).executePost(anyString(), any(), any(), any());
    }
}
