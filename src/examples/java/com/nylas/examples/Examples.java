package com.nylas.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Examples {

	static Properties loadExampleProperties() throws IOException {
		Properties props = new Properties();
		try (final InputStream in = Examples.class.getResourceAsStream("/example.properties")) {
			props.load(in);
		}
		return props;
	}

}
