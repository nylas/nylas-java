package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class represents a collection of results available via api on the server.
 * It fetches those results in chunks from the server. By default, the size of each chunk is 50, but is configurable
 * <p>
 * It can be accessed in 2 ways:
 *  <li>As an Iterable, which will fetch a chunk of results lazily as needed from the server when iterating
 *  <li>via fetchAll, which will eagerly fetch all chunks in advance
 * <p>
 * Note: It is possible that the results on the server change between loading chunks, which could possibly lead
 * to duplicate or skipped results.
 */
public class RemoteCollection<T> implements Iterable<T> {

	private static final int DEFAULT_CHUNK_SIZE = 100;
	
	private final RestfulDAO<?> dao;
	private final String view;
	private final Type type;
	private final RestfulQuery<?> query;

	private int chunkSize = DEFAULT_CHUNK_SIZE;
	
	public RemoteCollection(RestfulDAO<?> dao, String view, Type type, RestfulQuery<?> query) {
		this.dao = dao;
		this.view = view;
		this.type = type;
		this.query = query;
	}

	public RemoteCollection<T> chunkSize(int chunkSize) {
		if (chunkSize < 1) {
			throw new IllegalArgumentException("Chunk size must be positive");
		}
		this.chunkSize = chunkSize;
		return this;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iter();
	}
	
	public List<T> fetchAll() throws IOException, RequestFailedException {
		List<T> allResults = new ArrayList<>();
		int nextOffset = query.getEffectiveOffset();
		while (allResults.size() < query.getEffectiveLimit()) {
			int nextLimit = Math.min(chunkSize, query.getEffectiveLimit() - allResults.size());
			List<T> nextResults = fetchSet(nextOffset, nextLimit);
			allResults.addAll(nextResults);
			if (nextResults.size() < nextLimit) {
				// we didn't get as many as we requested. this means that's all the server has, so we stop
				break;
			}
			nextOffset += nextResults.size();
		}
		return allResults;
	}
	
	private List<T> fetchSet(int offset, int limit) throws IOException, RequestFailedException {
		RestfulQuery<?> nextQuery = query.copyAtNewOffsetLimit(offset, limit);
		return dao.fetchQuery(nextQuery, view, type);
	}
	
	public class Iter implements Iterator<T> {
		
		private List<T> buffer = Collections.emptyList();
		private int nextInBuffer = 0;
		
		private int nextOffset = query.getEffectiveOffset();
		private int remainingLimit = query.getEffectiveLimit();
		
		@Override
		public boolean hasNext() {
			if (nextInBuffer < buffer.size()) {
				return true;
			}
			if (remainingLimit == 0) {
				return false;
			}
			refillBuffer();
			return hasNext();
		}
		
		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T next = buffer.get(nextInBuffer);
			nextInBuffer++;
			return next;
		}
		
		private void refillBuffer() {
			try {
				int nextLimit = Math.min(chunkSize, remainingLimit);
				buffer = fetchSet(nextOffset, nextLimit);
				nextOffset += buffer.size();
				if (buffer.size() < nextLimit) {
					// we didn't get as many as we requested. this means that's all the server has, so we stop
					remainingLimit = 0;
				} else {
					remainingLimit -= buffer.size();
				}
				nextInBuffer = 0;
			} catch (IOException | RequestFailedException e) {
				throw new RuntimeException(e);
			}
		}


	}
}
