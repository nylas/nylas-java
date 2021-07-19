package com.nylas.examples.other;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RoomResource;
import com.nylas.RoomResources;
import com.nylas.examples.ExampleConf;

import java.util.List;

public class RoomResourceExample {
	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));

		RoomResources roomResource = account.roomResources();
		List<RoomResource> roomResourceList = roomResource.list();
		for(RoomResource resource : roomResourceList) {
			System.out.println(resource);
		}
	}
}
