package com.nylas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Drafts extends RestfulCollection<Draft, DraftQuery> {

	Drafts(NylasClient client, String accessToken) {
		super(client, Draft.class, "drafts", accessToken);
	}
	

	public Draft put(Draft draft) throws IOException, RequestFailedException {
		Map<String, Object> params = new HashMap<>();
		List<String> file_ids = draft.getFiles().stream().map(f -> f.getId()).collect(Collectors.toList());
		
		List<String> label_ids = draft.getLabels().stream().map(f -> f.getId()).collect(Collectors.toList());

		Tracking tracking = new Tracking();
		tracking.setLinks(true);
		tracking.setOpens(true);
		tracking.setThreadReplies(false);
		tracking.setPayload("tracking_payload");
		
		params.put("reply_to_message_id", draft.getReplyToMessageId());
		params.put("version", draft.getVersion());
		return super.put(draft.getId(),params);
	}

}


/*
 *  account_id ignored
 *  thread_id ignored
 *  subject worked
 *  from failed (but probably due to gmail)
 *  to, cc, bcc worked
 *  date failed
 *  unread, starred failed
 *  snippet failed
 *  body worked
 *  file_ids worked
 *  reply_to_message_id failed
 *  label_ids failed
 */
