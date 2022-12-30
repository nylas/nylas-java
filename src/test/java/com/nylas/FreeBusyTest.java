package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FreeBusyTest {
    private FreeBusy freeBusy;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        freeBusy = new FreeBusy();

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(Instant.ofEpochSecond(1670945645L));
        timeSlot.setEndTime(Instant.ofEpochSecond(1670945645L));

        List<TimeSlot> timeSlots = new LinkedList<>();
        timeSlots.add(timeSlot);

        FieldReflectionUtils.setField("email", "jdoe@gmail.com", freeBusy);
        FieldReflectionUtils.setField("time_slots", timeSlots, freeBusy);
    }

    @Test
    public void testGetters() {
        assertEquals(freeBusy.getEmail(), "jdoe@gmail.com");
        assertEquals(freeBusy.getTimeSlots().size(), 1);
        assertEquals(freeBusy.getTimeSlots().get(0).getStartTime(), Instant.ofEpochSecond(1670945645L));
        assertEquals(freeBusy.getTimeSlots().get(0).getEndTime(), Instant.ofEpochSecond(1670945645L));
    }

    @Test
    public void testToString() {
        assertEquals(freeBusy.toString(), "FreeBusy [email=jdoe@gmail.com, time_slots=[TimeSlot [status=null, start_time=2022-12-13T15:34:05Z, end_time=2022-12-13T15:34:05Z, emails=[]]]]");
    }
}
