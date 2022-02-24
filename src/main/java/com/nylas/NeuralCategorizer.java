package com.nylas;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	/**
	 * This adapter works around the API only returning a display name for the labels
	 * before deserializing the list of display name is replaced by a list of {@link Label}
	 */
	@SuppressWarnings("unchecked")
	static class CategorizeCustomAdapter {
		@FromJson
		NeuralCategorizer fromJson(JsonReader reader, JsonAdapter<NeuralCategorizer> delegate) throws IOException {
			Map<String, Object> json = JsonHelper.jsonToMap(reader);

			if(json.get("labels") instanceof List) {
				List<String> labels = (List<String>) json.get("labels");
				List<Label> labelObjects = labels.stream().map(Label::new).collect(Collectors.toList());
				json.replace("labels", labelObjects);
			}
			return delegate.fromJson(JsonHelper.mapToJson(json));
		}
	}
}
