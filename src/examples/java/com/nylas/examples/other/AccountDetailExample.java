package com.nylas.examples.other;

import com.nylas.AccountDetail;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;

public class AccountDetailExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		AccountDetail detail = account.fetchAccountByAccessToken();
		System.out.println(detail);
	}

}
