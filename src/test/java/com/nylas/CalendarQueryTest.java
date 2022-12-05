package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalendarQueryTest {

    @Test
    public void testAddParameters() {
        HttpUrl.Builder url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com")
                .addPathSegment("calendars");

        CalendarQuery calendarQuery = new CalendarQuery();

        MetadataQuery metadataQuery = new MetadataQuery();
        metadataQuery
                .metadataValue("key1")
                .metadataValue("value1");

        calendarQuery.metadataQuery(metadataQuery);
        calendarQuery.addParameters(url);

        assertEquals(url.build().toString(), "https://api.nylas.com/calendars?metadata_value=key1&metadata_value=value1");
    }
}
