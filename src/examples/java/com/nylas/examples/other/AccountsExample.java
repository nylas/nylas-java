package com.nylas.examples.other;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nylas.Account;
import com.nylas.AccountQuery;
import com.nylas.Accounts;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.TokenInfo;
import com.nylas.examples.ExampleConf;

public class AccountsExample {

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
			System.out.println(account);
		}
		
		Account first = accounts.get(accountList.get(0).getId());
		System.out.println("first: " + first);
		
		String accessToken = conf.get("access.token");
		TokenInfo tokenInfo = accounts.tokenInfo(first.getId(), accessToken);
		System.out.println("token info: " + tokenInfo);
		
		accounts.downgrade(first.getId());
		first = accounts.get(accountList.get(0).getId());
		System.out.println("after downgrade: " + first);

		accounts.upgrade(first.getId());
		first = accounts.get(accountList.get(0).getId());
		System.out.println("after upgrade: " + first);

		Map<String, String> metadata = new HashMap<>();
		metadata.put("account_type", "test");
		accounts.setMetadata(accountList.get(0).getId(), metadata);
		
		//accounts.delete(first.getId());
		//accounts.revokeAllTokensForAccount(first.getId(), "blahblah");
	}

}
