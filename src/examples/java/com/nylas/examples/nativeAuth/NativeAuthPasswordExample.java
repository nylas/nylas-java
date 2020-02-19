package com.nylas.examples.nativeAuth;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nylas.AccessToken;
import com.nylas.AccountDetail;
import com.nylas.ImapProviderSettings;
import com.nylas.NativeAuthentication;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.examples.ExampleConf;
import com.nylas.NylasAccount;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.ProviderSettings;
import com.nylas.RequestFailedException;
import com.nylas.Scope;

public class NativeAuthPasswordExample {
	
	private static final Logger log = LogManager.getLogger(NativeAuthPasswordExample.class);

	public static void main(String[] args) throws IOException, RequestFailedException {
		ExampleConf conf = new ExampleConf();

		String provider = conf.get("native.auth.provider");
		Map<String, String> providerConf = conf.getPrefixedEntries("native.auth." + provider + ".");
		
		String name = providerConf.get("name");
		String email = providerConf.get("email");
		String password = providerConf.get("password");
		ProviderSettings settings;
		switch(provider) {
		case "aol":
		case "hotmail":
		case "icloud":
		case "outlook":
		case "yahoo":
			settings = knownImap(provider, email, password);
			break;
		case "exchange":
			settings = exchange(providerConf, password, email);
			break;
		case "imap":
			settings = imap(providerConf);
			break;
		case "gmail":
		case "google":
		case "office365":
			throw new RuntimeException("This example does not support OAuth flows");
		default:
			throw new RuntimeException("Unsupported native.auth.provider: " + provider);
		}
		
		nativeAuth(conf, name, email, settings);
	}
	
	public static ProviderSettings exchange(Map<String, String> conf, String email, String password)
			throws IOException, RequestFailedException {
		return ProviderSettings.exchange()
				.username(email)
				.password(password)
				.easServerHost(conf.get("native.auth.exchange.server"))
				;
	}
	
	public static ProviderSettings imap(Map<String, String> conf)
			throws IOException, RequestFailedException {
		ImapProviderSettings settings = ProviderSettings.imap()
				.imapHost(conf.get("host"))
				.imapUsername(conf.get("username"))
				.imapPassword(conf.get("password"))
				.smtpHost(conf.get("smtp.host"))
				.smtpUsername(conf.get("smtp.username"))
				.smtpPassword(conf.get("smtp.password"))
				.sslRequired(true)
				;
		if (conf.containsKey("port")) {
			settings.imapPort(Integer.parseInt(conf.get("port")));
		}
		if (conf.containsKey("smtp.port")) {
			settings.smtpPort(Integer.parseInt(conf.get("smtp.port")));
		}
		if (conf.containsKey("ssl.required")) {
			settings.sslRequired(Boolean.parseBoolean(conf.get("ssl.required")));
		}
		return settings;
	}
	
	public static ProviderSettings knownImap(String provider, String email, String password)
			throws IOException, RequestFailedException {
		return ProviderSettings.knownImap(provider)
				.username(email)
				.password(password)
				;
	}
	
	public static void nativeAuth(ExampleConf conf, String name, String email, ProviderSettings provider)
			throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		NativeAuthentication authentication = application.nativeAuthentication();
		AuthRequestBuilder authRequest = authentication.authRequest()
				.name(name)
				.emailAddress(email)
				.providerSettings(provider)
				.scopes(Scope.values());
				
		log.info("Making a native authentication request for " + email);
		String authorizationCode = authRequest.execute();
		log.info("Succeeded.  Returned authorization code: " + authorizationCode);
		
		log.info("Using authorization code to request long lived access token.");
		AccessToken token = authentication.fetchToken(authorizationCode);
		log.info("Succeeded.  Returned token: " + token);
		
		log.info("Requesting account details with token.");
		NylasAccount account = client.account(token.getAccessToken());
		AccountDetail accountDetail = account.fetchAccountByAccessToken();
		log.info("Succeeded.  Account details: " + accountDetail);
	}

}
