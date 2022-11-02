package com.nylas;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

import static com.nylas.NylasClient.HttpHeaders.ACCEPT;
import static com.nylas.NylasClient.HttpHeaders.CONTENT_TYPE;
import static com.nylas.NylasClient.MediaType.APPLICATION_JSON;

public class ContentHeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        final String path = request.url().encodedPath();
        final String contentHeader = request.header(CONTENT_TYPE.getName());

        if(contentHeader == null && !isDownloadablePath(path)) {
            final Request enhancedRequest = request
                    .newBuilder()
                    .header(CONTENT_TYPE.getName(), APPLICATION_JSON.getName())
                    .header(ACCEPT.getName(), APPLICATION_JSON.getName())
                    .build();

            return chain.proceed(enhancedRequest);
        }

        return chain.proceed(request);
    }

    private boolean isDownloadablePath(String path) {
        return path.equals("%2Fcontacts%2Fpicture") ||
                path.equals("%2Ffiles%2Fdownload") ||
                path.equals("%2Fdelta%2Fstreaming");
    }
}
