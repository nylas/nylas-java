package com.nylas;

import java.util.Collections;
import java.util.Map;

public abstract class RestfulModel {

	private String id;

	public String getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
	}
	
	/**
	 * Return the set of fields used for model create/update (POST/PUT)
	 * 
	 * Default is none, for types that do not allow writes.  Subclasses that
	 * allow for writes should override and implement
	 */
	Map<String, Object> getWritableFields(@SuppressWarnings("unused") boolean creation) {
		return Collections.emptyMap();
	}
	
}
