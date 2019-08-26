package com.nylas;

import java.util.HashMap;
import java.util.Map;

class Maps {

	static <K,V> Map<K,V> of(K key, V val) {
		Map<K,V> map = new HashMap<>();
		map.put(key, val);
		return map;
	}
	
}
