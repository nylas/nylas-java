package com.nylas.delta;

import com.nylas.Maps;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class representation of query parameters that can be passed in to Delta endpoints
 * @see <a href="https://developer.nylas.com/docs/api/#tag--Deltas">Deltas</a>
 */
public class DeltaQueryOptions {

	private String include_types;
	private String excluded_types;
	private boolean expanded_view = false;
	private static final String EXPANDED_VIEW_OPTION_VALUE = "expanded";

	public enum Type {
		CONTACT("contact"),
		FILE("file"),
		MESSAGE("message"),
		EVENT("event"),
		DRAFT("draft"),
		THREAD("thread"),
		FOLDER("folder"),
		LABEL("label"),

		;

		private final String name;

		Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public DeltaQueryOptions expandedView(boolean expandedView) {
		this.expanded_view = expandedView;
		return this;
	}

	public DeltaQueryOptions includeTypes(Type... includeTypes) {
		if(includeTypes.length > 0) {
			this.include_types = Arrays.stream(includeTypes)
					.map(Type::getName)
					.collect(Collectors.joining(","));
		}
		return this;
	}

	public DeltaQueryOptions excludedTypes(Type... excludedTypes) {
		if(excludedTypes.length > 0) {
			this.excluded_types = Arrays.stream(excludedTypes)
					.map(Type::getName)
					.collect(Collectors.joining(","));
		}
		return this;
	}

	/**
	 * Formats the Delta query options to a map
	 * @return A mapping of Delta query options
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		if(expanded_view) {
			map.put("view", EXPANDED_VIEW_OPTION_VALUE);
		}
		Maps.putIfNotNull(map, "include_types", include_types);
		Maps.putIfNotNull(map, "excluded_types", excluded_types);
		return map;
	}

	/**
	 * Formats the Delta query options to a map if the query is valid
	 * @return A mapping of Delta query options
	 * @throws IllegalArgumentException if both {@code include_types} and {@code excluded_types} are set
	 */
	public Map<String, String> toValidMap() {
		if(include_types != null && excluded_types != null) {
			throw new IllegalArgumentException("Cannot set both 'include_types' and 'excluded_types'");
		}

		return toMap();
	}
}
