package com.nylas.examples.other;

import com.nylas.*;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class OutboxExample {

	private static final Logger log = LogManager.getLogger(OutboxExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Outbox outbox = account.outbox();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date dayAfter = calendar.getTime();

		OutboxMessage outboxMessage = new OutboxMessage();
		outboxMessage.setTo(Arrays.asList(new NameEmail("dude", "dude@b.com")));
		outboxMessage.setBody("this is the draft body text");
		outboxMessage.setSubject("I wonder if this draft will show up in gmail");
		outboxMessage.setSendAt(tomorrow);
		outboxMessage.setRetryLimitDatetime(dayAfter);
		OutboxJobStatus jobStatus = outbox.send(outboxMessage);
		log.info("Message sent to Outbox, job status: " + jobStatus);

		calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 2);
		Date newSendAt = calendar.getTime();
		outboxMessage = jobStatus.getOriginalData();
		outboxMessage.setSendAt(newSendAt);
		jobStatus = outbox.update(outboxMessage, jobStatus.getJobStatusId());
		log.info("Outbox message updated, job status: " + jobStatus);

		outbox.delete(jobStatus.getJobStatusId());
		log.info("Outbox job deleted.");
	}
}
