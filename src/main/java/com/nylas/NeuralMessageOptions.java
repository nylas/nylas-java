package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class NeuralMessageOptions {
	private final Boolean ignore_links;
	private final Boolean ignore_images;
	private final Boolean ignore_tables;
	private final Boolean remove_conclusion_phrases;
	private final Boolean images_as_markdowns;

	public NeuralMessageOptions(Boolean ignore_links,
								Boolean ignore_images,
								Boolean ignore_tables,
								Boolean remove_conclusion_phrases,
								Boolean images_as_markdowns) {
		this.ignore_links = ignore_links;
		this.ignore_images = ignore_images;
		this.ignore_tables = ignore_tables;
		this.remove_conclusion_phrases = remove_conclusion_phrases;
		this.images_as_markdowns = images_as_markdowns;
	}

	public Map<String, Boolean> toMap() {
		Map<String, Boolean> map = new HashMap<>();
		map.put("ignore_links", ignore_links);
		map.put("ignore_images", ignore_images);
		map.put("ignore_tables", ignore_tables);
		map.put("remove_conclusion_phrases", remove_conclusion_phrases);
		map.put("images_as_markdowns", images_as_markdowns);
		return map;
	}
}
