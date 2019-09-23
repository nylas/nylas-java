package com.nylas;

public class Drafts extends RestfulCollection<Draft, DraftQuery> {

	Drafts(NylasClient client, String accessToken) {
		super(client, Draft.class, "drafts", accessToken);
	}

}
