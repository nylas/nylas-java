package com.nylas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScopeTest {
    @Test
    public void testScopes() {
        assertEquals(Scope.EMAIL.getName(), "email");
        assertEquals(Scope.EMAIL_MODIFY.getName(), "email.modify");
        assertEquals(Scope.EMAIL_READ_ONLY.getName(), "email.read_only");
        assertEquals(Scope.EMAIL_SEND.getName(), "email.send");
        assertEquals(Scope.EMAIL_FOLDERS_AND_LABELS.getName(), "email.folders_and_labels");
        assertEquals(Scope.EMAIL_METADATA.getName(), "email.metadata");
        assertEquals(Scope.EMAIL_DRAFTS.getName(), "email.drafts");
        assertEquals(Scope.CALENDAR.getName(), "calendar");
        assertEquals(Scope.CALENDAR_READ_ONLY.getName(), "calendar.read_only");
        assertEquals(Scope.CALENDAR_FREE_BUSY.getName(), "calendar.free_busy");
        assertEquals(Scope.ROOM_RESOURCES_READ_ONLY.getName(), "room_resources.read_only");
        assertEquals(Scope.CONTACTS.getName(), "contacts");
        assertEquals(Scope.CONTACTS_READ_ONLY.getName(), "contacts.read_only");

        assertEquals(Scope.EMAIL.name(), "EMAIL");
        assertEquals(Scope.EMAIL_MODIFY.name(), "EMAIL_MODIFY");
        assertEquals(Scope.EMAIL_READ_ONLY.name(), "EMAIL_READ_ONLY");
        assertEquals(Scope.EMAIL_SEND.name(), "EMAIL_SEND");
        assertEquals(Scope.EMAIL_FOLDERS_AND_LABELS.name(), "EMAIL_FOLDERS_AND_LABELS");
        assertEquals(Scope.EMAIL_METADATA.name(), "EMAIL_METADATA");
        assertEquals(Scope.EMAIL_DRAFTS.name(), "EMAIL_DRAFTS");
        assertEquals(Scope.CALENDAR.name(), "CALENDAR");
        assertEquals(Scope.CALENDAR_READ_ONLY.name(), "CALENDAR_READ_ONLY");
        assertEquals(Scope.CALENDAR_FREE_BUSY.name(), "CALENDAR_FREE_BUSY");
        assertEquals(Scope.ROOM_RESOURCES_READ_ONLY.name(), "ROOM_RESOURCES_READ_ONLY");
        assertEquals(Scope.CONTACTS.name(), "CONTACTS");
        assertEquals(Scope.CONTACTS_READ_ONLY.name(), "CONTACTS_READ_ONLY");
    }
}
