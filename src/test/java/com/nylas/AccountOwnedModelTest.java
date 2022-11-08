package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountOwnedModelTest {
    private AccountOwnedModelImpl accountOwnedModel;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        accountOwnedModel = new AccountOwnedModelImpl();
        setField("account_id", "123", accountOwnedModel, true);
    }

    @Test
    public void testGetAccountID() {
        assertEquals(accountOwnedModel.getAccountId(), "123");
    }

    private void setField(String fieldName, Object fieldValue, Object o, boolean parent) throws NoSuchFieldException, IllegalAccessException {

        Field codeField = null;

        if(parent) {
            codeField = o.getClass().getSuperclass().getDeclaredField(fieldName);
        } else {
            codeField = o.getClass().getDeclaredField(fieldName);
        }

        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }

    private class AccountOwnedModelImpl extends AccountOwnedModel {
    }
}
