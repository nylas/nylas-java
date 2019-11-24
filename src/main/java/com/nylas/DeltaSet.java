package com.nylas;

import java.util.List;

public class DeltaSet {

	private String cursor_start;
	private String cursor_end;
	private List<Delta> deltas;
	
	public String getCursorStart() {
		return cursor_start;
	}
	
	public String getCursorEnd() {
		return cursor_end;
	}
	
	public List<Delta> getDeltas() {
		return deltas;
	}

	@Override
	public String toString() {
		return "DeltaSet [cursor_start=" + cursor_start + ", cursor_end=" + cursor_end + ", deltas=" + deltas + "]";
	}
	
}
