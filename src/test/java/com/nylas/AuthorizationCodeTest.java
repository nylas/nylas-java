package com.nylas;

import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorizationCodeTest {

    private NylasClient nylasClient;

    @BeforeEach
    private void init() {
        nylasClient = mock(NylasClient.class);
    }

    @Test
    public void testAuthorizationCodeDeserialization() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        RequestBody body = RequestBody.create(JsonHelper.jsonType(), "{token: abcdfg}");
        Request request = new Request.Builder().url("https://test.nylas.com/auth/code").post(body).build();
        AuthorizationCode authorizationCode = new AuthorizationCode();

        Field codeField = authorizationCode.getClass().getDeclaredField("code");
        codeField.setAccessible(true);
        codeField.set(authorizationCode, "asdf1234");

        when(nylasClient.executeRequest(request, AuthorizationCode.class)).thenReturn(authorizationCode);
        AuthorizationCode result = nylasClient.executeRequest(request, AuthorizationCode.class);

        assertEquals(authorizationCode.getCode(), "asdf1234");
    }
}
