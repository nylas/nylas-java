package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventTest {
    private Event event;

    private Event.When when = new Event.When() {
        @Override
        public String getObjectType() {
            return "testEvent";
        }
    };

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        event = new Event();
        event.setCustomerEventId("oiasdjvniu93");
        event.setEventCollectionId("slkdv90-3osd");
        event.setTitle("This will be fun");
        event.setDescription("Office offsite");
        event.setLocation("US");

        Event.Conferencing conferencing = new Event.Conferencing();
        conferencing.setAutocreate(new Event.Conferencing.Autocreate());
        conferencing.setDetails(new Event.Conferencing.Details());
        conferencing.setProvider("GOOGLE");
        event.setConferencing(conferencing);

        List<String> rrule = new LinkedList<>();
        rrule.add("FREQ=DAILY;UNTIL=20230107T230000");
        Event.Recurrence recurrence = new Event.Recurrence("America/Los_Angeles", rrule); // why is rrule a list?
        event.setRecurrence(recurrence);

        Event.Reminders reminders = new Event.Reminders(10, Event.Reminders.ReminderMethod.POPUP);
        event.setReminders(reminders);

        event.setWhen(when);

        event.setCapacity(10);

        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant());
        event.setParticipants(participants);

        event.setBusy(true);
        event.setHideParticipants(false);

        Map<String, String> metadata = new HashMap<>();
        event.setMetadata(metadata);

        List<String> roundRobinOrder = new ArrayList<>();
        roundRobinOrder.add("availability");
        roundRobinOrder.add("fairness");
        event.setRoundRobinOrder(roundRobinOrder);

        Event.Notification notification = new Event.Notification("testNotification") {
            @Override
            public String getType() {
                return super.getType();
            }

            @Override
            public int getMinutesBeforeEvent() {
                return super.getMinutesBeforeEvent();
            }

            @Override
            public void setMinutesBeforeEvent(int minutesBeforeEvent) {
                super.setMinutesBeforeEvent(minutesBeforeEvent);
            }
        };
        List<Event.Notification> notifications = new ArrayList<>();
        notifications.add(notification);
        event.setNotifications(notifications);

        event.addMetadata("ke1", "value1");
        event.addNotification(notification);
        event.addParticipants(new Participant());

        FieldReflectionUtils.setField("original_start_time", 1672250229L, event);
        FieldReflectionUtils.setField("master_event_id", "klsdfjv894", event);
        FieldReflectionUtils.setField("calendar_id", "lksdv9o83", event);
        FieldReflectionUtils.setField("ical_uid", "09123098", event);
        FieldReflectionUtils.setField("owner", "Rick Sanchez", event);
        FieldReflectionUtils.setField("organizer_email", "rick@r&m.com", event);
        FieldReflectionUtils.setField("organizer_name", "Rick Sanchez", event);
        FieldReflectionUtils.setField("visibility", "all", event);
        FieldReflectionUtils.setField("status", "ongoing", event);
        FieldReflectionUtils.setField("read_only", true, event);
    }

    @Test
    public void testConstructor() {
        Event.When when = () -> "testEvent";
        Event event1 = new Event("lsdkfvj89", when);

        assertEquals(event1.getWhen().getObjectType(), "testEvent");
    }

    @Test
    public void testGetters() {
        assertEquals(event.getObjectType(), "event");
        assertEquals(event.getCalendarId(),  "lksdv9o83");
        assertEquals(event.getIcalUid(), "09123098");
        assertEquals(event.getEventCollectionId(), "slkdv90-3osd");
        assertEquals(event.getTitle(), "This will be fun");
        assertEquals(event.getDescription(), "Office offsite");
        assertEquals(event.getWhen().getObjectType(), "testEvent");
        assertEquals(event.getLocation(), "US");
        assertEquals(event.getOwner(), "Rick Sanchez");
        assertEquals(event.getCustomerEventId(), "oiasdjvniu93");
        assertEquals(event.getOrganizerEmail(), "rick@r&m.com");
        assertEquals(event.getOrganizerName(), "Rick Sanchez");
        assertEquals(event.getVisibility(),  "all");
        assertEquals(event.getParticipants().size(), 2);
        assertEquals(event.getStatus(), "ongoing");
        assertEquals(event.getCapacity(), 10);
        assertTrue(event.getReadOnly());
        assertTrue(event.getBusy());
        assertFalse(event.getHideParticipants());
        assertEquals(event.getMetadata().size(), 1);
        assertEquals(event.getConferencing().getProvider(), "GOOGLE");
        assertEquals(event.getRoundRobinOrder().size(), 2);
        assertEquals(event.getNotifications().size(), 2);
        assertEquals(event.getRecurrence().getRrule().get(0), "FREQ=DAILY;UNTIL=20230107T230000");
        assertEquals(event.getReminders().getReminderMethod(), Event.Reminders.ReminderMethod.POPUP);
        assertEquals(event.getMasterEventId(), "klsdfjv894");
        assertEquals(event.getOriginalStartTime(), Instant.ofEpochSecond(1672250229L));
        assertNotNull(Event.getWhenJsonFactory());
        assertNotNull(event.toString());
    }

    @Test
    public void testAddNotifications() {
        Event.When when = () -> "testEvent";
        Event event1 = new Event("lsdkfvj89", when);
        event1.setNotifications(null);

        Event.Notification notification = new Event.Notification("testNotification") {
            @Override
            public String getType() {
                return super.getType();
            }

            @Override
            public int getMinutesBeforeEvent() {
                return super.getMinutesBeforeEvent();
            }

            @Override
            public void setMinutesBeforeEvent(int minutesBeforeEvent) {
                super.setMinutesBeforeEvent(minutesBeforeEvent);
            }
        };
        event1.addNotification(notification);
    }

    @Test
    public void testClearMetadata() {
        assertEquals(event.getMetadata().size(), 1);
        event.clearMetadata();
        assertEquals(event.getMetadata().size(), 0);
    }

    @Test
    public void testClearNotifications() {
        assertEquals(event.getNotifications().size(), 2);
        event.clearNotifications();
        assertEquals(event.getNotifications().size(), 0);
    }

    @Test
    public void testClearParticipants() {
        assertEquals(event.getParticipants().size(), 2);
        event.clearParticipants();
        assertEquals(event.getParticipants().size(), 0);
    }

    @Test
    public void testValidate() {
        event.setConferencing(null);
        event.setCapacity(null);
        assertTrue(event.isValid());
        assertDoesNotThrow(() -> {
            event.validate();
        });
    }

    @Test
    public void testGetWritableFields() {
        assertEquals(event.getWritableFields(true).size(), 16);
        assertEquals(event.getWritableFields(false).size(), 16);
    }

    @Test
    public void testRecurrence() {
        List<String> rrule = new LinkedList<>();
        rrule.add("FREQ=DAILY;UNTIL=20230107T230000");
        Event.Recurrence recurrence = new Event.Recurrence("America/Los_Angeles", rrule);

        assertEquals(recurrence.getRrule().size(), 1);
        assertEquals(recurrence.getTimezone(), "America/Los_Angeles");
    }

    @Test
    public void testRecurrenceDeserialization() {
        Event.Recurrence recurrence = new Event.Recurrence();

        assertNotNull(recurrence);
    }

    @Test
    public void testConferencingProviders() {
        assertEquals(Event.Conferencing.ConferencingProviders.GOOGLE_MEET.getName(), "Google Meet");
        assertEquals(Event.Conferencing.ConferencingProviders.GOTOMEETING.getName(), "GoToMeeting");
        assertEquals(Event.Conferencing.ConferencingProviders.MS_TEAMS.getName(), "Microsoft Teams");
        assertEquals(Event.Conferencing.ConferencingProviders.WEBEX.getName(), "WebEx");
        assertEquals(Event.Conferencing.ConferencingProviders.ZOOM.getName(), "Zoom Meeting");
    }

    @Test
    public void testSetConferencingProvider() {
        Event.Conferencing conferencing = new Event.Conferencing();
        conferencing.setProvider(Event.Conferencing.ConferencingProviders.GOOGLE_MEET);

        assertEquals(conferencing.getProvider(), "Google Meet");
    }

    @Test
    public void testConferencingDetails() {
        Event.Conferencing.Details details = new Event.Conferencing.Details();
        details.setPassword("abc");
        details.setMeetingCode("019283");
        details.setPhone(Arrays.asList("18001102938"));
        details.setPin("20202");
        details.setUrl("https://zoom.us/0-93urlkwedjf");

        Event.Conferencing conferencing = new Event.Conferencing();
        conferencing.setDetails(details);

        assertEquals(conferencing.getDetails().getMeetingCode(), "019283");
        assertEquals(conferencing.getDetails().getPassword(), "abc");
        assertEquals(conferencing.getDetails().getMeetingCode(), "019283");
        assertEquals(conferencing.getDetails().getPhone().size(), 1);
        assertEquals(conferencing.getDetails().getPin(), "20202");
        assertEquals(conferencing.getDetails().getUrl(), "https://zoom.us/0-93urlkwedjf");
    }

    @Test
    public void testConferencingAutoCreate() {
        Event.Conferencing.Autocreate autocreate = new Event.Conferencing.Autocreate();
        Map<String, String> settings = new HashMap<>();
        settings.put("code", "09-23");
        autocreate.setSettings(settings);

        Event.Conferencing conferencing = new Event.Conferencing();
        conferencing.setAutocreate(autocreate);

        assertEquals(conferencing.getAutocreate().getSettings().size(), 1);
    }

    @Test
    public void testReminders() {
        Event.Reminders reminders = new Event.Reminders(10, Event.Reminders.ReminderMethod.POPUP);

        assertEquals(reminders.getReminderMinutes(), 10);
        assertEquals(reminders.getReminderMethod(), Event.Reminders.ReminderMethod.POPUP);
        assertEquals(reminders.toString(), "Reminders [reminderMinutes=10, reminderMethod=popup]");
    }

    @Test
    public void testReminders_InvalidReminderMethod() throws NoSuchFieldException, IllegalAccessException {
        Event.Reminders reminders = new Event.Reminders(10, Event.Reminders.ReminderMethod.POPUP);
        FieldReflectionUtils.setField("reminder_method", "Slap", reminders);

        assertNull(reminders.getReminderMethod());
    }

    @Test
    public void testNotification() {
        Event.Notification notification = new TestNotification("TEST");
        notification.setMinutesBeforeEvent(10);

        assertEquals(notification.getMinutesBeforeEvent(), 10);
        assertEquals(notification.getType(), "TEST");
    }

    @Test
    public void testEmailNotification() {
        Event.EmailNotification emailNotification = new Event.EmailNotification();
        emailNotification.setBody("This is a reminder");
        emailNotification.setSubject("You have incoming meetings");
        emailNotification.setMinutesBeforeEvent(10);

        assertEquals(emailNotification.getBody(), "This is a reminder");
        assertEquals(emailNotification.getSubject(), "You have incoming meetings");
        assertEquals(emailNotification.getMinutesBeforeEvent(), 10);
        assertEquals(emailNotification.toString(), "EmailNotification [type=email, body=This is a reminder, subject=You have incoming meetings, minutesBeforeEvent=10]");
    }

    @Test
    public void testSMSNotification() {
        Event.SMSNotification smsNotification = new Event.SMSNotification();
        smsNotification.setMessage("this is a notification");

        assertEquals(smsNotification.getMessage(), "this is a notification");
        assertEquals(smsNotification.toString(), "SMSNotification [type=sms, message=this is a notification, minutesBeforeEvent=0]");
    }

    @Test
    public void testWehbhookNotification() {
        Event.WebhookNotification webhookNotification = new Event.WebhookNotification();
        webhookNotification.setUrl("https://api.nylas.com/");
        webhookNotification.setPayload("[]");

        assertEquals(webhookNotification.getPayload(), "[]");
        assertEquals(webhookNotification.getUrl(), "https://api.nylas.com/");
        assertEquals(webhookNotification.toString(), "WebhookNotification [type=webhook, url=https://api.nylas.com/, payload=[], minutesBeforeEvent=0]");
    }

    @Test
    public void testTime() {
        Event.Time serializeTimeConstructor = new Event.Time();
        assertNotNull(serializeTimeConstructor);

        Event.Time time = new Event.Time(Instant.ofEpochSecond(2938402039L));

        assertEquals(time.getObjectType(), "time");
        assertEquals(time.getTime(), Instant.ofEpochSecond(2938402039L));
        assertEquals(time.toString(), "Time [time=2063-02-11T06:47:19Z]");
    }

    @Test
    public void testZonedTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        Event.Time zonedTime = new Event.Time(zonedDateTime);

        assertEquals(zonedTime.getTime().getEpochSecond(), zonedDateTime.toInstant().getEpochSecond());
        assertEquals(zonedTime.getTimezone(), zonedDateTime.getZone().toString());
    }

    @Test
    public void testZonedTime_invalidTimeZone() {
        ZonedDateTime zonedDateTime = mock(ZonedDateTime.class);
        ZoneId zoneId = mock(ZoneId.class);

        when(zonedDateTime.getZone()).thenReturn(zoneId);
        when(zoneId.getId()).thenReturn("Bermuda/Triangle");

        assertThrows(IllegalArgumentException.class, () -> {
            Event.Time time = new Event.Time(zonedDateTime);
        });
    }

    @Test
    public void testTimeArgs() {
        Event.Time time = new Event.Time(908340958L, "America/Los_Angeles");

        assertEquals(time.getTime(), Instant.ofEpochSecond(908340958L));
        assertEquals(time.getTimezone(), "America/Los_Angeles");
    }

    @Test
    public void testTimeSpan() {
        Event.Timespan timespan = new Event.Timespan();
        assertNotNull(timespan);
    }

    @Test
    public void testTimeSpan_constructor() {
        Event.Timespan timespan = new Event.Timespan(Instant.ofEpochSecond(1672426121L), Instant.ofEpochSecond(1672426179L));

        assertEquals(timespan.getStartTime(), Instant.ofEpochSecond(1672426121L));
        assertEquals(timespan.getEndTime(), Instant.ofEpochSecond(1672426179L));
        assertEquals(timespan.getObjectType(), "timespan");
        assertEquals(timespan.toString(), "Timespan [start_time=2022-12-30T18:48:41Z, end_time=2022-12-30T18:49:39Z]");
    }

    @Test
    public void testTimeSpan_zonedStartAndEnd() {
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();

        Event.Timespan timespan = new Event.Timespan(startZonedDateTime, endZonedDateTime);

        assertEquals(timespan.getStartTimezone(), startZonedDateTime.getZone().toString());
        assertEquals(timespan.getEndTimezone(), endZonedDateTime.getZone().toString());
    }

    @Test
    public void testTimeSpan_zonedStartAndEnd_invalidStartTimezone() {
        ZonedDateTime startZonedDateTime =  mock(ZonedDateTime.class);
        ZoneId startZoneId = mock(ZoneId.class);

        when(startZonedDateTime.getZone()).thenReturn(startZoneId);
        when(startZoneId.getId()).thenReturn("Bermuda/Triangle");

        ZonedDateTime endZonedDateTime = ZonedDateTime.now();

        assertThrows(IllegalArgumentException.class, () -> {
            Event.Timespan timespan = new Event.Timespan(startZonedDateTime, endZonedDateTime);
        });
    }

    @Test
    public void testTimeSpan_zonedStartAndEnd_invalidEndTimezone() {
        ZonedDateTime endZonedDateTime =  mock(ZonedDateTime.class);
        ZoneId startZoneId = mock(ZoneId.class);

        when(endZonedDateTime.getZone()).thenReturn(startZoneId);
        when(startZoneId.getId()).thenReturn("Bermuda/Triangle");

        ZonedDateTime startZonedDateTime = ZonedDateTime.now();

        assertThrows(IllegalArgumentException.class, () -> {
            Event.Timespan timespan = new Event.Timespan(startZonedDateTime, endZonedDateTime);
        });
    }

    @Test
    public void testTimeSpan_args() {
        Event.Timespan timespan = new Event.Timespan(1672426872L, "America/Los_Angeles", 1672426894L, "America/Los_Angeles");

        assertEquals(timespan.getStartTime(), Instant.ofEpochSecond(1672426872L));
        assertEquals(timespan.getEndTime(), Instant.ofEpochSecond(1672426894L));
        assertEquals(timespan.getStartTimezone(), "America/Los_Angeles");
        assertEquals(timespan.getEndTimezone(), "America/Los_Angeles");
    }

    @Test
    public void testDate() {
        Event.Date date = new Event.Date();

        assertNotNull(date);
        assertEquals(date.getObjectType(), "date");
        assertEquals(date.toString(), "Date [date=null]");
    }

    @Test
    public void testDate_args() {
        LocalDate localDate = LocalDate.now();
        Event.Date date = new Event.Date(localDate);

        assertEquals(date.getDate(), LocalDate.parse(localDate.toString()));
    }

    @Test
    public void testDatespan() {
        Event.Datespan datespan = new Event.Datespan();

        assertNotNull(datespan);
        assertEquals(datespan.getObjectType(), "datespan");
        assertEquals(datespan.toString(), "Datespan [start_date=null, end_date=null]");
    }

    @Test
    public void testDatespan_args() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        Event.Datespan datespan = new Event.Datespan(startDate, endDate);

        assertEquals(datespan.getStartDate(), LocalDate.parse(startDate.toString()));
        assertEquals(datespan.getEndDate(), LocalDate.parse(endDate.toString()));
    }
}


