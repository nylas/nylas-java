package com.nylas.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
	
	int getInt(String key, int defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		} else {
			return Integer.parseInt(value);
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
}
