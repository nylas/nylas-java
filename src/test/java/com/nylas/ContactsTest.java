package com.nylas;

import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Timeout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.List;

import static com.nylas.AccessTokenTest.TEST_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactsTest {
    private NylasClient nylasClient;


    @BeforeEach
    public void init() {
        nylasClient = mock(NylasClient.class);
    }

    @Test
    public void testConstructor() {
        Contacts contacts = new Contacts(nylasClient, TEST_ACCESS_TOKEN);

        assertNotNull(contacts);
    }

    @Test
    public void testList() throws RequestFailedException, IOException {
        Contacts contacts = new Contacts(nylasClient, TEST_ACCESS_TOKEN);

        ContactQuery contactQuery = new ContactQuery();
        contactQuery.email("dospam@gmail.com");

        RemoteCollection<Contact> remoteCollection = new RemoteCollection(contacts, "contacts", Contact.class, contactQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Contact> result = contacts.list();

        assertNotNull(result);
    }

    @Test
    public void testList_contactQuery() throws RequestFailedException, IOException {
        Contacts contacts = new Contacts(nylasClient, TEST_ACCESS_TOKEN);

        ContactQuery contactQuery = new ContactQuery();
        contactQuery.email("dospam@gmail.com");

        RemoteCollection<Contact> remoteCollection = new RemoteCollection(contacts, "contacts", Contact.class, contactQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Contact> result = contacts.list(contactQuery);

        assertNotNull(result);
    }

    @Test
    public void testGetContact() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Contacts contacts = new Contacts(nylasClient, TEST_ACCESS_TOKEN);

        Contact.Email email = new Contact.Email();
        FieldReflectionUtils.setField("email", "morty@hbo.com", email);

        List<Contact.Email> emails = new LinkedList<>();
        emails.add(email);

        Contact contact = new Contact();
        contact.setEmails(emails);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(contact);

        Contact result = contacts.get("091209293");

        assertNotNull(result);
        assertEquals(result.getEmails().get(0).getEmail(), "morty@hbo.com");
    }

    @Test
    public void testCreateContact() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Contacts contacts = new Contacts(nylasClient, TEST_ACCESS_TOKEN);

        Contact.Email email = new Contact.Email();
        FieldReflectionUtils.setField("email", "morty@hbo.com", email);

        List<Contact.Email> emails = new LinkedList<>();
        emails.add(email);

        Contact contact = new Contact();
        contact.setEmails(emails);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any(), any())).thenReturn(contact);

        Contact result = contacts.create(contact);

        assertNotNull(result);
        assertEquals(result.getEmails().get(0).getEmail(), "morty@hbo.com");
    }

    @Test
    public void testUpdateContact() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Contacts contacts = new Contacts(nylasClient, TEST_ACCESS_TOKEN);

        Contact.Email email = new Contact.Email();
        FieldReflectionUtils.setField("email", "morty@hbo.com", email);

        List<Contact.Email> emails = new LinkedList<>();
        emails.add(email);

        Contact contact = new Contact();
        FieldReflectionUtils.setField("id", "askldjvh9", contact, 1);
        contact.setEmails(emails);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(contact);

        Contact result = contacts.update(contact);

        assertNotNull(result);
        assertEquals(result.getEmails().get(0).getEmail(), "morty@hbo.com");
    }

    @Test
    public void testDeleteContact() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Contacts contacts = new Contacts(nylasClient, TEST_ACCESS_TOKEN);

        Contact.Email email = new Contact.Email();
        FieldReflectionUtils.setField("email", "morty@hbo.com", email);

        List<Contact.Email> emails = new LinkedList<>();
        emails.add(email);

        Contact contact = new Contact();
        FieldReflectionUtils.setField("id", "askldjvh9", contact, 1);
        contact.setEmails(emails);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeDelete(any(), any(), any(), any())).thenReturn(null);

        String result = contacts.delete(contact.getId());

        assertNull(result);
    }

    @Test
    public void testDownloadProfilePicture() throws RequestFailedException, IOException {
        Contacts contacts = new Contacts(nylasClient, TEST_ACCESS_TOKEN);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());

        when(nylasClient.download(any(), any())).thenReturn(new ResponseBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("svg");
            }

            @Override
            public long contentLength() {
                return 59834848L;
            }

            @Override
            public BufferedSource source() {
                String svgImage = "<svg xmlns=\"http://www.w3.org/2000/svg\"  " +
                        "viewBox=\"0 0 40 40\" width=\"16px\" height=\"16px\">" +
                        "<path fill=\"#bae0bd\" d=\"M20,38.5C9.8,38.5,1.5,30.2,1.5,20S9.8,1.5,20,1.5S38.5,9.8,38.5,20S30.2,38.5,20,38.5z\"/>" +
                        "<path fill=\"#5e9c76\" d=\"M20,2c9.9,0,18,8.1,18,18s-8.1,18-18,18S2,29.9,2,20S10.1,2,20,2 M20,1C9.5,1,1,9.5,1,20s8.5,19,19,19\ts19-8.5,19-19S30.5,1,20,1L20,1z\"/>" +
                        "<path fill=\"none\" stroke=\"#fff\" stroke-miterlimit=\"10\" stroke-width=\"3\" d=\"M11.2,20.1l5.8,5.8l13.2-13.2\"/></svg>";
                ReadableByteChannel channel = new Buffer().writeUtf8(svgImage);
                return Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));
            }
        });

        ResponseBody responseBody = contacts.downloadProfilePicture("902319idjkd");

        assertNotNull(responseBody);
        assertEquals(responseBody.contentLength(), 59834848L);
    }
}
