package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
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
        setField("billing_state", "paid", account); // can be: cancelled, deleted
        setField("email", "ric@nylas.com", account);
        setField("provider", "google", account);
        setField("sync_state", "update", account);
        setField("authentication_type", "code", account);
        setField("trial", false, account);
        setField("metadata", metadata, account);
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

    private void setField(String fieldName, Object fieldValue, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field codeField = o.getClass().getDeclaredField(fieldName);
        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }
}
