package com.nylas;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.nylas.AccessTokenTest.TEST_ACCOUNT_ID;
import static org.junit.jupiter.api.Assertions.*;

public class FreeBusyQueryTest {

    @Test
    public void testBuildQuery() {
        FreeBusyQuery freeBusyQuery = new FreeBusyQuery()
                .startTime(Instant.ofEpochSecond(1670947843L))
                .endTime(Instant.ofEpochSecond(1670947862L))
                .emails("jdoe@gmail.com", "foo@nylas.com")
                .calendars(new FreeBusyCalendars(TEST_ACCOUNT_ID, Arrays.asList("this-is-immutable-so-cant-add()-more :D")));

        assertTrue(freeBusyQuery.isValid());

        Map<String, Object> queryMap = freeBusyQuery.toMap();
        assertEquals(queryMap.get("start_time"), 1670947843L);
        assertEquals(queryMap.get("end_time"), 1670947862L);

        @SuppressWarnings("unchecked")
        List<FreeBusyCalendars> freeBusyCalendars = (List<FreeBusyCalendars>) queryMap.get("calendars");
        @SuppressWarnings("unchecked")
        List<String> emails = (List<String>) queryMap.get("emails");

        assertEquals(freeBusyCalendars.size(), 1);
        assertEquals(emails.size(), 2);
    }

    @Test
    public void testBuildQuery_noInstants() {
        FreeBusyQuery freeBusyQuery = new FreeBusyQuery()
                .startTime(1670947843L)
                .endTime(1670947843L)
                .emails("jdoe@gmail.com", "foo@nylas.com")
                .calendars(new FreeBusyCalendars(TEST_ACCOUNT_ID, Arrays.asList("this-is-immutable-so-cant-add()-more :D")));

        assertTrue(freeBusyQuery.isValid());

        Map<String, Object> queryMap = freeBusyQuery.toMap();
        assertEquals(queryMap.get("start_time"), 1670947843L);
        assertEquals(queryMap.get("end_time"), 1670947843L);

        @SuppressWarnings("unchecked")
        List<FreeBusyCalendars> freeBusyCalendars = (List<FreeBusyCalendars>) queryMap.get("calendars");
        @SuppressWarnings("unchecked")
        List<String> emails = (List<String>) queryMap.get("emails");

        assertEquals(freeBusyCalendars.size(), 1);
        assertEquals(emails.size(), 2);
    }

    @Test
    public void testValidation_pass() {
        FreeBusyQuery freeBusyQuery = new FreeBusyQuery()
                .startTime(1670947843L)
                .endTime(1670947843L)
                .emails("jdoe@gmail.com", "foo@nylas.com")
                .calendars(new FreeBusyCalendars(TEST_ACCOUNT_ID, Arrays.asList("this-is-immutable-so-cant-add()-more :D")));


        assertDoesNotThrow(freeBusyQuery::validate);
    }

    @Test
    public void testValidation_trhows() {
        FreeBusyQuery freeBusyQuery = new FreeBusyQuery();

        assertThrows(IllegalArgumentException.class, freeBusyQuery::validate);
    }
}
