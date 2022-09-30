package com.nylas;

import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Timeout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.nylas.AccessTokenTest.TEST_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class NylasClientTest {
    private NylasClient nylasClient;

    private MockNylas mockNylas;

    public static String TEST_CLIENT_ID = "kmsdv7809834n98fdvc";
    public static String TEST_CLIENT_SECRET = "kjlsdvnis8yv923nfo89";

    @BeforeEach
    private void init() {
        mockNylas = new MockNylas();
    }

    @AfterEach
    private void cleanup() {
        mockNylas.cleanup();
    }

    @Test
    public void testNylasClientDefaultConstructor() {
        nylasClient = new NylasClient();
        assertNotNull(nylasClient);
    }

    @Test
    public void testNylasClientConstructor_customHttpBuilder() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60,  TimeUnit.SECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .addNetworkInterceptor(new HttpLoggingInterceptor());

        nylasClient = new NylasClient(httpClientBuilder);

        assertNotNull(nylasClient);
    }

    @Test
    public void testGetsNewUriBuilder() {
        nylasClient = new NylasClient();

        HttpUrl.Builder builder = nylasClient.newUrlBuilder();

        assertNotNull(builder);
    }

    @Test
    public void testGetHttpClient() {
        nylasClient = new NylasClient();

        OkHttpClient client = nylasClient.getHttpClient();

        assertNotNull(client.interceptors());
        assertNotNull(client);
    }

    @Test
    public void testRetrieveApplication() {
        nylasClient = new NylasClient();

        NylasApplication application = nylasClient.application(TEST_CLIENT_ID, TEST_CLIENT_SECRET);

        assertNotNull(application);
    }

    @Test
    public void testRetrieveNylasAccount() {
        nylasClient = new NylasClient();

        NylasAccount account = nylasClient.account(TEST_ACCESS_TOKEN);

        assertNotNull(account);
    }

    @Test
    public void testExecuteGet() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                        .scheme("https")
                        .addPathSegment("test")
                        .addPathSegment("response")
                        .host("api.nylas.com")
                        .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        TestClientResponse response = nylasClient.executeGet("jhonny", url, TestClientResponse.class, NylasClient.AuthMethod.BASIC);
        assertEquals(response.getStatus(), "ok");
    }

    @Test
    public void testExecuteGet_noAuthMethod() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        TestClientResponse response = nylasClient.executeGet("jhonny", url, TestClientResponse.class);
        assertEquals(response.getStatus(), "ok");
    }

    @Test
    public void testExecuteGet_noAuthMethod_fails() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn("error");
        when(mockResponse.code()).thenReturn(400);

        Exception exception = assertThrows(RequestFailedException.class, () -> {
            nylasClient.executeGet("jhonny", url, TestClientResponse.class);
        });

        assertEquals(exception.getMessage(), "statusCode=400, type=null, message=null");
    }

    @Test
    public void testExecutePut() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        Map<String, Object> params = new HashMap<>();
        params.put("param1", "ok");

        TestClientResponse response = nylasClient.executePut("jhonny", url, params, TestClientResponse.class, NylasClient.AuthMethod.BEARER);
        assertEquals(response.getStatus(), "ok");
    }

    @Test
    public void testExecutePut_noAuthMethod() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        Map<String, Object> params = new HashMap<>();
        params.put("param1", "ok");

        TestClientResponse response = nylasClient.executePut("jhonny", url, params, TestClientResponse.class);
        assertEquals(response.getStatus(), "ok");
    }

    @Test
    public void testExecutePatch() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        Map<String, Object> params = new HashMap<>();
        params.put("param1", "ok");

        TestClientResponse response = nylasClient.executePatch("jhonny", url, params, TestClientResponse.class, NylasClient.AuthMethod.BASIC_WITH_CREDENTIALS);
        assertEquals(response.getStatus(), "ok");
    }

    @Test
    public void testExecutePost() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        Map<String, Object> params = new HashMap<>();
        params.put("param1", "ok");

        TestClientResponse response = nylasClient.executePost("jhonny", url, params, TestClientResponse.class, NylasClient.AuthMethod.BASIC_WITH_CREDENTIALS);
        assertEquals(response.getStatus(), "ok");
    }

    @Test
    public void testExecutePost_noAuthMethod() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn("{\"status\": \"ok\"}");

        Map<String, Object> params = new HashMap<>();
        params.put("param1", "ok");

        String response = nylasClient.executePost("jhonny", url, params, String.class);
        assertEquals(response, "{\"status\": \"ok\"}");
    }

    @Test
    public void testExecuteDelete() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        TestClientResponse response = nylasClient.executeDelete("jhonny", url, null, NylasClient.AuthMethod.BASIC_WITH_CREDENTIALS);
        assertNull(response);
    }

    @Test
    public void testExecuteDelete_noAuthMethod() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        TestClientResponse response = nylasClient.executeDelete("jhonny", url, TestClientResponse.class);
        assertEquals(response.getStatus(), "ok");
    }

    @Test
    public void testDownload() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // mockery
        okhttp3.HttpUrl.Builder url = mock(okhttp3.HttpUrl.Builder.class);
        OkHttpClient mockClient = mock(OkHttpClient.class);
        okhttp3.Response mockResponse = mock(okhttp3.Response.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);

        nylasClient = new NylasClient();

        Field clientField = nylasClient.getClass().getDeclaredField("httpClient");
        clientField.setAccessible(true);
        clientField.set(nylasClient, mockClient);

        when(url.build()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .addPathSegment("test")
                .addPathSegment("response")
                .host("api.nylas.com")
                .build());

        ReadableByteChannel channel = new Buffer().writeUtf8("{\"status\": \"ok\"}");
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));

        when(mockClient.newCall(ArgumentMatchers.any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.source()).thenReturn(source);

        ResponseBody response = nylasClient.download("jhonny", url);
        assertNotNull(response);
    }

    @Test
    public void testAddAuthHeader() {
        Request.Builder builder = new Request.Builder()
                .url("https://api.nylas.com");

        nylasClient = new NylasClient();

        nylasClient.addAuthHeader(builder, "some-auth-user");

        assertNotNull(builder.build().header("AUTHORIZATION"));

    }

    @Test
    public void testMediaType() {
        assertEquals(NylasClient.MediaType.MESSAGE_RFC822.getName(), "message/rfc822");
        assertEquals(NylasClient.MediaType.APPLICATION_JSON.getName(), "application/json");

        assertEquals(NylasClient.MediaType.MESSAGE_RFC822.name(), "MESSAGE_RFC822");
        assertEquals(NylasClient.MediaType.APPLICATION_JSON.name(), "APPLICATION_JSON");
    }

    @Test
    public void testBuilder() {
        NylasClient.Builder builder = new NylasClient.Builder();

        OkHttpClient nylasClient = builder
                .baseUrl("https://api.nylas.com")
                .httpClient()
                .build();

        assertNotNull(nylasClient);
    }
}
