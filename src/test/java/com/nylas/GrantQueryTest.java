package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GrantQueryTest {
    private GrantQuery grantQuery;

    @BeforeEach
    public void init() {
        grantQuery = new GrantQuery();
    }

    @Test
    public void testOffset() {
        grantQuery.offset(10);
        int offset = grantQuery.getEffectiveOffset();

        assertEquals(offset, 10);
    }

    @Test
    public void testLimit() {
        grantQuery.limit(10);
        int limit = grantQuery.getEffectiveLimit();

        assertEquals(limit, 10);
    }

    @Test
    public void testParameters_noLimit_noOffset() {
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https")
                .host("api.nylas.com");

        grantQuery.addParameters(builder);

        String url = builder.build().toString();

        assertEquals(url, "https://api.nylas.com/");
    }

    @Test
    public void testParameters_Limit_Offset() {
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https")
                .addPathSegment("grant")
                .host("api.nylas.com");

        grantQuery.limit(10);
        grantQuery.offset(10);
        grantQuery.addParameters(builder);

        String url = builder.build().toString();

        assertEquals(url, "https://api.nylas.com/grant?offset=10&limit=10");
    }

    @Test
    public void testCopyAtNewOffsetAndLimit() {
        grantQuery.limit(10);
        grantQuery.offset(10);

        GrantQuery newGrant = grantQuery.copyAtNewOffsetLimit(20, 20);

        assertEquals(newGrant.getEffectiveLimit(), 20);
        assertEquals(newGrant.getEffectiveOffset(), 20);
    }
}
