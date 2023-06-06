package com.nylas.delta;

public interface DeltaLongPollListener {

	void onDeltaCursor(DeltaCursor deltaCursor);
}
