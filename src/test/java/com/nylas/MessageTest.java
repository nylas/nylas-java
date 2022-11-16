package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    private Message message;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        message = new Message();

        FieldReflectionUtils.setField("thread_id", "okdvc089", message);
        FieldReflectionUtils.setField("subject", "That thing I sent you", message);
        FieldReflectionUtils.setField("body", "Did you get that thing I sent you? /n Potamus.", message);
        FieldReflectionUtils.setField("date", 1668083280L, message);
        FieldReflectionUtils.setField("unread", true, message);
        FieldReflectionUtils.setField("starred", true, message);
        FieldReflectionUtils.setField("snippet", "Did you get that thing...", message);

        FieldReflectionUtils.setField("from", Arrays.asList(
                getNameEmail("Peter Potamus", "potamus.peter@sebbebandsebben.com")), message);
        FieldReflectionUtils.setField("to", Arrays.asList(
                getNameEmail("Blue Falcone", "falcone.blue@sebbebandsebben.com")), message);
        FieldReflectionUtils.setField("cc", Arrays.asList(
                getNameEmail("Harvey Birdman", "harvey.birdman@sebbebandsebben.com")), message);
        FieldReflectionUtils.setField("bcc", Arrays.asList(
                getNameEmail("Christie", "christie@sebbebandsebben.com")), message);
        FieldReflectionUtils.setField("reply_to", Arrays.asList(
                getNameEmail("Peter Potamus", "potamus.peter@sebbebandsebben.com")), message);
        FieldReflectionUtils.setField("files", Arrays.asList(getFile("logo.jpg", "image/jpeg")), message);
        FieldReflectionUtils.setField("events", Arrays.asList(new Event()), message);
        FieldReflectionUtils.setField("folder", new Folder(), message);
        FieldReflectionUtils.setField("labels", Arrays.asList(new Label()), message);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value");
        FieldReflectionUtils.setField("metadata", metadata, message);

        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value");
        FieldReflectionUtils.setField("headers", headers, message);
    }

    @Test
    public void testGetters() {
        assertEquals(message.getObjectType(), "message");
        assertEquals(message.getThreadId(), "okdvc089");
        assertEquals(message.getSubject(), "That thing I sent you");
        assertEquals(message.getBody(), "Did you get that thing I sent you? /n Potamus.");
        assertEquals(message.getDate(), Instant.ofEpochSecond(1668083280L));
        assertTrue(message.getStarred());
        assertTrue(message.getUnread());
        assertEquals(message.getSnippet(), "Did you get that thing...");

        assertEquals(message.getFrom().get(0).getEmail(), "potamus.peter@sebbebandsebben.com");
        assertEquals(message.getTo().get(0).getEmail(), "falcone.blue@sebbebandsebben.com");
        assertEquals(message.getCc().get(0).getEmail(), "harvey.birdman@sebbebandsebben.com");
        assertEquals(message.getBcc().get(0).getEmail(), "christie@sebbebandsebben.com");
        assertEquals(message.getReplyTo().get(0).getEmail(), "potamus.peter@sebbebandsebben.com");
        assertEquals(message.getFiles().get(0).getSize(), 12334);
        assertEquals(message.getEvents().size(), 1);
        assertNotNull(message.getFolder());
        assertEquals(message.getLabels().size(), 1);
        assertEquals(message.getMetadata().size(), 1);
        assertEquals(message.getHeaders().size(), 1);
        assertEquals(message.toString(), "Message [id=null, account_id=null, thread_id=okdvc089, subject=That thing I sent you, from=[NameEmail [name=Peter Potamus, email=potamus.peter@sebbebandsebben.com]], to=[NameEmail [name=Blue Falcone, email=falcone.blue@sebbebandsebben.com]], cc=[NameEmail [name=Harvey Birdman, email=harvey.birdman@sebbebandsebben.com]], bcc=[NameEmail [name=Christie, email=christie@sebbebandsebben.com]], reply_to=[NameEmail [name=Peter Potamus, email=potamus.peter@sebbebandsebben.com]], date=2022-11-10T12:28:00Z, unread=true, starred=true, snippet=Did you get that thing..., body.length=46, files=[File [id=null, filename=logo.jpg, size=12334, content_type=image/jpeg, message_ids=[odjashjcv89], content_id=osdivcnm90834e]], events=[Event [id='null', calendar_id='null', ical_uid='null', master_event_id='null', event_collection_id='null', title='null', description='null', location='null', owner='null', status='null', capacity=null, read_only=null, busy=null, hide_participant=null, original_start_time=null, when=null, conferencing=null, recurrence=null, round_robin_order=[], notifications=[], participants=[], metadata={}]], folder=Folder [id=null, name=null, display_name=null], labels=[Label [id=null, name=null, display_name=null]], headers={header1=value}, metadata={key1=value}]");
    }


    private NameEmail getNameEmail(String name, String email) throws NoSuchFieldException, IllegalAccessException {
        NameEmail nameEmail = new NameEmail();
        FieldReflectionUtils.setField("name", name, nameEmail);
        FieldReflectionUtils.setField("email", email, nameEmail);

        return nameEmail;
    }

    private File getFile(String filename, String contentType) throws NoSuchFieldException, IllegalAccessException {
        File file = new File();

        FieldReflectionUtils.setField("filename", filename, file);
        FieldReflectionUtils.setField("size", 12334, file);
        FieldReflectionUtils.setField("content_type", contentType, file);
        FieldReflectionUtils.setField("message_ids", Arrays.asList("odjashjcv89"), file);
        FieldReflectionUtils.setField("content_id", "osdivcnm90834e", file);

        return file;
    }
}
