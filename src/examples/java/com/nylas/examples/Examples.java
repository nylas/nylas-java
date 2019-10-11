package com.nylas.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.nylas.NameEmail;

public class Examples {

	static Properties loadExampleProperties() throws IOException {
		Properties props = new Properties();
		try (final InputStream in = Examples.class.getResourceAsStream("/example.properties")) {
			props.load(in);
		}
		return props;
	}

	static NameEmail getNameEmail(Properties props, String nameProp, String emailProp) {
		String name = props.getProperty(nameProp, "");
		String email = props.getProperty(emailProp);
		return email == null ? null : new NameEmail(name, email);
	}

	static List<NameEmail> getNameEmailList(Properties props, String nameProp, String emailProp) {
		NameEmail nameEmail = getNameEmail(props, nameProp, emailProp);
		return nameEmail == null ? Collections.emptyList() : Arrays.asList(nameEmail);
	}
}
