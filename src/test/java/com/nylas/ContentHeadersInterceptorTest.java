package com.nylas;

import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ContentHeadersInterceptorTest {
    OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    @Test
    public void testIntercept() throws IOException {
        OkHttpClient client = httpClientBuilder
                .addInterceptor(new ContentHeadersInterceptor())
                .build();

        HttpUrl.Builder url = new HttpUrl.Builder();
            url.scheme("https")
                    .host("api.nylas.com");


        Request request = new Request.Builder()
                .url(url.build())
                .build();

        Response response = client.newCall(request).execute();

        assertNotNull(response.request().header("Content-Type"));
        assertNotNull(response.request().header("Accept"));

        assertEquals(response.request().header("Content-Type"), "application/json");
        assertNotNull(response.request().header("Accept"), "application/json");
    }

    @Test
    public void testIntercept_withHeaders() throws IOException {
        OkHttpClient client = httpClientBuilder
                .addInterceptor(new ContentHeadersInterceptor())
                .build();

        HttpUrl.Builder url = new HttpUrl.Builder();
        url.scheme("https")
                .host("api.nylas.com")
                .addPathSegment("/contacts/picture");


        Request request = new Request.Builder()
                .url(url.build())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        assertNotNull(response.request().header("Content-Type"));
        assertNotNull(response.request().header("Accept"));

        assertEquals(response.request().header("Content-Type"), "application/json");
        assertNotNull(response.request().header("Accept"), "application/json");
    }
}
