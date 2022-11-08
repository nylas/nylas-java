package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountDetailTest {

    private AccountDetail accountDetail;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        accountDetail = new AccountDetail();
        setField("id", "123", accountDetail);
        setField("name", "Marty McFly", accountDetail);
        setField("email_address", "marty@delorian.com", accountDetail);
        setField("provider", "google", accountDetail);
        setField("organization_unit", "ops", accountDetail);
        setField("sync_state", "update", accountDetail);
        setField("linked_at", 12039L, accountDetail);
    }

    @Test
    public void testGetters() {
        assertEquals(accountDetail.getEmailAddress(), "marty@delorian.com");
        assertEquals(accountDetail.getId(), "123");
        assertEquals(accountDetail.getName(), "Marty McFly");
        assertEquals(accountDetail.getProvider(), "google");
        assertEquals(accountDetail.getOrganizationUnit(), "ops");
        assertEquals(accountDetail.getSyncState(), "update");
        assertEquals(accountDetail.getLinkedAt(), Instant.ofEpochSecond(12039L));
        assertEquals(accountDetail.toString(), "AccountDetail [id=123, name=Marty McFly, email_address=marty@delorian.com, provider=google, organization_unit=ops, sync_state=update, linked_at=1970-01-01T03:20:39Z]");
    }


    private void setField(String fieldName, Object fieldValue, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field codeField = o.getClass().getDeclaredField(fieldName);
        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }
}
