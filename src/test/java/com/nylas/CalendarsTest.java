package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.nylas.AccessTokenTest.TEST_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalendarsTest {
    private NylasClient nylasClient;

    @BeforeEach
    public void init() {
        nylasClient = mock(NylasClient.class);
    }

    @Test
    public void testConstructor() {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        assertNotNull(calendars);
    }

    @Test
    public void testList() throws RequestFailedException, IOException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        MetadataQuery metadataQuery = new MetadataQuery();
        metadataQuery.metadataKey("key1");
        metadataQuery.metadataValue("value1");

        CalendarQuery calendarQuery = new CalendarQuery();
        calendarQuery.metadataQuery(metadataQuery);

        RemoteCollection<Contact> remoteCollection = new RemoteCollection(calendars, "contacts", Contact.class, calendarQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Calendar> result = calendars.list();

        assertNotNull(result);
    }

    @Test
    public void testList_contactQuery() throws RequestFailedException, IOException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        MetadataQuery metadataQuery = new MetadataQuery();
        metadataQuery.metadataKey("key1");
        metadataQuery.metadataValue("value1");

        CalendarQuery calendarQuery = new CalendarQuery();
        calendarQuery.metadataQuery(metadataQuery);

        RemoteCollection<Contact> remoteCollection = new RemoteCollection(calendars, "contacts", Contact.class, calendarQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Calendar> result = calendars.list(calendarQuery);

        assertNotNull(result);
    }

    @Test
    public void testGetCalendar() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        Calendar calendar = new Calendar();
        calendar.setName("test calendar");

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(calendar);

        Calendar result = calendars.get("091209293");

        assertNotNull(result);
        assertEquals(result.getName(), "test calendar");
    }

    @Test
    public void testCreateContact() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        Calendar calendar = new Calendar();
        calendar.setName("test calendar");

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any(), any())).thenReturn(calendar);

        Calendar result = calendars.create(calendar);

        assertNotNull(result);
        assertEquals(result.getName(), "test calendar");
    }

    @Test
    public void testUpdateContact() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        Calendar calendar = new Calendar();
        FieldReflectionUtils.setField("id", "lskdvhj", calendar, 1);
        calendar.setName("test calendar");

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(calendar);

        Calendar result = calendars.update(calendar);

        assertNotNull(result);
        assertEquals(result.getName(), "test calendar");
    }

    @Test
    public void testDeleteContact() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        Calendar calendar = new Calendar();
        calendar.setName("test calendar");

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeDelete(any(), any(), any(), any())).thenReturn(null);

        String result = calendars.delete("lksdjvoilsdh234");

        assertNull(result);
    }

    @Test
    public void testIds() throws RequestFailedException, IOException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        MetadataQuery metadataQuery = new MetadataQuery();
        metadataQuery.metadataKey("key1");
        metadataQuery.metadataValue("value1");

        CalendarQuery calendarQuery = new CalendarQuery();
        calendarQuery.metadataQuery(metadataQuery);

        RemoteCollection<Contact> remoteCollection = new RemoteCollection(calendars, "contacts", Contact.class, calendarQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<String> result = calendars.ids(calendarQuery);

        assertNotNull(result);
    }

    @Test
    public void testCount() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        MetadataQuery metadataQuery = new MetadataQuery();
        metadataQuery.metadataKey("key1");
        metadataQuery.metadataValue("value1");

        CalendarQuery calendarQuery = new CalendarQuery();
        calendarQuery.metadataQuery(metadataQuery);

        Count count = new Count();
        FieldReflectionUtils.setField("count", 100L, count);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(count);

        long result = calendars.count(calendarQuery);

        assertEquals(result, 100L);
    }

    @Test
    public void testCheckFreeBusy() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        FreeBusy freeBusy = new FreeBusy();
        FieldReflectionUtils.setField("email", "jdoe@gmail.com", freeBusy);

        List<FreeBusy> freeBusies = new LinkedList<>();
        freeBusies.add(freeBusy);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any())).thenReturn(freeBusies);

        List<FreeBusy> freeBusyList = calendars.checkFreeBusy(Instant.ofEpochSecond(1670537543L), Instant.ofEpochSecond(1670537543L), "jdoe@gmail.com");

        assertEquals(freeBusyList.size(), 1);
    }

    @Test
    public void testCheckFreeBusy_objectParameter() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        FreeBusy freeBusy = new FreeBusy();
        FieldReflectionUtils.setField("email", "jdoe@gmail.com", freeBusy);

        List<FreeBusy> freeBusies = new LinkedList<>();
        freeBusies.add(freeBusy);

        FreeBusyQuery freeBusyQuery = new FreeBusyQuery();
        freeBusyQuery.emails("jdoe@gmail.com");
        freeBusyQuery.startTime(Instant.ofEpochSecond(1670537543L));
        freeBusyQuery.endTime(Instant.ofEpochSecond(1670537543L));

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any())).thenReturn(freeBusies);

        List<FreeBusy> freeBusyList = calendars.checkFreeBusy(freeBusyQuery);

        assertEquals(freeBusyList.size(), 1);
    }

    @Test
    public void testSingleAvailability() throws RequestFailedException, IOException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        SingleAvailabilityQuery singleAvailabilityQuery = new SingleAvailabilityQuery();
        singleAvailabilityQuery.durationMinutes(60);
        singleAvailabilityQuery.intervalMinutes(60);
        singleAvailabilityQuery.startTime(Instant.ofEpochSecond(1670537543L));
        singleAvailabilityQuery.endTime(Instant.ofEpochSecond(1670537543L));
        singleAvailabilityQuery.emails(Arrays.asList("jdoe@gmail.com"));

        Availability availability = new Availability();

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any())).thenReturn(availability);

        Availability result = calendars.availability(singleAvailabilityQuery);

        assertEquals(result, availability);
    }

    @Test
    public void testMultipleAvailability() throws RequestFailedException, IOException {
        Calendars calendars = new Calendars(nylasClient, TEST_ACCESS_TOKEN);

        MultipleAvailabilityQuery multipleAvailabilityQuery = new MultipleAvailabilityQuery();
        multipleAvailabilityQuery.durationMinutes(60);
        multipleAvailabilityQuery.intervalMinutes(60);
        multipleAvailabilityQuery.startTime(Instant.ofEpochSecond(1670537543L));
        multipleAvailabilityQuery.endTime(Instant.ofEpochSecond(1670537543L));
        multipleAvailabilityQuery.emails(Arrays.asList(Arrays.asList("jdoe@gmail.com")));

        ConsecutiveAvailability consecutiveAvailability = new ConsecutiveAvailability();
        List<ConsecutiveAvailability> consecutiveAvailabilityList = new LinkedList<>();
        consecutiveAvailabilityList.add(consecutiveAvailability);

        List<List<ConsecutiveAvailability>> expected = new ArrayList<>();
        expected.add(consecutiveAvailabilityList);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any())).thenReturn(expected);

        List<List<ConsecutiveAvailability>> result = calendars.consecutiveAvailability(multipleAvailabilityQuery);

        assertEquals(result.size(), 1);
    }
}
