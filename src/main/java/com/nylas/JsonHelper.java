package com.nylas;

import java.io.IOException;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class JsonHelper {

	private static final Moshi moshi;
	
	static {
		moshi = new Moshi.Builder()
				.add(Event.WHEN_JSON_FACTORY)
				.build();
	}
	
	public static Moshi moshi() {
		return moshi;
	}
	
	public static <T> T fromJsonSafe(JsonAdapter<T> adapter, String json) {
		try {
			return adapter.fromJson(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final JsonAdapter<Map<String, Object>> mapAdapter = moshi.<Map<String,Object>>adapter(Map.class);

	public static String mapToJson(Map<String, Object> map) {
		return mapAdapter.toJson(map);
	}
	
	public static Map<String, Object> jsonToMap(String json) {
		return fromJsonSafe(mapAdapter, json);
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
