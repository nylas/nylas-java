package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {

    private Account account;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("label_count", "3");

        account = new Account();
        FieldReflectionUtils.setField("billing_state", "paid", account); // can be: cancelled, deleted
        FieldReflectionUtils.setField("email", "ric@nylas.com", account);
        FieldReflectionUtils.setField("provider", "google", account);
        FieldReflectionUtils.setField("sync_state", "update", account);
        FieldReflectionUtils.setField("authentication_type", "code", account);
        FieldReflectionUtils.setField("trial", false, account);
        FieldReflectionUtils.setField("metadata", metadata, account);
    }

    @Test
    public void testGetters() {
        assertEquals(account.getBillingState(), "paid");
        assertEquals(account.getEmail(), "ric@nylas.com");
        assertEquals(account.getProvider(), "google");
        assertEquals(account.getSyncState(), "update");
        assertEquals(account.getAuthenticationType(), "code");
        assertEquals(account.getTrial(), false);
        assertEquals(account.getMetadata().get("label_count"), "3");
        assertEquals(account.toString(), "Account [id='null', billing_state='paid', email='ric@nylas.com', provider='google', sync_state='update', authentication_type='code', trial=false, metadata={label_count=3}]");
    }
}
