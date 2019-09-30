package com.nylas;

import java.util.Collections;
import java.util.List;

public class ExpandedThread extends Thread {

	private List<Message> messages = Collections.emptyList();

	@Override
	public String toString() {
		return "ExpandedThread [messages=" + messages + "]";
	}
	
	
	
}
