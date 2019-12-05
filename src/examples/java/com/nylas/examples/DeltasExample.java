package com.nylas.examples;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nylas.DeltaSet;
import com.nylas.Deltas;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class DeltasExample {

	private static final Logger log = LogManager.getLogger(DeltasExample.class);
	
	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Deltas deltas = account.deltas();
		
		String cursor = deltas.fetchLatestCursor();
		log.info("Cursor: " + cursor);
		
		//DeltaSet deltaSet = deltas.getDeltas("6lavrjux2449hertnpni8i6nm");
		//DeltaSet deltaSet = deltas.getDeltas(cursor);
		DeltaSet deltaSet = deltas.getDeltas("9ksliyey9zohqowc5sp638gw4");
		log.info(deltaSet);
	}
}
