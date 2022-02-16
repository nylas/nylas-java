package com.nylas;

import com.nylas.delta.Delta;
import com.nylas.delta.DeltaCursor;
import com.nylas.delta.DeltaLongPollListener;
import com.nylas.delta.DeltaStreamListener;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonEncodingException;
import com.squareup.moshi.Types;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import okio.Buffer;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Deltas {

	private final NylasClient client;
	private final String accessToken;

	public Deltas(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	public String latestCursor() throws RequestFailedException, IOException {
		Map<String, String> response = client.executePost(accessToken, deltaEndpoint().addPathSegment("latest_cursor"), null, Map.class);
		String cursor = response.get("cursor");
		if(cursor == null) {
			throw new RuntimeException("Unexpected response from the API server. Returned 200 but no 'cursor' string found.");
		}

		return cursor;
	}

	public DeltaCursor since(String cursor) throws RequestFailedException, IOException {
		HttpUrl.Builder url = deltaEndpoint().addQueryParameter("cursor", cursor);
		return client.executeGet(accessToken, url, DeltaCursor.class);
	}

	public List<Delta<? extends AccountOwnedModel>> stream(String cursor) throws RequestFailedException, IOException {
		return stream(cursor, null);
	}

	public List<Delta<? extends AccountOwnedModel>> stream(String cursor, DeltaStreamListener listener)
			throws RequestFailedException, IOException {
		List<Delta<? extends AccountOwnedModel>> deltas = new ArrayList<>();
		Type deltaAdapterType = Types.newParameterizedType(Delta.class, AccountOwnedModel.class);
		JsonAdapter<Delta<? extends AccountOwnedModel>> adapter = JsonHelper.moshi().adapter(deltaAdapterType);
		HttpUrl.Builder url = deltaEndpoint()
				.addPathSegment("streaming")
				.addQueryParameter("cursor", cursor);

		ResponseBody responseBody = client.download(accessToken, url);
		StringBuilder jsonBuilder = new StringBuilder();
		try {
			while(!responseBody.source().exhausted()) {
				Buffer buffer = new Buffer();
				long count = responseBody.source().read(buffer, 8192);
				if(count > 1) {
					String[] jsonStream = buffer.readUtf8().split("\n");
					for(String json : jsonStream) {
						jsonBuilder.append(json);
						try {
							Delta<? extends AccountOwnedModel> delta = adapter.fromJson(jsonBuilder.toString());
							deltas.add(delta);
							if(listener != null) {
								listener.onDelta(delta);
							}
							jsonBuilder.setLength(0);
						} catch (JsonEncodingException | EOFException e) {
							// The JSON was partial, move on to the next string/chunk
						}
					}
				}
			}
		} catch (IllegalStateException e) {
			// Stream was closed, return the deltas we have
		}

		return deltas;
	}

	public DeltaCursor longpoll(String cursor, int timeout) throws RequestFailedException, IOException {
		return longpoll(cursor, timeout, null);
	}

	public DeltaCursor longpoll(String cursor, int timeout, DeltaLongPollListener listener)
			throws RequestFailedException, IOException {
		DeltaCursor deltaCursor = null;
		JsonAdapter<DeltaCursor> adapter = JsonHelper.moshi().adapter(DeltaCursor.class);
		HttpUrl.Builder url = deltaEndpoint()
				.addPathSegment("longpoll")
				.addQueryParameter("cursor", cursor)
				.addQueryParameter("timeout", String.valueOf(timeout));

		ResponseBody responseBody = client.download(accessToken, url);
		StringBuilder jsonBuilder = new StringBuilder();
		try {
			while(!responseBody.source().exhausted()) {
				Buffer buffer = new Buffer();
				long count = responseBody.source().read(buffer, 8192);
				if(count > 1) {
					String[] jsonStream = buffer.readUtf8().split("\n");
					for(String json : jsonStream) {
						jsonBuilder.append(json);
						try {
							deltaCursor = adapter.fromJson(jsonBuilder.toString());
							if(listener != null) {
								listener.onDeltaCursor(deltaCursor);
							}
							jsonBuilder.setLength(0);
						} catch (JsonEncodingException | EOFException e) {
							// The JSON was partial, move on to the next string/chunk
						}
					}
				}
			}
		} catch (IllegalStateException e) {
			// Stream was closed, return the deltas we have
		}

		return deltaCursor;
	}

	private HttpUrl.Builder deltaEndpoint() {
		return client.newUrlBuilder().addPathSegment("delta");
	}
}
