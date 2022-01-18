package com.nylas.examples.other;

import com.nylas.AccountDetail;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountDetailExample {

	private static final Logger log = LogManager.getLogger(AccountDetailExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		AccountDetail detail = account.fetchAccountByAccessToken();
		log.info(detail);
	}

}
