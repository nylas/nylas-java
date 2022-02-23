package com.nylas;

import java.util.List;

public class NeuralCategorizer extends Message {

	private Categorize categorizer;

	/**
	 * Available message categorization options
	 */
	public enum Category {
		CONVERSATION,
		FEED,

		;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public Categorize getCategorizer() {
		return categorizer;
	}

	@Override
	public String toString() {
		return "NeuralCategorizer [" +
				"thread_id='" + thread_id + '\'' +
				", subject='" + subject + '\'' +
				", from=" + from +
				", to=" + to +
				", cc=" + cc +
				", bcc=" + bcc +
				", reply_to=" + reply_to +
				", date=" + date +
				", unread=" + unread +
				", starred=" + starred +
				", snippet='" + snippet + '\'' +
				", body='" + body + '\'' +
				", files=" + files +
				", events=" + events +
				", folder=" + folder +
				", labels=" + labels +
				", metadata=" + metadata +
				", categorizer=" + categorizer +
				']';
	}

	public static class Categorize {

		private String category;
		private String model_version;
		private Long categorized_at;
		private List<String> subcategories;

		public String getCategory() {
			return category;
		}

		public String getModelVersion() {
			return model_version;
		}

		public Long getCategorizedAt() {
			return categorized_at;
		}

		public List<String> getSubcategories() {
			return subcategories;
		}

		@Override
		public String toString() {
			return "Categorize [" +
					"category='" + category + '\'' +
					", model_version='" + model_version + '\'' +
					", categorized_at=" + categorized_at +
					", subcategories=" + subcategories +
					']';
		}
	}
}
