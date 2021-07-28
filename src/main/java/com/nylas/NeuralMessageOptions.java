package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class NeuralMessageOptions {
	private boolean ignore_links;
	private boolean ignore_images;
	private boolean ignore_tables;
	private boolean remove_conclusion_phrases;
	private boolean images_as_markdown;

	// Neural Signature Contact specific options
	private boolean parse_contacts;

	public NeuralMessageOptions ignoreLinks(boolean ignoreLinks) {
		ignore_links = ignoreLinks;
		return this;
	}

	public NeuralMessageOptions ignoreImages(boolean ignoreImages) {
		ignore_images = ignoreImages;
		return this;
	}

	public NeuralMessageOptions ignoreTables(boolean ignoreTables) {
		ignore_tables = ignoreTables;
		return this;
	}

	public NeuralMessageOptions removeConclusionPhrases(boolean removeConclusionPhrases) {
		remove_conclusion_phrases = removeConclusionPhrases;
		return this;
	}

	public NeuralMessageOptions imagesAsMarkdown(boolean imagesAsMarkdown) {
		images_as_markdown = imagesAsMarkdown;
		return this;
	}

	public NeuralMessageOptions parseContacts(boolean parseContacts) {
		parse_contacts = parseContacts;
		return this;
	}

	public Map<String, Boolean> toMap() {
		Map<String, Boolean> map = new HashMap<>();
		Maps.putIfNotNull(map, "ignore_links", ignore_links);
		Maps.putIfNotNull(map, "ignore_images", ignore_images);
		Maps.putIfNotNull(map, "ignore_tables", ignore_tables);
		Maps.putIfNotNull(map, "remove_conclusion_phrases", remove_conclusion_phrases);
		Maps.putIfNotNull(map, "images_as_markdown", images_as_markdown);
		Maps.putIfNotNull(map, "parse_contacts", parse_contacts);
		return map;
	}
}
