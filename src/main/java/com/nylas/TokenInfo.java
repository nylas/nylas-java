package com.nylas;

import java.time.Instant;

public class TokenInfo {

	private Long created_at;
	private Long updated_at;
	private String scopes;
	private String state;
	
	public Instant getCreatedAt() {
		return Instants.toNullableInstant(created_at);
	}
	
	public Instant getUpdatedAt() {
		return Instants.toNullableInstant(updated_at);
	}
	
	public String getScopes() {
		return scopes;
	}
	
	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		return "TokenInfo [created_at=" + getCreatedAt() + ", updated_at=" + getUpdatedAt() + ", scopes=" + scopes
				+ ", state=" + state + "]";
	}

}
