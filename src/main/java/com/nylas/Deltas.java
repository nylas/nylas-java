package com.nylas;

import com.nylas.delta.*;
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
	private static final int MAXIMUM_LONGPOLL_TIMEOUT_SECONDS = 1800;

	public Deltas(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	/**
	 * Get the latest Delta cursor
	 * @return The latest Delta cursor
	 * @see <a href="https://developer.nylas.com/docs/api/#post/delta/latest_cursor">Get a Delta Cursor</a>
	 */
	public String latestCursor() throws RequestFailedException, IOException {
		HttpUrl.Builder url = deltaEndpoint().addPathSegment("latest_cursor");
		Map<String, String> response = client.executePost(accessToken, url, null, Map.class);
		String cursor = response.get("cursor");
		if(cursor == null) {
			throw new RuntimeException("Unexpected response from the API server. Returned 200 but no 'cursor' string found.");
		}

		return cursor;
	}

	/**
	 * Get set of Deltas since the provided cursor
	 * <br> {@code options} is set to null.
	 * @see Deltas#since(String, DeltaQueryOptions) 
	 */
	public DeltaCursor since(String cursor) throws RequestFailedException, IOException {
		return since(cursor, null);
	}

	/**
	 * Get set of Deltas since the provided cursor
	 * @param cursor The cursor to query from
	 * @param options Additional options for this Delta query
	 * @return A {@link DeltaCursor} object containing the set of cursors since the provider cursor
	 * @throws IllegalArgumentException If cursor is null/empty or if options are invalid
	 * @see <a href="https://developer.nylas.com/docs/api/#get/delta">Request Delta Cursors</a>
	 */
	public DeltaCursor since(String cursor, DeltaQueryOptions options) throws RequestFailedException, IOException {
		if(Validations.nullOrEmpty(cursor)) {
			throw new IllegalArgumentException("Null or empty cursor passed in.");
		}
		HttpUrl.Builder url = deltaEndpoint().addQueryParameter("cursor", cursor);
		if(options != null) {
			options.toValidMap().forEach(url::addQueryParameter);
		}

		return client.executeGet(accessToken, url, DeltaCursor.class);
	}

	/**
	 * Stream Deltas since the provided cursor
	 * <br> {@code listener} is set to null.
	 * <br> {@code options} is set to null.
	 * @see Deltas#stream(String, DeltaStreamListener, DeltaQueryOptions) 
	 */
	public List<Delta<? extends AccountOwnedModel>> stream(String cursor) throws RequestFailedException, IOException {
		return stream(cursor, null, null);
	}

	/**
	 * Stream Deltas since the provided cursor
	 * <br> {@code listener} is set to null.
	 * @see Deltas#stream(String, DeltaStreamListener, DeltaQueryOptions)
	 */
	public List<Delta<? extends AccountOwnedModel>> stream(String cursor, DeltaQueryOptions options)
			throws RequestFailedException, IOException {
		return stream(cursor, null, options);
	}

	/**
	 * Stream Deltas since the provided cursor
	 * <br> {@code options} is set to null.
	 * @see Deltas#stream(String, DeltaStreamListener, DeltaQueryOptions)
	 */
	public List<Delta<? extends AccountOwnedModel>> stream(String cursor, DeltaStreamListener listener)
			throws RequestFailedException, IOException {
		return stream(cursor, listener, null);
	}

	/**
	 * Stream Deltas since the provided cursor
	 * @param cursor The cursor to start streaming from
	 * @param listener An object that implements {@link DeltaStreamListener} to listen on every Delta received
	 * @param options Additional options for this Delta query
	 * @return A list of all the {@link Delta} objects captured during the duration of the stream
	 * @throws IllegalArgumentException If cursor is null/empty or if options are invalid
	 * @see <a href="https://developer.nylas.com/docs/api#get/delta/streaming">Streaming Deltas</a>
	 */
	public List<Delta<? extends AccountOwnedModel>> stream(String cursor, DeltaStreamListener listener, DeltaQueryOptions options)
			throws RequestFailedException, IOException {
		if(Validations.nullOrEmpty(cursor)) {
			throw new IllegalArgumentException("Null or empty cursor passed in.");
		}
		List<Delta<? extends AccountOwnedModel>> deltas = new ArrayList<>();
		Type deltaAdapterType = Types.newParameterizedType(Delta.class, AccountOwnedModel.class);
		JsonAdapter<Delta<? extends AccountOwnedModel>> adapter = JsonHelper.moshi().adapter(deltaAdapterType);
		HttpUrl.Builder url = deltaEndpoint()
				.addPathSegment("streaming")
				.addQueryParameter("cursor", cursor);
		if(options != null) {
			options.toValidMap().forEach(url::addQueryParameter);
		}

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

	/**
	 * Poll for a DeltaCursor since the provided cursor until a delta is received or timeout is reached
	 * <br> {@code listener} is set to null.
	 * <br> {@code options} is set to null.
	 * @see Deltas#longpoll(String, int, DeltaLongPollListener, DeltaQueryOptions)
	 */
	public DeltaCursor longpoll(String cursor, int timeout) throws RequestFailedException, IOException {
		return longpoll(cursor, timeout, null, null);
	}

	/**
	 * Poll for a DeltaCursor since the provided cursor until a delta is received or timeout is reached
	 * <br> {@code options} is set to null.
	 * @see Deltas#longpoll(String, int, DeltaLongPollListener, DeltaQueryOptions)
	 */
	public DeltaCursor longpoll(String cursor, int timeout, DeltaLongPollListener listener)
			throws RequestFailedException, IOException {
		return longpoll(cursor, timeout, listener, null);
	}

	/**
	 * Poll for a DeltaCursor since the provided cursor until a delta is received or timeout is reached
	 * <br> {@code listener} is set to null.
	 * @see Deltas#longpoll(String, int, DeltaLongPollListener, DeltaQueryOptions)
	 */
	public DeltaCursor longpoll(String cursor, int timeout, DeltaQueryOptions options)
			throws RequestFailedException, IOException {
		return longpoll(cursor, timeout, null, options);
	}

	/**
	 * Poll for a DeltaCursor since the provided cursor until a delta is received or timeout is reached
	 * @param cursor The cursor to start polling from
	 * @param timeout The number of seconds to poll for before timing out, maximum is {@value #MAXIMUM_LONGPOLL_TIMEOUT_SECONDS}
	 * @param listener An object that implements {@link DeltaLongPollListener} to listen for a DeltaCursor
	 * @param options Additional options for this Delta query
	 * @return The {@link DeltaCursor} object captured while polling
	 * @throws IllegalArgumentException If cursor is null/empty, if timeout is invalid, or if options are invalid
	 * @see <a href="https://developer.nylas.com/docs/api/#get/delta/longpoll">Return Long-Polling Deltas</a>
	 */
	public DeltaCursor longpoll(String cursor, int timeout, DeltaLongPollListener listener, DeltaQueryOptions options)
			throws RequestFailedException, IOException {
		if(Validations.nullOrEmpty(cursor)) {
			throw new IllegalArgumentException("Null or empty cursor passed in.");
		}
		DeltaCursor deltaCursor = null;
		JsonAdapter<DeltaCursor> adapter = JsonHelper.moshi().adapter(DeltaCursor.class);
		HttpUrl.Builder url = deltaEndpoint()
				.addPathSegment("longpoll")
				.addQueryParameter("cursor", cursor)
				.addQueryParameter("timeout", String.valueOf(timeout));
		if(timeout < 0 || timeout > MAXIMUM_LONGPOLL_TIMEOUT_SECONDS) {
			throw new IllegalArgumentException(String.format(
					"Timeout must be a positive value not greater than %d.", MAXIMUM_LONGPOLL_TIMEOUT_SECONDS));
		}
		if(options != null) {
			options.toValidMap().forEach(url::addQueryParameter);
		}

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
