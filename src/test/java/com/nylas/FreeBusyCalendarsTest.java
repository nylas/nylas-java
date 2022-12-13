package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nylas.AccessTokenTest.TEST_ACCOUNT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FreeBusyCalendarsTest {
    private FreeBusyCalendars freeBusyCalendars;

    @BeforeEach
    public void init() {
        List<String> calendarIds = new ArrayList<>();
        calendarIds.add("sldkvnsd230-98");
        calendarIds.add("lskdjv9803");

        freeBusyCalendars = new FreeBusyCalendars(TEST_ACCOUNT_ID, calendarIds);
    }

    @Test
    public void testConstructor() {
        assertEquals(freeBusyCalendars.getCalendarIds().size(), 2);
        assertEquals(freeBusyCalendars.getAccountId(), TEST_ACCOUNT_ID);
    }

    @Test
    public void testSetAccountId() {
        assertEquals(freeBusyCalendars.getAccountId(), TEST_ACCOUNT_ID);

        freeBusyCalendars.setAccountId("123455");
        assertEquals(freeBusyCalendars.getAccountId(), "123455");
    }

    @Test
    public void testSetCalendarIds() {
        assertEquals(freeBusyCalendars.getCalendarIds().size(), 2);
        List<String> calendarIds = Arrays.asList("sldkvnsd230-98");

        freeBusyCalendars.setCalendarIds(calendarIds);
        assertEquals(freeBusyCalendars.getCalendarIds().size(), 1);
    }

    @Test
    public void testAddCalendarIds() {
        assertEquals(freeBusyCalendars.getCalendarIds().size(), 2);

        freeBusyCalendars.addCalendarIds("12309489045", "12309489045");
        assertEquals(freeBusyCalendars.getCalendarIds().size(), 4);
    }
}
