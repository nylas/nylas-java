package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountOwnedModelTest {
    private AccountOwnedModelImpl accountOwnedModel;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        accountOwnedModel = new AccountOwnedModelImpl();
        FieldReflectionUtils.setField("account_id", "123", accountOwnedModel, true);
    }

    @Test
    public void testGetAccountID() {
        assertEquals(accountOwnedModel.getAccountId(), "123");
    }

    private class AccountOwnedModelImpl extends AccountOwnedModel {
    }
}
