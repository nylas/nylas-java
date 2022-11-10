package com.nylas;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Timeout;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;;

public class GrantTest {

    @Test
    public void testConstructor() {
        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Grant grant = new Grant(Authentication.Provider.valueOf("GOOGLE"), settings);

        assertEquals(grant.getProvider(), Authentication.Provider.GOOGLE);
        assertEquals(grant.getProviderString(), "google");
        assertEquals(grant.getSettings(), settings);
    }

    @Test
    public void testDeserializeConstructor() {
        Grant grant = new Grant();

        assertNotNull(grant);
    }

    @Test
    public void testSetsProvider() {
        Grant grant = new Grant();
        grant.setProvider(Authentication.Provider.GOOGLE);

        assertEquals(grant.getProviderString(), "google");
        assertEquals(grant.getProvider(), Authentication.Provider.GOOGLE);
    }

    @Test
    public void testSetState() {
        Grant grant = new Grant();
        grant.setState("VALID");

        assertEquals(grant.getState(), "VALID");
    }

    @Test
    public void testSetScope() {
        Grant grant = new Grant();
        List<String> scopes = new ArrayList<>();
        scopes.add("CALENDAR");
        scopes.add("EMAIL");

        grant.setScope(scopes);

        assertEquals(grant.getScope(), scopes);

        grant.addScope("EMAIL_SEND");

        assertEquals(grant.getScope().size(), 3);
    }

    @Test
    public void testSetSettings() {
        Grant grant = new Grant();
        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        grant.setSettings(settings);

        assertEquals(grant.getSettings(), settings);

        grant.addSetting("setting2", "param2");

        assertEquals(grant.getSettings().size(), 2);
    }

    @Test
    public void testSetMetadata() {
        Grant grant = new Grant();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("meta", "value1");

        grant.setMetadata(metadata);

        assertEquals(grant.getMetadata(), metadata);

        grant.addMetadata("meta2", "value2");

        assertEquals(grant.getMetadata().size(), 2);
    }

    @Test
    public void testGetWritableFields_creationFalse() {
        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("meta", "value1");

        List<String> scopes = new ArrayList<>();
        scopes.add("CALENDAR");
        scopes.add("EMAIL");

        Grant grant = new Grant();
        grant.setSettings(settings);
        grant.setMetadata(metadata);
        grant.setScope(scopes);

        Map<String, Object> writeableFields = grant.getWritableFields(false);


        assertNotNull(writeableFields);
        assertEquals(writeableFields.get("settings"), settings);
        assertEquals(writeableFields.get("metadata"), metadata);
        assertEquals(writeableFields.get("scope"), scopes);
    }

    @Test
    public void testGetWritableFields_creationTrue() {
        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("meta", "value1");

        List<String> scopes = new ArrayList<>();
        scopes.add("CALENDAR");
        scopes.add("EMAIL");

        Grant grant = new Grant();
        grant.setSettings(settings);
        grant.setMetadata(metadata);
        grant.setScope(scopes);
        grant.setProvider(Authentication.Provider.GOOGLE);
        grant.setState("VALID");

        Map<String, Object> writeableFields = grant.getWritableFields(true);


        assertNotNull(writeableFields);
        assertEquals(writeableFields.get("settings"), settings);
        assertEquals(writeableFields.get("metadata"), metadata);
        assertEquals(writeableFields.get("scope"), scopes);
        assertEquals(writeableFields.get("provider"), "google");
        assertEquals(writeableFields.get("state"), "VALID");
    }

    @Test
    public void testToString() {
        Grant grant = new Grant();

        assertEquals(grant.toString(), "Grant [id='null', provider='null', state='null', email='null', ip='null', grant_status='null', user_agent='null', created_at=null, updated_at=null, scope=[], settings={}, metadata={}]");
    }

    @Test
    public void testValidate() {
        Map<String, String> settings = new HashMap<>();
        settings.put("setting1", "value1");

        Grant grant = new Grant();
        grant.setProvider(Authentication.Provider.GOOGLE);
        grant.setSettings(settings);

        assertDoesNotThrow(grant::validate);
    }

    @Test
    public void testNonWriteableFields() throws NoSuchFieldException, IllegalAccessException {
        Grant grant = new Grant();

        FieldSetter.setField("email", "noreply@nylas.com", grant);
        FieldSetter.setField("ip", "192.168.72.11", grant);
        FieldSetter.setField("grant_status", "GRANTED", grant);
        FieldSetter.setField("user_agent", "X-Nylas-SDK", grant);
        FieldSetter.setField("created_at", 1664987122L, grant);
        FieldSetter.setField("updated_at", 1664987123L, grant);

        assertEquals(grant.getEmail(), "noreply@nylas.com");
        assertEquals(grant.getIp(), "192.168.72.11");
        assertEquals(grant.getGrantStatus(), "GRANTED");
        assertEquals(grant.getUserAgent(), "X-Nylas-SDK");
        assertEquals(grant.getCreatedAt(), 1664987122L);
        assertEquals(grant.getUpdatedAt(), 1664987123L);
    }

    @Test
    public void testGrantCustomAdapter() throws IOException {
        final String jsonGrant = "{" +
                    "\"provider\": \"google\", " +
                    "\"settings\": {}" +
                "}";

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Grant> jsonAdapter = moshi.adapter(Grant.class);

        ReadableByteChannel channel = new Buffer().writeUtf8(jsonGrant);
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));
        JsonReader jsonReader = JsonReader.of(source);

        Grant.GrantCustomAdapter grantCustomAdapter = new Grant.GrantCustomAdapter();

        Grant theGrant = grantCustomAdapter.fromJson(jsonReader, jsonAdapter);

        assertNotNull(theGrant);
        assertEquals(theGrant.getProvider(), Authentication.Provider.GOOGLE);
    }

    @Test
    public void testGrantCustomAdapter_dataField() throws IOException {
        final String jsonGrant = "{" +
                "\"data\": {" +
                "\"provider\": \"google\", " +
                "\"settings\": {}" +
                "}}";

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Grant> jsonAdapter = moshi.adapter(Grant.class);

        ReadableByteChannel channel = new Buffer().writeUtf8(jsonGrant);
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));
        JsonReader jsonReader = JsonReader.of(source);

        Grant.GrantCustomAdapter grantCustomAdapter = new Grant.GrantCustomAdapter();

        Grant theGrant = grantCustomAdapter.fromJson(jsonReader, jsonAdapter);

        assertNotNull(theGrant);
        assertEquals(theGrant.getProvider(), Authentication.Provider.GOOGLE);
    }

    @Test
    public void testGrantListCustomAdapter() throws IOException {
        final String jsonGrant = "{ \"data\": [{" +
                "\"provider\": \"google\", " +
                "\"settings\": {}" +
                "}]}";

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List<Grant>> jsonAdapter = moshi.adapter((Type) List.class);

        ReadableByteChannel channel = new Buffer().writeUtf8(jsonGrant);
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));
        JsonReader jsonReader = JsonReader.of(source);

        Grant.GrantListCustomAdapter grantListCustomAdapter = new Grant.GrantListCustomAdapter();

        List<Grant> theGrants = grantListCustomAdapter.fromJson(jsonReader, jsonAdapter);

        assertNotNull(theGrants);
        assertEquals(theGrants.size(), 1);
    }

    @Test
    public void testGrantListCustomAdapter_empty() throws IOException {
        final String jsonGrant = "{ \"not\": \"found?\"}";

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List<Grant>> jsonAdapter = moshi.adapter((Type) List.class);

        ReadableByteChannel channel = new Buffer().writeUtf8(jsonGrant);
        BufferedSource source = Okio.buffer(new ByteChannelSource(channel, Timeout.NONE));
        JsonReader jsonReader = JsonReader.of(source);

        Grant.GrantListCustomAdapter grantListCustomAdapter = new Grant.GrantListCustomAdapter();

        List<Grant> theGrants = grantListCustomAdapter.fromJson(jsonReader, jsonAdapter);

        assertNotNull(theGrants);
        assertEquals(theGrants.size(), 0);
    }

}
