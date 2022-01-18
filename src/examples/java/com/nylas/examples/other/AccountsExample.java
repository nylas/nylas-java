package com.nylas.examples.other;

import java.util.List;

import com.nylas.Account;
import com.nylas.AccountQuery;
import com.nylas.Accounts;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.TokenInfo;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountsExample {

	private static final Logger log = LogManager.getLogger(AccountsExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		Accounts accounts = application.accounts();
		AccountQuery query = new AccountQuery()
				.limit(2)
				//.offset(10)
				;
		List<Account> accountList = accounts.list(query).fetchAll();
		for (Account account : accountList) {
			log.info(account);
		}
		
		Account first = accounts.get(accountList.get(0).getId());
		log.info("first: " + first);
		
		String accessToken = conf.get("access.token");
		TokenInfo tokenInfo = accounts.tokenInfo(first.getId(), accessToken);
		log.info("token info: " + tokenInfo);
		
		accounts.downgrade(first.getId());
		first = accounts.get(accountList.get(0).getId());
		log.info("after downgrade: " + first);
		
		accounts.upgrade(first.getId());
		first = accounts.get(accountList.get(0).getId());
		log.info("after upgrade: " + first);
		
		//accounts.delete(first.getId());
		//accounts.revokeAllTokensForAccount(first.getId(), "blahblah");
	}

}
