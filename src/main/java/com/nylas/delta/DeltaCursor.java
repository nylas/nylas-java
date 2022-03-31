package com.nylas.delta;

import com.nylas.AccountOwnedModel;

import java.util.List;

public class DeltaCursor {

	private String cursor_start;
	private String cursor_end;
	private List<Delta<? extends AccountOwnedModel>> deltas;

	public String getCursorStart() {
		return cursor_start;
	}

	public String getCursorEnd() {
		return cursor_end;
	}

	public List<Delta<? extends AccountOwnedModel>> getDeltas() {
		return deltas;
	}

	@Override
	public String toString() {
		return "DeltaCursor [" +
				"cursor_start='" + cursor_start + '\'' +
				", cursor_end='" + cursor_end + '\'' +
				", deltas=" + deltas +
				']';
	}
}
