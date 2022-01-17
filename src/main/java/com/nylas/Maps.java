package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class Maps {

	public static <K,V> Map<K,V> of(K key, V val) {
		Map<K,V> map = new HashMap<>();
		map.put(key, val);
		return map;
	}

	public static <K,V> void putIfNotNull(Map<K, V> map, K key, V val) {
		if (key != null && val != null) {
			map.put(key, val);
		}
	}

	static <K,V> void putValueOrDefault(Map<K, V> map, K key, V val, V defaultVal) {
		if (key != null && val != null) {
			map.put(key, val);
		} else if(key != null) {
			map.put(key, defaultVal);
		}
	}
	
}
