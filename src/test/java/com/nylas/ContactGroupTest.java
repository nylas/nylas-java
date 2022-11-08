package com.nylas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ContactGroupTest {
    private ContactGroup contactGroup;

    @Test
    public void testGetters() {
        contactGroup = new ContactGroup();

        assertNull(contactGroup.getName());
        assertNull(contactGroup.getPath());
        assertEquals(contactGroup.toString(), "ContactGroup [name=null, path=null, accountId=null, id=null]");
    }
}
