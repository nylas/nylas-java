package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadTest {
    private Thread thread;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        thread = new Thread();

        List<NameEmail> participants = Arrays.asList(
            new NameEmail("Peter, Potamus", "ppotamus@gmail.com")
        );

        List<Folder> folders = Arrays.asList(
                new Folder()
        );

        List<Label> labels = Arrays.asList(
                new Label()
        );

        List<String> messageIds = Arrays.asList(
                "sldfkvj893w",
                "losdkvj933"
        );

        List<String> draftIds = Arrays.asList(
                "sdklcvj93"
        );

        Message oneMessage = new Message();
        FieldReflectionUtils.setField("body", "this is a body", oneMessage);
        List<Message> messages = Arrays.asList(
                oneMessage
        );

        List<Draft> drafts = Arrays.asList(
                new Draft()
        );

        FieldReflectionUtils.setField("id", "solidkvjnopisd903", thread, 1);
        FieldReflectionUtils.setField("subject", "That thing I sent you", thread);
        FieldReflectionUtils.setField("unread", true, thread);
        FieldReflectionUtils.setField("starred", true, thread);
        FieldReflectionUtils.setField("last_message_timestamp", 1669219388L, thread);
        FieldReflectionUtils.setField("last_message_received_timestamp", 1669219395L, thread);
        FieldReflectionUtils.setField("last_message_sent_timestamp", 1669219384L, thread);
        FieldReflectionUtils.setField("first_message_timestamp", 1669219188L, thread);
        FieldReflectionUtils.setField("participants", participants, thread);
        FieldReflectionUtils.setField("snippet","this is a preview", thread);
        FieldReflectionUtils.setField("version", 1, thread);
        FieldReflectionUtils.setField("folders", folders, thread);
        FieldReflectionUtils.setField("labels", labels, thread);
        FieldReflectionUtils.setField("message_ids", messageIds, thread);
        FieldReflectionUtils.setField("draft_ids", draftIds, thread);
        FieldReflectionUtils.setField("messages", messages, thread);
        FieldReflectionUtils.setField("drafts", drafts, thread);
        FieldReflectionUtils.setField("has_attachments", true, thread);
    }

    @Test
    public void testGetters() {
        assertEquals(thread.getObjectType(), "thread");
        assertEquals(thread.getSubject(), "That thing I sent you");
        assertTrue(thread.isUnread());
        assertTrue(thread.isStarred());
        assertEquals(thread.getLastMessageTimestamp(), Instant.ofEpochSecond(1669219388L));
        assertEquals(thread.getLastMessageReceivedTimestamp(), Instant.ofEpochSecond(1669219395L));
        assertEquals(thread.getLastMessageSentTimestamp(), Instant.ofEpochSecond(1669219384L));
        assertEquals(thread.getFirstMessageTimestamp(), Instant.ofEpochSecond(1669219188L));
        assertEquals(thread.getParticipants().size(), 1);
        assertEquals(thread.getSnippet(), "this is a preview");
        assertEquals(thread.getVersion(), 1);
        assertEquals(thread.getFolders().size(), 1);
        assertEquals(thread.getFolders().size(), 1);
        assertTrue(thread.hasAttachments());
        assertEquals(thread.getMessageIds().size(), 2);
        assertEquals(thread.getDraftIds().size(), 1);
        assertEquals(thread.getMessages().size(), 1);
        assertEquals(thread.getDrafts().size(), 1);
        assertEquals(thread.toString(), "Thread [id=solidkvjnopisd903, account_id=null,subject=That thing I sent you, unread=true, starred=true, last_message_timestamp=2022-11-23T16:03:08Z, last_message_received_timestamp=2022-11-23T16:03:15Z, last_message_sent_timestamp=2022-11-23T16:03:04Z, first_message_timestamp=2022-11-23T15:59:48Z, participants=[NameEmail [name=Peter, Potamus, email=ppotamus@gmail.com]], snippet=this is a preview, version=1, folders=[Folder [id=null, name=null, display_name=null]], labels=[Label [id=null, name=null, display_name=null]], has_attachments=true, message_ids=[sldfkvj893w, losdkvj933], draft_ids=[sdklcvj93], messages=[Message [id=null, account_id=null, thread_id=null, subject=null, from=[], to=[], cc=[], bcc=[], reply_to=[], date=null, unread=null, starred=null, snippet=null, files=[], events=[], folder=null, reply_to_message_id=null, labels=[], headers={}, metadata={}]], drafts=[Draft [reply_to_message_id=null, version=null, account_id=null, thread_id=null, subject=null, from=[], to=[], cc=[], bcc=[], reply_to=[], date=null, unread=null, starred=null, snippet=null, body=null, files=[], folder=null, reply_to_message_id=null, labels=[], tracking=null, metadata={}]]]");
    }

    @Test
    public void testCreateReply() {
        Draft reply = thread.createReply();

        assertEquals(reply.getThreadId(), "solidkvjnopisd903");
        assertEquals(reply.getSubject(), "That thing I sent you");
    }
}
