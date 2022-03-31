package com.nylas.delta;

import com.nylas.AccountOwnedModel;

public interface DeltaStreamListener {

	void onDelta(Delta<? extends AccountOwnedModel> delta);
}
