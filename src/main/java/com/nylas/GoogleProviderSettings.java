package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provider settings for Google.
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 * <a href="https://docs.nylas.com/docs/native-authentication-setup-for-google-oauth">
 * https://docs.nylas.com/docs/native-authentication-setup-for-google-oauth</a>
 */
public class GoogleProviderSettings extends ProviderSettings {

	private String googleClientId;
	private String googleClientSecret;
	private String googleRefreshToken;
	
	public GoogleProviderSettings() {
		super("gmail");
	}
	
	public String getGoogleClientId() {
		return googleClientId;
	}

	public String getGoogleClientSecret() {
		return googleClientSecret;
	}

	public String getGoogleRefreshToken() {
		return googleRefreshToken;
	}

	public GoogleProviderSettings googleClientId(String googleClientId) {
		this.googleClientId = googleClientId;
		return this;
	}
	
	public GoogleProviderSettings googleClientSecret(String googleClientSecret) {
		this.googleClientSecret = googleClientSecret;
		return this;
	}
	
	public GoogleProviderSettings googleRefreshToken(String googleRefreshToken) {
		this.googleRefreshToken = googleRefreshToken;
		return this;
	}

	private void validate() {
		assertState(!nullOrEmpty(googleClientId), "Google Client ID is required");
		assertState(!nullOrEmpty(googleClientSecret), "Google Client Secret is required");
		assertState(!nullOrEmpty(googleRefreshToken), "Google Refresh Token is required");
	}
	
	@Override
	protected void fillSettings(Map<String, Object> settings) {
		validate();
		
		settings.put("google_client_id", googleClientId);
		settings.put("google_client_secret", googleClientSecret);
		settings.put("google_refresh_token", googleRefreshToken);
	}
	
	private static final Map<Scope, List<String>> matchingScopes;
	private static void addMapping(Scope nylasScope, String googleScope) {
		matchingScopes.computeIfAbsent(nylasScope, s -> new ArrayList<>()).add(googleScope);
	}
	static {
		matchingScopes = new EnumMap<>(Scope.class);
		addMapping(Scope.EMAIL_MODIFY, "https://www.googleapis.com/auth/gmail.modify");
		addMapping(Scope.EMAIL_READ_ONLY, "https://www.googleapis.com/auth/gmail.readonly");
		addMapping(Scope.EMAIL_SEND, "https://www.googleapis.com/auth/gmail.compose");
		addMapping(Scope.EMAIL_SEND, "https://www.googleapis.com/auth/gmail.modify");
		addMapping(Scope.EMAIL_SEND, "https://www.googleapis.com/auth/gmail.send");
		addMapping(Scope.EMAIL_FOLDERS_AND_LABELS, "https://www.googleapis.com/auth/gmail.labels");
		addMapping(Scope.EMAIL_METADATA, "https://www.googleapis.com/auth/gmail.metadata");
		addMapping(Scope.EMAIL_DRAFTS, "https://www.googleapis.com/auth/gmail.compose");
		addMapping(Scope.CALENDAR, "https://www.googleapis.com/auth/calendar");
		addMapping(Scope.CALENDAR_READ_ONLY, "https://www.googleapis.com/auth/calendar.readonly");
		addMapping(Scope.ROOM_RESOURCES_READ_ONLY, "https://www.googleapis.com/auth/admin.directory.resource.calendar.readonly");
		addMapping(Scope.CONTACTS, "https://www.googleapis.com/auth/contacts");
		addMapping(Scope.CONTACTS_READ_ONLY, "https://www.googleapis.com/auth/contacts.readonly");
	}
	
	/**
	 * Convenience method to lookup the matching Google OAuth scopes for a given set of Nylas scopes.
	 * <p>
	 * Note - Nylas also currently requires access to email during token verification, so you need to also
	 * add the scope of "https://www.googleapis.com/auth/userinfo.email" or just "email"
	 */
	public static Set<String> getMatchingGoogleScopes(Collection<Scope> nylasScopes) {
		Set<String> googleScopes = new HashSet<>();
		for (Scope scope : nylasScopes) {
			googleScopes.addAll(matchingScopes.get(scope));
		}
		return googleScopes;
	}
	
}
