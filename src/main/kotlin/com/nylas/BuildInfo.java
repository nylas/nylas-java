package com.nylas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildInfo {
	
	private static final Logger log = LoggerFactory.getLogger(BuildInfo.class);

	static final String VERSION;
	static final String COMMIT_ID;
	static final String TIMESTAMP;
	
	static {
		
		String version = null;
		String commitId = null;
		String timestamp = null;
		try {
			// First, look for build.properties file left by gradle
			try(InputStream in = BuildInfo.class.getResourceAsStream("/build.properties")) {
				if (in != null) {
					Properties props = new Properties();
					props.load(in);
					version = props.getProperty("version");
					commitId = props.getProperty("commit.hash");
					timestamp = props.getProperty("build.timestamp");
				}
			}
		
			// Otherwise, look directly for gradle.properties file (e.g. in an IDE)
			if (version == null || version.isEmpty()) {
				try (InputStream in = new FileInputStream("gradle.properties")) {
					if (in != null) {
						Properties props = new Properties();
						props.load(in);
						version = props.getProperty("version");
					}
				}
			}
		
		} catch (Throwable t) {
			log.error("Failed to load build info", t);
		}

		VERSION = version;
		COMMIT_ID = commitId;
		TIMESTAMP = timestamp;
	}
}
