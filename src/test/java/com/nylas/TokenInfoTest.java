package com.nylas;

import jdk.nashorn.internal.parser.Token;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TokenInfoTest {

    @Test
    public void testGetters() throws NoSuchFieldException, IllegalAccessException {
        TokenInfo tokenInfo = new TokenInfo();
        setField("created_at", 1664983332L, tokenInfo);
        setField("updated_at", 1664983333L, tokenInfo);
        setField("scopes", "email,calendar", tokenInfo);
        setField("state", "valid", tokenInfo);

        assertNotNull(tokenInfo);
        assertEquals(tokenInfo.getCreatedAt().getEpochSecond(), 1664983332L);
        assertEquals(tokenInfo.getUpdatedAt().getEpochSecond(), 1664983333L);
        assertEquals(tokenInfo.getScopes(), "email,calendar");
        assertEquals(tokenInfo.getState(), "valid");
        assertEquals(tokenInfo.toString(), "TokenInfo [created_at=2022-10-05T15:22:12Z, updated_at=2022-10-05T15:22:13Z, scopes=email,calendar, state=valid]");
    }

    private void setField(String fieldName, Object fieldValue, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field codeField = o.getClass().getDeclaredField(fieldName);
        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }
}
