package com.nylas;

import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccessTokenTest {
    public static String TEST_ACCESS_TOKEN = "d964023b-4c66-42ed-abd4-8bd139087136";
    public static String TEST_ACCOUNT_ID = "ls0d9v8adfvbjasdofb90dvlsdv";
    public static String TEST_EMAIL_ADDRESS = "test@nylas.com";
    public static String TEST_PROVIDER = "google";

    private NylasClient nylasClient;

    @BeforeEach
    private void init() {
        nylasClient = mock(NylasClient.class);
    }

    @Test
    public void testAccessTokenFields() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        RequestBody body = RequestBody.create(JsonHelper.jsonType(), "{code: asdf1234}");
        Request request = new Request.Builder().url("https://test.nylas.com/auth/access_token").post(body).build();

        AccessToken accessToken = new AccessToken();
        FieldSetter.setField("access_token", TEST_ACCESS_TOKEN, accessToken);
        FieldSetter.setField("account_id", TEST_ACCOUNT_ID, accessToken);
        FieldSetter.setField("email_address", TEST_EMAIL_ADDRESS, accessToken);
        FieldSetter.setField("provider", TEST_PROVIDER, accessToken);

        when(nylasClient.executeRequest(request, AccessToken.class)).thenReturn(accessToken);
        AccessToken result = nylasClient.executeRequest(request, AccessToken.class);

        String expectedToString = "AccessToken [access_token=" + TEST_ACCESS_TOKEN + ", account_id=" + TEST_ACCOUNT_ID + ", email_address="
                + TEST_EMAIL_ADDRESS + ", provider=" + TEST_PROVIDER + "]";

        assertEquals(result.getAccessToken(), TEST_ACCESS_TOKEN);
        assertEquals(result.getAccountId(), TEST_ACCOUNT_ID);
        assertEquals(result.getEmailAddress(), TEST_EMAIL_ADDRESS);
        assertEquals(result.getProvider(), TEST_PROVIDER);
        assertEquals(result.toString(), expectedToString);
    }
}
