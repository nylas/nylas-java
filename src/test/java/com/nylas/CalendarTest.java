package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalendarTest {
    private Calendar calendar;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        calendar = new Calendar();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value1");

        calendar.setName("Main Calendar");
        calendar.setDescription("This is a test calendar");
        calendar.setLocation("US");
        calendar.setTimezone("America/Los_Angeles");
        calendar.setMetadata(metadata);
        FieldReflectionUtils.setField("hex_color", "#FFFFFF", calendar);
        FieldReflectionUtils.setField("read_only", true, calendar);
        FieldReflectionUtils.setField("is_primary", true, calendar);
    }

    @Test
    public void testGetters() {
        assertEquals(calendar.getName(), "Main Calendar");
        assertEquals(calendar.getDescription(), "This is a test calendar");
        assertEquals(calendar.getLocation(), "US");
        assertEquals(calendar.getTimezone(), "America/Los_Angeles");
        assertEquals(calendar.getMetadata().size(), 1);
        assertEquals(calendar.getHexColor(), "#FFFFFF");
        assertTrue(calendar.isPrimary());
        assertTrue(calendar.isReadOnly());
        assertTrue(calendar.isPrimary());
    }

    @Test
    public void testAddMetadata() {
        calendar.addMetadata("key2", "value2");

        assertEquals(calendar.getMetadata().size(), 2);
    }

    @Test
    public void testGetWriteableFields() {
        Map<String, Object> fields = calendar.getWritableFields(true);

        assertEquals(fields.size(), 5);
    }

    @Test
    public void testToString() {
        assertEquals(calendar.toString(), "Calendar [name=Main Calendar, description=This is a test calendar, location=US, timezone=America/Los_Angeles, hexColor=#FFFFFF, readOnly=true, isPrimary=true, metadata={key1=value1}]");
    }
}
