package com.nylas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;;

public class ContactGroupTest {
    private ContactGroup contactGroup;

    @Test
    public void testGetters() throws NoSuchFieldException, IllegalAccessException {
        contactGroup = new ContactGroup();
        FieldReflectionUtils.setField("name", "IT", contactGroup);
        FieldReflectionUtils.setField("path", "ORG", contactGroup);

        //Depth: ContactGroup[-1].AccountOwnedModel[0].RestfulModel[1]
        FieldReflectionUtils.setField("id", "lskdvnc83e", contactGroup, 1);
        FieldReflectionUtils.setField("account_id", "09123840", contactGroup, 0);

        assertEquals(contactGroup.getName(), "IT");
        assertEquals(contactGroup.getPath(), "ORG");
        assertEquals(contactGroup.toString(), "ContactGroup [name=IT, path=ORG, accountId=09123840, id=lskdvnc83e]");
    }
}
