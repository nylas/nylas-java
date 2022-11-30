package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadQueryTest {
    private ThreadQuery threadQuery;

    @BeforeEach
    public void init() {
        threadQuery = new ThreadQuery();
        threadQuery.subject("Hello World!");
        threadQuery.anyEmail("someone@nylas.com");
        threadQuery.to("jdoe@gmail.com");
        threadQuery.from("janet@gmail.com");
        threadQuery.cc("boss@gmail.com");
        threadQuery.bcc("someone@gmail.com");
        threadQuery.in("sent");
        threadQuery.unread(true);
        threadQuery.starred(true);
        threadQuery.filename("name.jpg");
        threadQuery.lastMessageBefore(Instant.ofEpochSecond(1669735183L));
        threadQuery.lastMessageAfter(Instant.ofEpochSecond(1669735183L));
        threadQuery.startedBefore(Instant.ofEpochSecond(1669735183L));
        threadQuery.startedAfter(Instant.ofEpochSecond(1669735183L));
    }

    @Test
    public void testParametrers() {
        HttpUrl.Builder url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com")
                .addPathSegment("threads");

        threadQuery.addParameters(url);

        assertEquals(url.build().toString(), "https://api.nylas.com/threads?subject=Hello%20World%21&any_email=someone%40nylas.com&to=jdoe%40gmail.com&from=janet%40gmail.com&cc=boss%40gmail.com&bcc=someone%40gmail.com&in=sent&unread=true&starred=true&filename=name.jpg&last_message_before=1669735183&last_message_after=1669735183&started_before=1669735183&started_after=1669735183");
    }

    @Test
    public void testParametrers_nullAnyEmail() {
        HttpUrl.Builder url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com")
                .addPathSegment("threads");

        threadQuery.anyEmail(Collections.emptyList());
        threadQuery.addParameters(url);

        assertEquals(url.build().toString(), "https://api.nylas.com/threads?subject=Hello%20World%21&to=jdoe%40gmail.com&from=janet%40gmail.com&cc=boss%40gmail.com&bcc=someone%40gmail.com&in=sent&unread=true&starred=true&filename=name.jpg&last_message_before=1669735183&last_message_after=1669735183&started_before=1669735183&started_after=1669735183");
    }
}
