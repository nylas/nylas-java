package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageQueryTest {

    @Test
    public void testAddParameters() {
        HttpUrl.Builder url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com")
                .addPathSegment("messages");

        MessageQuery messageQuery = new MessageQuery();
        messageQuery.from("jdoe@gmail.com")
                .receivedAfter(Instant.ofEpochSecond(1669221612L))
                .receivedBefore(Instant.ofEpochSecond(1669221512L));

        messageQuery.addParameters(url);

        assertEquals(url.build().toString(), "https://api.nylas.com/messages?from=jdoe%40gmail.com&received_before=1669221512&received_after=1669221612");
    }
}
