package com.nylas.examples;

import java.util.List;
import java.util.Properties;

import com.nylas.Draft;
import com.nylas.DraftQuery;
import com.nylas.Drafts;
import com.nylas.NylasClient;

public class DraftsExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Drafts drafts = client.drafts(accessToken);
		
		DraftQuery query = new DraftQuery()
				//.limit(1)
				//.offset(1)
				//.anyEmail("info@twitter.com")
				;
		
		List<Draft> allDrafts = drafts.list(query);
		Draft firstDraft = allDrafts.get(0);
		for (Draft draft : allDrafts) {
			System.out.println(draft);
		}
		
		firstDraft = drafts.get(firstDraft.getId());
		System.out.println("first = " + firstDraft);
	}

}
