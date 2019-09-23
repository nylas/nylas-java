package com.nylas;

public class TokenInfo {

	private Long created_at;
	private Long updated_at;
	private String scopes;
	private String state;
	
	public Long getCreatedAt() {
		return created_at;
	}
	
	public Long getUpdatedAt() {
		return updated_at;
	}
	
	public String getScopes() {
		return scopes;
	}
	
	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		return "TokenInfo [created_at=" + created_at + ", updated_at=" + updated_at + ", scopes=" + scopes + ", state="
				+ state + "]";
	}

}
