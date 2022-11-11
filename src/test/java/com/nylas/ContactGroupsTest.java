package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.nylas.AccessTokenTest.TEST_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactGroupsTest {
    private NylasClient nylasClient;

    @BeforeEach
    public void init() {
        nylasClient = mock(NylasClient.class);
    }

    @Test
    public void testConstructor() {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com"));
        ContactGroups contactGroups = new ContactGroups(nylasClient, TEST_ACCESS_TOKEN);

        assertNotNull(contactGroups);
        assertEquals(contactGroups.getCollectionUrl().build().toString(), "https://api.nylas.com/contacts/groups");
    }

    @Test
    public void testListAll() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        // prepare expectations
        ContactGroup expectedGroup = new ContactGroup();
        FieldReflectionUtils.setField("name", "IT", expectedGroup);
        FieldReflectionUtils.setField("path", "ORG", expectedGroup);

        //Depth: ContactGroup[-1].AccountOwnedModel[0].RestfulModel[1]
        FieldReflectionUtils.setField("id", "lskdvnc83e", expectedGroup, 1);
        FieldReflectionUtils.setField("account_id", "09123840", expectedGroup, 0);

        List<ContactGroup> expectedGroups = new ArrayList<>();
        expectedGroups.add(expectedGroup);

        // set expectations
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com"));
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(expectedGroups);

        // test!
        ContactGroups contactGroups = new ContactGroups(nylasClient, TEST_ACCESS_TOKEN);

        List<ContactGroup> groups = contactGroups.listAll();
        assertTrue(groups.size() > 0);
    }




}
