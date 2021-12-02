package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class Maps {

	public static <K,V> Map<K,V> of(K key, V val) {
		Map<K,V> map = new HashMap<>();
		map.put(key, val);
		return map;
	}

	public static <K,V> Map<K,V> of(K key, V val, K key2, V val2) {
		Map<K,V> map = new HashMap<>();
		map.put(key, val);
		map.put(key2, val2);
		return map;
	}

	public static <K,V> void putIfNotNull(Map<K, V> map, K key, V val) {
		if (key != null && val != null) {
			map.put(key, val);
		}
	}
	
}
