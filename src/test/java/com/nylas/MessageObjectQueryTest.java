package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageObjectQueryTest {
    private MessageObjectQuery messageObjectQuery;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        messageObjectQuery = new DefaultMessageObjectQuery();
        messageObjectQuery.subject("This is a message");
        messageObjectQuery.anyEmail("someone@nylas.com");
        messageObjectQuery.to("jdoe@gmail.com");
        messageObjectQuery.cc("jdoe2@gmail.com");
        messageObjectQuery.bcc("jdoe3@gmail.com");
        messageObjectQuery.in("trash");
        messageObjectQuery.unread(true);
        messageObjectQuery.starred(true);
        messageObjectQuery.threadId("lskdnmv93");
        messageObjectQuery.hasAttachment(true);
        messageObjectQuery.filename("iloveyou.png");
    }

    @Test
    public void testBuildParameters() {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com")
                .addPathSegment("messages");

        messageObjectQuery.addParameters(builder);

        assertEquals(builder.build().toString(), "https://api.nylas.com/messages?subject=This%20is%20a%20message&any_email=someone%40nylas.com&to=jdoe%40gmail.com&cc=jdoe2%40gmail.com&bcc=jdoe3%40gmail.com&in=trash&unread=true&starred=true&thread_id=lskdnmv93&has_attachment=true&filename=iloveyou.png");
    }

    @Test
    public void testBuildParameters_anyEmailNull() throws NoSuchFieldException, IllegalAccessException {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com")
                .addPathSegment("messages");

        messageObjectQuery.anyEmail(Collections.emptyList());
        messageObjectQuery.addParameters(builder);

        assertEquals(builder.build().toString(), "https://api.nylas.com/messages?subject=This%20is%20a%20message&to=jdoe%40gmail.com&cc=jdoe2%40gmail.com&bcc=jdoe3%40gmail.com&in=trash&unread=true&starred=true&thread_id=lskdnmv93&has_attachment=true&filename=iloveyou.png");
    }
}

class DefaultMessageObjectQuery extends MessageObjectQuery {

}
