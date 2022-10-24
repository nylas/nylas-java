package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GrantsTest {
    private NylasClient nylasClient;
    private HttpUrl.Builder baseUrl;

    @BeforeEach
    public void init() {
        baseUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com");

        nylasClient = mock(NylasClient.class);
    }

    @Test
    public void testConstructor() {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);

        assertNotNull(grants);
    }

    @Test
    public void testList() throws RequestFailedException, IOException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);
        RemoteCollection<Grant> grantList = grants.list();

        assertNotNull(grantList);
    }

    @Test
    public void testGetGrant() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);
        Grant grant = new Grant();
        grant.setProvider(Authentication.Provider.GOOGLE);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(grant);

        Grant result = grants.get("abc");

        assertNotNull(result);
        assertEquals(result.getProvider(), Authentication.Provider.GOOGLE);
    }

    @Test
    public void testCreateGrant() throws RequestFailedException, IOException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);

        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Grant grant = new Grant();
        grant.setSettings(settings);
        grant.setProvider(Authentication.Provider.GOOGLE);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any(), any())).thenReturn(grant);

        Grant actual = grants.create(grant);

        assertNotNull(actual);
        assertEquals(actual.getProvider(), Authentication.Provider.GOOGLE);
    }

    @Test
    public void testUpdate() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);

        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Grant grant = new Grant();
        setField("id", "abc", grant, true);
        grant.setSettings(settings);
        grant.setProvider(Authentication.Provider.GOOGLE);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePatch(any(), any(), any(), any(), any())).thenReturn(grant);

        Grant updated = grants.update(grant);

        assertEquals(updated.getId(), "abc");
    }

    @Test
    public void testUpdate_newGrant() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);

        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Grant grant = new Grant();
        grant.setSettings(settings);
        grant.setProvider(Authentication.Provider.GOOGLE);

        assertThrows(UnsupportedOperationException.class, () -> {
            grants.update(grant);
        });
    }

    @Test
    public void testSaveGrant_create() throws RequestFailedException, IOException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);

        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Grant grant = new Grant();
        grant.setSettings(settings);
        grant.setProvider(Authentication.Provider.GOOGLE);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePost(any(), any(), any(), any(), any())).thenReturn(grant);

        Grant actual = grants.save(grant);

        assertNotNull(actual);
        assertEquals(actual.getProvider(), Authentication.Provider.GOOGLE);
    }

    @Test
    public void testSaveGrant_update() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);

        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Grant grant = new Grant();
        setField("id", "abc", grant, true);
        grant.setSettings(settings);
        grant.setProvider(Authentication.Provider.GOOGLE);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executePatch(any(), any(), any(), any(), any())).thenReturn(grant);

        Grant updated = grants.save(grant);

        assertEquals(updated.getId(), "abc");
    }

    @Test
    public void testDeleteGrant() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);

        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Grant grant = new Grant();
        setField("id", "abc", grant, true);
        grant.setSettings(settings);
        grant.setProvider(Authentication.Provider.GOOGLE);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeDelete(any(), any(), any(), any())).thenReturn(null);

        String deleted = grants.delete("abc");

        assertNull(deleted);
    }

    @Test
    public void testOnDemandSync() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);
        Grant grant = new Grant();
        setField("id", "abc", grant, true);
        grant.setProvider(Authentication.Provider.GOOGLE);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(grant);

        Grant result = grants.onDemandSync("abc");

        assertNotNull(result);
        assertEquals(result.getProvider(), Authentication.Provider.GOOGLE);
    }

    @Test
    public void testOnDemandSync_from() throws RequestFailedException, IOException, NoSuchFieldException, IllegalAccessException {
        Grants grants = new Grants(nylasClient, "jhon-doe", baseUrl);
        Grant grant = new Grant();
        setField("id", "abc", grant, true);
        grant.setProvider(Authentication.Provider.GOOGLE);

        when(nylasClient.newUrlBuilder()).thenReturn(new HttpUrl.Builder());
        when(nylasClient.executeGet(any(), any(), any(), any())).thenReturn(grant);

        Grant result = grants.onDemandSync("abc", 12312312L);

        assertNotNull(result);
        assertEquals(result.getProvider(), Authentication.Provider.GOOGLE);
    }

    private void setField(String fieldName, Object fieldValue, Object o, boolean parent) throws NoSuchFieldException, IllegalAccessException {

        Field codeField = null;

        if(parent) {
            codeField = o.getClass().getSuperclass().getDeclaredField(fieldName);
        } else {
            codeField = o.getClass().getDeclaredField(fieldName);
        }

        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }
}
