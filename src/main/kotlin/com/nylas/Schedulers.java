package com.nylas;

import com.nylas.scheduler.*;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schedulers extends RestfulDAO<Scheduler> {

	private static final String SCHEDULER_API_BASE_URL = "https://api.schedule.nylas.com/";
	private static final String SCHEDULER_API_EU_BASE_URL = "https://ireland.api.schedule.nylas.com/";
	private final String baseUrl;

	Schedulers(NylasClient client, String accessToken) {
		super(client, Scheduler.class, "manage/pages", accessToken);

		// Set the Scheduler URL based on the URL set in the client
		switch (client.newUrlBuilder().toString()) {
			case NylasClient.EU_BASE_URL:
				baseUrl = SCHEDULER_API_EU_BASE_URL;
				break;
			case NylasClient.DEFAULT_BASE_URL:
			default:
				baseUrl = SCHEDULER_API_BASE_URL;
				break;
		}
	}

	protected HttpUrl.Builder getCollectionUrl() {
		return HttpUrl.get(baseUrl).newBuilder().addPathSegments(collectionPath);
	}

	public RemoteCollection<Scheduler> list() throws IOException, RequestFailedException {
		return list(new SchedulerQuery());
	}

	public RemoteCollection<Scheduler> list(SchedulerQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Scheduler get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	public Scheduler create(Scheduler scheduler) throws IOException, RequestFailedException {
		return super.create(scheduler);
	}

	public Scheduler update(Scheduler scheduler) throws IOException, RequestFailedException {
		return super.update(scheduler);
	}

	/**
	 * Delete the scheduler with the given id.
	 * Returns the id of job status for the deletion.
	 */
	public String delete(String id) throws IOException, RequestFailedException {
		return super.delete(id);
	}

	/**
	 * Convenience method to create or update the given scheduler.
	 * If it has an existing ID, update it, otherwise create it.
	 * Returns the scheduler as returned by the server.
	 */
	public Scheduler save(Scheduler scheduler) throws IOException, RequestFailedException {
		return scheduler.hasId() ? update(scheduler) : create(scheduler);
	}

	public List<AvailableCalendars> getAvailableCalendars(String schedulerId)
			throws RequestFailedException, IOException {
		HttpUrl.Builder url = getInstanceUrl(schedulerId).addPathSegment("calendars");
		Type listType = JsonHelper.listTypeOf(AvailableCalendars.class);
		return client.executeGet(authUser, url, listType);
	}

	public UploadImageResponse uploadImage(String schedulerId, String contentType, String objectName)
			throws RequestFailedException, IOException {
		Map<String, Object> params = new HashMap<>();
		params.put("contentType", contentType);
		params.put("objectName", objectName);
		HttpUrl.Builder url = getInstanceUrl(schedulerId).addPathSegment("upload-image");
		return client.executePut(authUser, url, params, UploadImageResponse.class);
	}

	public List<ProviderAvailability> getGoogleAvailability()
			throws RequestFailedException, IOException {
		return getProviderAvailability("google");
	}

	public List<ProviderAvailability> getOffice365Availability()
			throws RequestFailedException, IOException {
		return getProviderAvailability("o365");
	}

	public Scheduler getPageBySlug(String slug) throws RequestFailedException, IOException {
		HttpUrl.Builder url = schedulerUrl()
				.addPathSegment(slug)
				.addPathSegment("info");
		return client.executeGet(authUser, url, Scheduler.class);
	}

	public List<TimeSlot> getAvailableTimeSlots(String slug) throws RequestFailedException, IOException {
		HttpUrl.Builder url = schedulerUrl()
				.addPathSegment(slug)
				.addPathSegment("timeslots");
		Type listType = JsonHelper.listTypeOf(TimeSlot.class);
		return client.executeGet(authUser, url, listType);
	}

	public BookingConfirmation bookTimeSlot(String slug, BookingRequest bookingRequest)
			throws RequestFailedException, IOException {
		HttpUrl.Builder url = schedulerUrl()
				.addPathSegment(slug)
				.addPathSegment("timeslots");
		return client.executePost(authUser, url, bookingRequest.getWritableFields(false), BookingConfirmation.class);
	}

	public boolean cancelBooking(String slug, String editHash, String reason)
			throws RequestFailedException, IOException {
		Map<String, Object> params = new HashMap<>();
		params.put("reason", reason);
		HttpUrl.Builder url = schedulerUrl()
				.addPathSegment(slug)
				.addPathSegment(editHash)
				.addPathSegment("cancel");
		Map<String, Object> response = client.executePost(authUser, url, params, Map.class);
		if(response.containsKey("success")) {
			return response.get("success") instanceof Boolean ?
					(Boolean) response.get("success") :
					Boolean.parseBoolean((String) response.get("success"));
		}

		return false;
	}

	public BookingConfirmation confirmBooking(String slug, String editHash)
			throws RequestFailedException, IOException {
		HttpUrl.Builder url = schedulerUrl()
				.addPathSegment(slug)
				.addPathSegment(editHash)
				.addPathSegment("confirm");
		return client.executePost(authUser, url, Collections.emptyMap(), BookingConfirmation.class);
	}

	private List<ProviderAvailability> getProviderAvailability(String provider)
			throws RequestFailedException, IOException {
		HttpUrl.Builder url = schedulerUrl()
				.addPathSegment("availability")
				.addPathSegment(provider);
		Type listType = JsonHelper.listTypeOf(ProviderAvailability.class);
		return client.executeGet(authUser, url, listType);
	}

	private HttpUrl.Builder schedulerUrl() {
		return HttpUrl.get(baseUrl).newBuilder().addPathSegment("schedule");
	}
}
