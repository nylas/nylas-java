package com.nylas.examples;

import java.util.Properties;

import com.nylas.DeltaSet;
import com.nylas.Deltas;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class DeltasExample {

	
	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Deltas deltas = account.deltas();
		
		String cursor = deltas.fetchLatestCursor();
		System.out.println("Cursor: " + cursor);
		
		//DeltaSet deltaSet = deltas.getDeltas("6lavrjux2449hertnpni8i6nm");
		DeltaSet deltaSet = deltas.getDeltas("5n3bnzzalufhi0ltlol5799fc");
		System.out.println(deltaSet);
	}
}
