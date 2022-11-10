package com.nylas;

import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

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

        FieldSetter.setField("code", "asdf1234", authorizationCode);

        when(nylasClient.executeRequest(request, AuthorizationCode.class)).thenReturn(authorizationCode);
        AuthorizationCode result = nylasClient.executeRequest(request, AuthorizationCode.class);

        assertEquals(result.getCode(), "asdf1234");
    }
}
