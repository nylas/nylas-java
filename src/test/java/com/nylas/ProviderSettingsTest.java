package com.nylas;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProviderSettingsTest {

    @Test
    public void testConstructor() {
        ProviderSettings providerSettings = new ProviderSettings("google");
        String providerName = providerSettings.getName();

        assertEquals(providerName, "google");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerNull() {
        ProviderSettings knownImapProviderSettings = ProviderSettings.getProviderSettingsByProvider(null);

        assertNotNull(knownImapProviderSettings);
    }

    @Test
    public void testGetProviderSettingsByProvider_providerGmail() {
        ProviderSettings gmailSettings = ProviderSettings.getProviderSettingsByProvider("gmail");

        assertEquals(gmailSettings.getName(), "gmail");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerImap() {
        ProviderSettings imapSettings = ProviderSettings.getProviderSettingsByProvider("imap");

        assertEquals(imapSettings.getName(), "imap");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerOffice365() {
        ProviderSettings o365Settings = ProviderSettings.getProviderSettingsByProvider("office365");

        assertEquals(o365Settings.getName(), "office365");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerExchange() {
        ProviderSettings exchangeSettings = ProviderSettings.getProviderSettingsByProvider("exchange");

        assertEquals(exchangeSettings.getName(), "exchange");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerYahoo() {
        ProviderSettings yahooSettings = ProviderSettings.getProviderSettingsByProvider("yahoo");

        assertEquals(yahooSettings.getName(), "yahoo");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerAOL() {
        ProviderSettings aolSettings = ProviderSettings.getProviderSettingsByProvider("aol");

        assertEquals(aolSettings.getName(), "aol");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerHotmail() {
        ProviderSettings hotmailSettings = ProviderSettings.getProviderSettingsByProvider("hotmail");

        assertEquals(hotmailSettings.getName(), "hotmail");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerOutlook() {
        ProviderSettings outlookSettings = ProviderSettings.getProviderSettingsByProvider("outlook");

        assertEquals(outlookSettings.getName(), "outlook");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerICloud() {
        ProviderSettings iCloudSettings = ProviderSettings.getProviderSettingsByProvider("icloud");

        assertEquals(iCloudSettings.getName(), "icloud");
    }

    @Test
    public void testGetProviderSettingsByProvider_providerDefault() {
        ProviderSettings knownImapProviderSettings = ProviderSettings.getProviderSettingsByProvider("zoho-mail");

        assertNotNull(knownImapProviderSettings);
    }

    @Test
    public void testAddSettings() {
        ProviderSettings providerSettings = new ProviderSettings("google");
        providerSettings.add("key1", "setting1");
        providerSettings.add("key2", "setting2");

        assertEquals(providerSettings.getValidatedSettings().size(), 2);
    }

    @Test
    public void testAddAllSettings() {
        Map<String, String> settings = new HashMap<>();
        settings.put("key1", "setting1");
        settings.put("key2", "setting2");

        ProviderSettings providerSettings = new ProviderSettings("google");
        providerSettings.addAll(settings);

        assertEquals(providerSettings.getValidatedSettings().size(), 2);
    }

    @Test
    public void testAssertSetting() {
        Map<String, String> settings = new HashMap<>();
        settings.put("key1", "setting1");
        settings.put("key2", "setting2");

        ProviderSettings providerSettings = new ProviderSettings("google");
        providerSettings.addAll(settings);

        providerSettings.assertSetting("key1", "I'm sorry key1 was not found");
    }

    @Test
    public void testToString() {
        ProviderSettings providerSettings = new ProviderSettings("google");

        assertEquals(providerSettings.toString(), "ProviderSettings [providerName=google, settings={}]");
    }
}
