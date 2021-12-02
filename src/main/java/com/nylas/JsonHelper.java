package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class JsonHelper {

	private static final Moshi moshi;
	
	static {
		moshi = new Moshi.Builder()
				.add(Event.WHEN_JSON_FACTORY)
				.add(Event.EVENT_NOTIFICATION_JSON_FACTORY)
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

	public static String mapToJson(Map<String, Object> map) {
		return mapAdapter.toJson(map);
	}
	
	public static Map<String, Object> jsonToMap(String json) {
		return fromJsonUnchecked(mapAdapter, json);
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
