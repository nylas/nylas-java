package com.nylas;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BuildVersion {

	private static final String buildVersion = readBuildVersion();
	
	public static final String getBuildVersion() {
		return buildVersion;
	}
	
	private static String readBuildVersion() {
		try {
			// Look for version properties file left by build task first
			try(InputStream in = BuildVersion.class.getResourceAsStream("/version.properties")) {
				if (in != null) {
					String version = getVersionFromPropertiesStream(in);
					if (version != null && version.length() > 0) {
						return version;
					}
				}
			}
		
			// Otherwise, look directly for gradle.properties file (e.g. in an IDE)
			try (InputStream in = new FileInputStream("gradle.properties")) {
				String version = getVersionFromPropertiesStream(in);
				if (version != null && version.length() > 0) {
					return version;
				}
			}
		
		} catch (Throwable t) {
			// swallow
		}
		return "unknown";
	}
	
	private static String getVersionFromPropertiesStream(InputStream in) throws IOException {
		Properties props = new Properties();
		props.load(in);
		return props.getProperty("version");
	}
}
