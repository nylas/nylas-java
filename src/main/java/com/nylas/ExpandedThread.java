package com.nylas;

import java.util.List;

public class ExpandedThread extends Thread {

	private List<Message> messages;

	@Override
	public String toString() {
		return "ExpandedThread [messages=" + messages + "]";
	}
	
	
	
}
