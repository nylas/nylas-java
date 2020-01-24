package com.nylas.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.nylas.NameEmail;

public class ExampleConf {

	private final Properties props = new Properties();
	
	ExampleConf() {
		try (final InputStream in = ExampleConf.class.getResourceAsStream("/example.properties")) {
			props.load(in);
		} catch (IOException ioe) {
			throw new RuntimeException("Failed to load example properties", ioe);
		}
	}

	/**
	 * Returns the configuration property for the given key
	 * 
	 * @throws RuntimeException if no such property is found.
	 */
	String get(String key) {
		String value = getOrNull(key);
		if (value == null) {
			throw new RuntimeException("No example property found for key: " + key);
		}
		return value;
	}
	
	/**
	 * Returns the configuration property for the given key or null if none is found.
	 */
	String getOrNull(String key) {
		return props.getProperty(key);
	}
	
	/**
	 * Returns the configuration property for the given key or the given default value if none is found.
	 */
	String get(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}
	
	boolean contains(String key) {
		return props.containsKey(key);
	}
	
	int getInt(String key, int defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		} else {
			return Integer.parseInt(value);
		}
	}
	
	boolean getBoolean(String key, boolean defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		} else {
			return Boolean.parseBoolean(value);
		}
	}
	
	NameEmail getNameEmail(String key) {
		String name = get(key + ".name", "");
		String email = get(key + ".email", null);
		return email == null ? null : new NameEmail(name, email);
	}

	List<NameEmail> getNameEmailList(String key) {
		NameEmail nameEmail = getNameEmail(key);
		return nameEmail == null ? Collections.emptyList() : Arrays.asList(nameEmail);
	}
	
	/**
	 * Return a map derived from all properties that start with a given prefix.
	 * The returned map has keys stripped of the prefix.
	 * <p>
	 * For example, if these are properties:
	 * <pre>
	 * menu.open.color=blue
	 * menu.open.size=10
	 * menu.close.color=red
	 * menu.close.size=5
	 * </pre>
	 * then calling getPrefixedEntries("menu.open.") will return a map with these entries:
	 * <pre>
	 * color=blue
	 * size=10
	 * </pre>
	 */
	Map<String, String> getPrefixedEntries(String prefix) {
		int prefixLength = prefix.length();
		Map<String, String> entries = new HashMap<>();
		for (String key : props.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				entries.put(key.substring(prefixLength), props.getProperty(key));
			}
		}
		return entries;
	}
}
