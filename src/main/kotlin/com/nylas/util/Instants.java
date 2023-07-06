package com.nylas.util;

import java.time.Instant;

/**
 * Some utility methods for java.time.Instant
 */
public class Instants {
	
	public static String formatEpochSecond(Instant instant) {
		return Long.toString(instant.getEpochSecond());
	}
	
	public static Instant toNullableInstant(Long epochSecond) {
		return epochSecond == null ? null : Instant.ofEpochSecond(epochSecond);
	}
}
