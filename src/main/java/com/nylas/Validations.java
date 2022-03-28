package com.nylas;

import java.util.List;
import java.util.Map;

class Validations {

	static void assertState(boolean valid, String message) {
		if (!valid) {
			throw new IllegalStateException(message);
		}
	}
	
	static void assertState(boolean valid, String message, Object argument) {
		if (!valid) {
			throw new IllegalStateException(String.format(message, argument));
		}
	}
	
	static void assertArgument(boolean valid, String message) {
		if (!valid) {
			throw new IllegalArgumentException(message);
		}
	}
	
	static void assertArgument(boolean valid, String message, Object argument) {
		if (!valid) {
			throw new IllegalArgumentException(String.format(message, argument));
		}
	}

	static boolean nullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	static boolean nullOrEmpty(List<?> l) {
		return l == null || l.isEmpty();
	}

	static boolean nullOrEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
}
