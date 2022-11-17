package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static com.nylas.AccessTokenTest.TEST_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class MessagesTest {
    private NylasClient nylasClient;

    private Messages messages;

    @BeforeEach
    public void init() {
        nylasClient = mock(NylasClient.class);
        messages = new Messages(nylasClient, TEST_ACCESS_TOKEN);
    }

    @Test
    public void testConstructor() {
        assertNotNull(messages);
    }

    @Test
    public void testList() throws RequestFailedException, IOException {
        MessageQuery messageQuery = new MessageQuery();
        messageQuery.from("morty@hbo.com");

        RemoteCollection<Message> remoteCollection = new RemoteCollection<>(messages, "messages", Message.class, messageQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Message> result = messages.list();

        assertNotNull(result);
    }

    @Test
    public void testGetMessage() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("subject", "Hello", message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);

        Message result = messages.get("0938nsdv90sd");

        assertNotNull(result);
        assertEquals(result.getId(), "0938nsdv90sd");
        assertEquals(result.getSubject(), "Hello");
    }

    @Test
    public void testGetMessageExpanded() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("subject", "Hello", message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);

        Message result = messages.get("0938nsdv90sd", true);

        assertNotNull(result);
        assertEquals(result.getId(), "0938nsdv90sd");
        assertEquals(result.getSubject(), "Hello");
    }

    @Test
    public void testExpanded() throws RequestFailedException, IOException {
        MessageQuery messageQuery = new MessageQuery();
        messageQuery.from("morty@hbo.com");

        RemoteCollection<Message> remoteCollection = new RemoteCollection<>(messages, "messages", Message.class, messageQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Message> result = messages.expanded(messageQuery);

        assertNotNull(result);
    }

    @Test
    public void testIds() throws RequestFailedException, IOException {
        MessageQuery messageQuery = new MessageQuery();
        messageQuery.from("morty@hbo.com");

        RemoteCollection<String> remoteCollection = new RemoteCollection<>(messages, "messages", String.class, messageQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<String> result = messages.ids(messageQuery);

        assertNotNull(result);
    }

    @Test
    public void testCount() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        MessageQuery messageQuery = new MessageQuery();
        messageQuery.from("morty@hbo.com");

        Count count = new Count();
        FieldReflectionUtils.setField("count", 100L, count);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(count);

        long result = messages.count(messageQuery);

        assertEquals(result, 100L);
    }

    @Test
    public void testSearch() throws RequestFailedException, IOException {
        MessageQuery messageQuery = new MessageQuery();
        messageQuery.from("morty@hbo.com");

        RemoteCollection<Message> remoteCollection = new RemoteCollection<>(messages, "messages", Message.class, messageQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Message> result = messages.search("hello");

        assertNotNull(result);
    }

    @Test
    public void testSearch_limit_offset() throws RequestFailedException, IOException {
        MessageQuery messageQuery = new MessageQuery();
        messageQuery.from("morty@hbo.com");

        RemoteCollection<Message> remoteCollection = new RemoteCollection<>(messages, "messages", Message.class, messageQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Message> result = messages.search("hello", 10, 0);

        assertNotNull(result);
    }

    @Test
    public void testGetRaw() throws RequestFailedException, IOException {
        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com"));
        when(nylasClient.executeRequest(any(), any())).thenReturn("this is a message");

        String response = messages.getRaw("lskdvnmlksndv");

        assertNotNull(response);
        assertEquals(response, "this is a message");
    }

    @Test
    public void testSetUnread() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("unread", true, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        Message result = messages.setUnread("0938nsdv90sd", true);

        assertEquals(result.getId(), "0938nsdv90sd");
        assertTrue(result.getUnread());
    }

    @Test
    public void testSetStarred() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("starred", true, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        Message result = messages.setStarred("0938nsdv90sd", true);

        assertEquals(result.getId(), "0938nsdv90sd");
        assertTrue(result.getStarred());
    }

    @Test
    public void testSetFolderId() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("subject", "Hello", message);

        Folder folder = new Folder();
        FieldReflectionUtils.setField("id","oskjdvn", folder, 1);
        FieldReflectionUtils.setField("folder", folder, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        Message result = messages.setFolderId("0938nsdv90sd", "oskjdvn");

        assertEquals(result.getId(), "0938nsdv90sd");
        assertEquals(result.getFolder().getId(), "oskjdvn");
    }

    @Test
    public void testLabelIds() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        List<String> labelIds = new LinkedList<>();
        labelIds.add("o09erg0-9efvb");
        labelIds.add("slkdcjv8904ec");

        Label label1 = new Label();
        FieldReflectionUtils.setField("id", "o09erg0-9efvb", label1, 1);
        FieldReflectionUtils.setField("name", "inbox", label1);

        Label label2 = new Label();
        FieldReflectionUtils.setField("id", "slkdcjv8904ec", label2, 1);
        FieldReflectionUtils.setField("name", "important", label2);

        List<Label> labels = Arrays.asList(label1, label2);

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("labels", labels, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        Message result = messages.setLabelIds("dlsdv9034ljkdsc", labelIds);

        assertNotNull(result);
        assertEquals(result.getLabels().size(), 2);
        assertEquals(result.getLabels().get(0).getId(), "o09erg0-9efvb");
        assertEquals(result.getLabels().get(1).getId(), "slkdcjv8904ec");
    }

    @Test
    public void testAddLabels() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Set<String> labelIds =  new HashSet<>();
        labelIds.add("o09erg0-9efvb");
        labelIds.add("slkdcjv8904ec");

        List<String> updatedLabelIds = new LinkedList<>();
        updatedLabelIds.add("o09erg0-9efvb");
        updatedLabelIds.add("slkdcjv8904ec");
        updatedLabelIds.add("939393939");

        Label label1 = new Label();
        FieldReflectionUtils.setField("id", "o09erg0-9efvb", label1, 1);
        FieldReflectionUtils.setField("name", "inbox", label1);

        Label label2 = new Label();
        FieldReflectionUtils.setField("id", "slkdcjv8904ec", label2, 1);
        FieldReflectionUtils.setField("name", "important", label2);

        List<Label> labels = Arrays.asList(label1, label2);

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("labels", labels, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        boolean actualLabels = messages.addLabel("0-923ojiwdv", "939393939");

        assertTrue(actualLabels);
    }

    @Test
    public void testAddLabels_false() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Set<String> labelIds =  new HashSet<>();
        labelIds.add("o09erg0-9efvb");
        labelIds.add("slkdcjv8904ec");

        List<String> updatedLabelIds = new LinkedList<>();
        updatedLabelIds.add("o09erg0-9efvb");
        updatedLabelIds.add("slkdcjv8904ec");
        updatedLabelIds.add("939393939");

        Label label1 = new Label();
        FieldReflectionUtils.setField("id", "o09erg0-9efvb", label1, 1);
        FieldReflectionUtils.setField("name", "inbox", label1);

        Label label2 = new Label();
        FieldReflectionUtils.setField("id", "slkdcjv8904ec", label2, 1);
        FieldReflectionUtils.setField("name", "important", label2);

        List<Label> labels = Arrays.asList(label1, label2);

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("labels", labels, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        boolean actualLabels = messages.addLabel("0-923ojiwdv", "slkdcjv8904ec"); // label already exists!

        assertFalse(actualLabels);
    }

    @Test
    public void testRemoveLabels() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Set<String> labelIds =  new HashSet<>();
        labelIds.add("o09erg0-9efvb");
        labelIds.add("slkdcjv8904ec");

        List<String> updatedLabelIds = new LinkedList<>();
        updatedLabelIds.add("o09erg0-9efvb");
        updatedLabelIds.add("slkdcjv8904ec");
        updatedLabelIds.add("939393939");

        Label label1 = new Label();
        FieldReflectionUtils.setField("id", "o09erg0-9efvb", label1, 1);
        FieldReflectionUtils.setField("name", "inbox", label1);

        Label label2 = new Label();
        FieldReflectionUtils.setField("id", "slkdcjv8904ec", label2, 1);
        FieldReflectionUtils.setField("name", "important", label2);

        List<Label> labels = Arrays.asList(label1, label2);

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("labels", labels, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        boolean actualLabels = messages.removeLabel("0-923ojiwdv", "939393939");

        assertFalse(actualLabels); //label did not existed
    }

    @Test
    public void testRemoveLabels_true() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Set<String> labelIds =  new HashSet<>();
        labelIds.add("o09erg0-9efvb");
        labelIds.add("slkdcjv8904ec");

        List<String> updatedLabelIds = new LinkedList<>();
        updatedLabelIds.add("o09erg0-9efvb");
        updatedLabelIds.add("slkdcjv8904ec");
        updatedLabelIds.add("939393939");

        Label label1 = new Label();
        FieldReflectionUtils.setField("id", "o09erg0-9efvb", label1, 1);
        FieldReflectionUtils.setField("name", "inbox", label1);

        Label label2 = new Label();
        FieldReflectionUtils.setField("id", "slkdcjv8904ec", label2, 1);
        FieldReflectionUtils.setField("name", "important", label2);

        List<Label> labels = Arrays.asList(label1, label2);

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("labels", labels, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        boolean actualLabels = messages.removeLabel("0-923ojiwdv", "slkdcjv8904ec"); // label already exists!

        assertTrue(actualLabels); // label existed and was deleted
    }

    @Test
    public void testSetMetadata() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("provider", "google");

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("metadata", metadata, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        Message result = messages.setMetadata("lskdvn9", metadata);

        assertNotNull(result);
        assertEquals(result.getMetadata().size(), 1);
    }

    @Test
    public void testGetMetadata() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("provider", "google");

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("metadata", metadata, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);

        boolean result = messages.addMetadata("lskdvn9", "provider", "google");

        assertTrue(result);
    }

    @Test
    public void testGetMetadata_override() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("provider", "google");

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("metadata", metadata, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        boolean result = messages.addMetadata("lskdvn9", "provider", "google", false);

        assertFalse(result);
    }

    @Test
    public void testRemoveMetadata() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("provider", "google");

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("metadata", metadata, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        boolean result = messages.removeMetadata("lskdvn9", "provider");

        assertTrue(result);
    }

    @Test
    public void testRemoveMetadata_fail() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("provider", "google");

        Message message = new Message();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", message, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", message, 0);
        FieldReflectionUtils.setField("subject", "Hello", message);
        FieldReflectionUtils.setField("body", "How are you holding up... because I'm a potato...", message);
        FieldReflectionUtils.setField("metadata", metadata, message);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(message);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(message);

        boolean result = messages.removeMetadata("lskdvn9", "provider-nonononono");

        assertFalse(result);
    }
}
