package com.nylas;

import java.util.Map;

public abstract class AccountOwnedModel extends RestfulModel {

	private String account_id;

	public String getAccountId() {
		return account_id;
	}

	/**
	 * Converts the model to a JSON string
	 * @return JSON string representation of the object
	 */
	public String toJSON() {
		return JsonHelper.objectToJson(AccountOwnedModel.class, this);
	}

	/**
	 * Converts the model to a Map
	 * @return Map representation of the object
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ? extends AccountOwnedModel> toMap() {
		return (Map<String, ? extends AccountOwnedModel>) JsonHelper.adapter(AccountOwnedModel.class).toJsonValue(this);
	}
	
}
