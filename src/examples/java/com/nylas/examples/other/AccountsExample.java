package com.nylas.examples.other;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nylas.*;
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

		Map<String, String> metadata = new HashMap<>();
		metadata.put("account_type", "test");
		accounts.setMetadata(accountList.get(0).getId(), metadata);

		MetadataQuery metadataQuery = new MetadataQuery().metadataKey("account_type");
		AccountQuery accountQuery = new AccountQuery().metadataQuery(metadataQuery);
		List<Account> accountsWithMetadata = accounts.list(accountQuery).fetchAll();
		for (Account account : accountsWithMetadata) {
			log.info("found account with 'account_type' metadata: " + account);
		}
		
		//accounts.delete(first.getId());
		//accounts.revokeAllTokensForAccount(first.getId(), "blahblah");
		//accounts.revoke(conf.get("access.token"));
	}

}
