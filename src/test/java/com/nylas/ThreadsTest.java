package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static com.nylas.AccessTokenTest.TEST_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ThreadsTest {
    private NylasClient nylasClient;

    private Threads threads;

    @BeforeEach
    public void init() {
        nylasClient = mock(NylasClient.class);

        threads = new Threads(nylasClient, TEST_ACCESS_TOKEN);
    }

    @Test
    public void testConstruct() {
        assertNotNull(threads);
    }

    @Test
    public void testList() throws RequestFailedException, IOException {
        ThreadQuery threadQuery = new ThreadQuery();
        threadQuery.subject("Hello World");

        RemoteCollection<Thread> remoteCollection = new RemoteCollection<>(threads, "threads", Thread.class, threadQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Thread> result = threads.list();

        assertNotNull(result);
    }

    @Test
    public void testGetThread() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Thread thread = new Thread();
        FieldReflectionUtils.setField("subject", "Hello World", thread);
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(thread);

        Thread result = threads.get("0938nsdv90sd");

        assertNotNull(result);
        assertEquals(result.getId(), "0938nsdv90sd");
        assertEquals(result.getSubject(), "Hello World");
    }

    @Test
    public void testGetThread_expanded() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Thread thread = new Thread();
        FieldReflectionUtils.setField("subject", "Hello World", thread);
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(thread);

        Thread result = threads.get("0938nsdv90sd", true);

        assertNotNull(result);
        assertEquals(result.getId(), "0938nsdv90sd");
        assertEquals(result.getSubject(), "Hello World");
    }

    @Test
    public void testGetExpandedThreads() throws RequestFailedException, IOException {
        ThreadQuery threadQuery = new ThreadQuery();
        threadQuery.subject("Hello World");

        RemoteCollection<Thread> remoteCollection = new RemoteCollection<>(threads, "threads", Thread.class, threadQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Thread> result = threads.expanded(threadQuery);

        assertNotNull(result);
    }

    @Test
    public void testGetThreadIds() throws RequestFailedException, IOException {
        ThreadQuery threadQuery = new ThreadQuery();
        threadQuery.subject("Hello World");

        RemoteCollection<Thread> remoteCollection = new RemoteCollection<>(threads, "threads", Thread.class, threadQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<String> result = threads.ids(threadQuery);

        assertNotNull(result);
    }

    @Test
    public void testCount() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        ThreadQuery threadQuery = new ThreadQuery();
        threadQuery.from("morty@hbo.com");

        Count count = new Count();
        FieldReflectionUtils.setField("count", 100L, count);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(count);

        long result = threads.count(threadQuery);

        assertEquals(result, 100L);
    }

    @Test
    public void testSearch() throws RequestFailedException, IOException {
        ThreadQuery threadQuery = new ThreadQuery();
        threadQuery.from("morty@hbo.com");

        RemoteCollection<Thread> remoteCollection = new RemoteCollection<>(threads, "threads", Thread.class, threadQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Thread> result = threads.search("hello");

        assertNotNull(result);
    }

    @Test
    public void testSearch_limit_offset() throws RequestFailedException, IOException {
        ThreadQuery threadQuery = new ThreadQuery();
        threadQuery.from("morty@hbo.com");

        RemoteCollection<Thread> remoteCollection = new RemoteCollection<>(threads, "threads", Thread.class, threadQuery);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(remoteCollection);

        RemoteCollection<Thread> result = threads.search("hello", 10, 0);

        assertNotNull(result);
    }

    @Test
    public void testSetUnread() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Thread thread = new Thread();
        FieldReflectionUtils.setField("subject", "Hello World", thread);
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("unread", true, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        Thread result = threads.setUnread("0938nsdv90sd", true);

        assertEquals(result.getId(), "0938nsdv90sd");
        assertTrue(result.isUnread());
    }

    @Test
    public void testSetStarred() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Thread thread = new Thread();
        FieldReflectionUtils.setField("subject", "Hello World", thread);
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("starred", true, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        Thread result = threads.setStarred("0938nsdv90sd", true);

        assertEquals(result.getId(), "0938nsdv90sd");
        assertTrue(result.isStarred());
    }

    @Test
    public void testSetFolderId() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
        Thread thread = new Thread();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("subject", "Hello", thread);

        Folder folder = new Folder();
        FieldReflectionUtils.setField("id","oskjdvn", folder, 1);
        List<Folder> folders = Arrays.asList(folder);
        FieldReflectionUtils.setField("folders", folders, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        Thread result = threads.setFolderId("0938nsdv90sd", "oskjdvn");

        assertEquals(result.getId(), "0938nsdv90sd");
        assertEquals(result.getFolders().get(0).getId(), "oskjdvn");
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

        Thread thread = new Thread();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("subject", "Hello", thread);
        FieldReflectionUtils.setField("labels", labels, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        Thread result = threads.setLabelIds("dlsdv9034ljkdsc", labelIds);

        assertNotNull(result);
        assertEquals(result.getLabels().size(), 2);
        assertEquals(result.getLabels().get(0).getId(), "o09erg0-9efvb");
        assertEquals(result.getLabels().get(1).getId(), "slkdcjv8904ec");
    }

    @Test
    public void testSetLabelIds() throws NoSuchFieldException, IllegalAccessException, RequestFailedException, IOException {
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

        Thread thread = new Thread();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", thread, 0);
        FieldReflectionUtils.setField("subject", "Hello", thread);
        FieldReflectionUtils.setField("labels", labels, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(thread);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        Thread actualLabels = threads.setLabelIds("0-923ojiwdv", (Iterable<String>) labelIds);

        assertNotNull(actualLabels);
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

        Thread thread = new Thread();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", thread, 0);
        FieldReflectionUtils.setField("subject", "Hello", thread);
        FieldReflectionUtils.setField("labels", labels, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(thread);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        boolean actualLabels = threads.addLabel("0-923ojiwdv", "939393939");

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

        Thread thread = new Thread();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", thread, 0);
        FieldReflectionUtils.setField("subject", "Hello", thread);
        FieldReflectionUtils.setField("labels", labels, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(thread);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        boolean actualLabels = threads.addLabel("0-923ojiwdv", "slkdcjv8904ec"); // label already exists!

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

        Thread thread = new Thread();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", thread, 0);
        FieldReflectionUtils.setField("subject", "Hello", thread);
        FieldReflectionUtils.setField("labels", labels, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(thread);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        boolean actualLabels = threads.removeLabel("0-923ojiwdv", "939393939");

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

        Thread thread = new Thread();
        FieldReflectionUtils.setField("id", "0938nsdv90sd", thread, 1);
        FieldReflectionUtils.setField("account_id", "sldjkv894", thread, 0);
        FieldReflectionUtils.setField("subject", "Hello", thread);
        FieldReflectionUtils.setField("labels", labels, thread);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(thread);
        when(nylasClient.executePut(any(), any(), any(), any(), any())).thenReturn(thread);

        boolean actualLabels = threads.removeLabel("0-923ojiwdv", "slkdcjv8904ec"); // label already exists!

        assertTrue(actualLabels); // label existed and was deleted
    }
}
