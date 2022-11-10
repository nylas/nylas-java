package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountDetailTest {

    private AccountDetail accountDetail;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        accountDetail = new AccountDetail();
        FieldSetter.setField("id", "123", accountDetail);
        FieldSetter.setField("name", "Marty McFly", accountDetail);
        FieldSetter.setField("email_address", "marty@delorian.com", accountDetail);
        FieldSetter.setField("provider", "google", accountDetail);
        FieldSetter.setField("organization_unit", "ops", accountDetail);
        FieldSetter.setField("sync_state", "update", accountDetail);
        FieldSetter.setField("linked_at", 12039L, accountDetail);
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
}
