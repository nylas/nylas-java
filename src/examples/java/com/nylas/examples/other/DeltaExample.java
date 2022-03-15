package com.nylas.examples.other;

import com.nylas.*;
import com.nylas.delta.*;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

class DeltaProcessor implements DeltaStreamListener, DeltaLongPollListener {

	private static final Logger log = LogManager.getLogger(DeltaProcessor.class);

	@Override
	public void onDelta(Delta<? extends AccountOwnedModel> delta) {
		log.info("Delta received! " + delta);
	}

	@Override
	public void onDeltaCursor(DeltaCursor deltaCursor) {
		log.info("Delta Cursor received! " + deltaCursor);
	}
}

public class DeltaExample {

	private static final Logger log = LogManager.getLogger(DeltaExample.class);

	public static void main(String[] args) throws RequestFailedException, IOException {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Deltas deltas = account.deltas();

		// Get the latest cursor
		String latestCursor = deltas.latestCursor();
		log.info("The latest cursor is: " + latestCursor);

		// Get all the delta cursors since the latest cursor
		DeltaCursor deltasSinceLatestCursor = deltas.since(latestCursor);
		log.info("The delta cursors since the latest cursor: " + deltasSinceLatestCursor);

		// Create an instance of our class that will listen for new Delta and DeltaCursor types
		DeltaProcessor deltaProcessor = new DeltaProcessor();

		// Stream deltas since the latest cursor
		deltas.stream(latestCursor);

		// Long-poll for deltas, with options passed in
		DeltaQueryOptions options = new DeltaQueryOptions()
				.expandedView(true)
				.includeTypes(DeltaQueryOptions.Type.EVENT, DeltaQueryOptions.Type.MESSAGE);
		deltas.longpoll(latestCursor, 30, deltaProcessor, options);
	}
}
