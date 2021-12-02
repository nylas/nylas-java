package com.nylas;

import java.util.Collections;
import java.util.Map;

public abstract class Model {

	/**
	 * Return the set of fields used for model create/update (POST/PUT)
	 *
	 * Default is none, for types that do not allow writes.  Subclasses that
	 * allow for writes should override and implement
	 */
	protected Map<String, Object> getWritableFields(@SuppressWarnings("unused") boolean creation) {
		return Collections.emptyMap();
	}
}
