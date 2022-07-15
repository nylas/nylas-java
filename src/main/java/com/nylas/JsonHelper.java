package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nylas.delta.Delta;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class JsonHelper {

	private static final Moshi moshi;
	
	static {
		moshi = new Moshi.Builder()
				.add(Event.WHEN_JSON_FACTORY)
				.add(Event.EVENT_NOTIFICATION_JSON_FACTORY)
        		.add(Delta.ACCOUNT_OWNED_MODEL_JSON_FACTORY)
				.add(new NeuralCategorizer.CategorizeCustomAdapter())
				.add(new Integration.IntegrationCustomAdapter())
				.add(new Integration.IntegrationListCustomAdapter())
				.add(new Grant.GrantCustomAdapter())
				.add(new Grant.GrantListCustomAdapter())
				.add(new LoginInfo.LoginInfoCustomAdapter())
				.add(Date.class, new Rfc3339DateJsonAdapter().nullSafe())
				.build();
	}
	
	public static Moshi moshi() {
		return moshi;
	}
	
	public static Type listTypeOf(Type type) {
		return Types.newParameterizedType(List.class, type);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> JsonAdapter<T> adapter(Type type) {
		return (JsonAdapter<T>) moshi.adapter(type).indent("  ");
	}
	
	public static <T> T fromJsonUnchecked(JsonAdapter<T> adapter, String json) {
		try {
			return adapter.fromJson(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final JsonAdapter<Map<String, Object>> mapAdapter
		= moshi.<Map<String,Object>>adapter(Map.class).indent("  ");

	private static final JsonAdapter<List<Object>> listAdapter
			= moshi.<List<Object>>adapter(List.class).indent("  ");

	public static String mapToJson(Map<String, Object> map) {
		return mapAdapter.toJson(map);
	}

	public static String listToJson(List<Object> list) {
		return listAdapter.toJson(list);
	}
	
	public static Map<String, Object> jsonToMap(String json) {
		return fromJsonUnchecked(mapAdapter, json);
	}

	public static Map<String, Object> jsonToMap(JsonReader jsonReader) throws IOException {
		Map<String, Object> json = new HashMap<>();
		jsonReader.beginObject();
		while(jsonReader.hasNext()) {
			json.put(jsonReader.nextName(), jsonReader.readJsonValue());
		}
		jsonReader.endObject();

		return json;
	}
	
	private static final MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
	public static MediaType jsonType() {
		return jsonType;
	}
	
	public static RequestBody jsonRequestBody(Map<String, Object> params) {
		String json = mapToJson(params);
		return RequestBody.create(JsonHelper.jsonType(), json);
	}
}
