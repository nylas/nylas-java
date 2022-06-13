package com.nylas.scheduler;

import com.nylas.AccountOwnedModel;
import com.nylas.JsonObject;
import com.nylas.Maps;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Scheduler extends AccountOwnedModel implements JsonObject {

	private final transient SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

	private String app_client_id;
	private String edit_token;
	private String name;
	private String slug;
	private String created_at;
	private String modified_at;
	private Integer app_organization_id;
	private Config config;
	private List<String> access_tokens = new ArrayList<>();

	public String getAppClientId() {
		return app_client_id;
	}

	public String getEditToken() {
		return edit_token;
	}

	public String getName() {
		return name;
	}

	public String getSlug() {
		return slug;
	}

	public String getCreatedAt() {
		return created_at;
	}

	public String getModifiedAt() {
		return modified_at;
	}

	public Integer getAppOrganizationId() {
		return app_organization_id;
	}

	public Config getConfig() {
		return config;
	}

	public List<String> getAccessTokens() {
		return access_tokens;
	}

	public void setAppClientId(String appClientId) {
		this.app_client_id = appClientId;
	}

	public void setEditToken(String editToken) {
		this.edit_token = editToken;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setCreatedAt(String createdAt) {
		this.created_at = createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.created_at = DATE_FORMATTER.format(createdAt);
	}

	public void setModifiedAt(String modifiedAt) {
		this.modified_at = modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modified_at = DATE_FORMATTER.format(modifiedAt);
	}

	public void setAppOrganizationId(Integer appOrganizationId) {
		this.app_organization_id = appOrganizationId;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setAccessTokens(List<String> accessTokens) {
		this.access_tokens = accessTokens;
	}

	public boolean addAccessTokens(String... accessTokens) {
		return Collections.addAll(this.access_tokens, accessTokens);
	}

	@Override
	public String getObjectType() {
		return "scheduler";
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();

		if (creation) {
			Maps.putIfNotNull(params, "access_tokens", access_tokens);
		}

		Maps.putIfNotNull(params, "app_client_id", app_client_id);
		Maps.putIfNotNull(params, "edit_token", edit_token);
		Maps.putIfNotNull(params, "name", name);
		Maps.putIfNotNull(params, "slug", slug);
		Maps.putIfNotNull(params, "app_organization_id", app_organization_id);
		Maps.putIfNotNull(params, "created_at", created_at);
		Maps.putIfNotNull(params, "modified_at", modified_at);
		Maps.putIfNotNull(params, "config", config);
		return params;
	}

	@Override
	public String toString() {
		return "Scheduler [" +
				"appClientId='" + app_client_id + '\'' +
				", editToken='" + edit_token + '\'' +
				", name='" + name + '\'' +
				", slug='" + slug + '\'' +
				", accessTokens=" + access_tokens +
				", appOrganizationId=" + app_organization_id +
				", createdAt=" + created_at +
				", modifiedAt=" + modified_at +
				", config=" + config +
				']';
	}

	public static class Config {

		private String locale;
		private String locale_for_guests;
		private String timezone;
		private Boolean disable_emails;
		private Appearance appearance;
		private Booking booking;
		private Event event;
		private ExpireAfter expire_after;
		private List<Reminders> reminders = new ArrayList<>();
		private Map<String, Calendar> calendar_ids;

		public String getLocale() {
			return locale;
		}

		public String getLocaleForGuests() {
			return locale_for_guests;
		}

		public String getTimezone() {
			return timezone;
		}

		public Boolean getDisableEmails() {
			return disable_emails;
		}

		public Appearance getAppearance() {
			return appearance;
		}

		public Booking getBooking() {
			return booking;
		}

		public Event getEvent() {
			return event;
		}

		public ExpireAfter getExpireAfter() {
			return expire_after;
		}

		public List<Reminders> getReminders() {
			return reminders;
		}

		public Map<String, Calendar> getCalendarIds() {
			return calendar_ids;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public void setLocaleForGuests(String localeForGuests) {
			this.locale_for_guests = localeForGuests;
		}

		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}

		public void setDisableEmails(Boolean disableEmails) {
			this.disable_emails = disableEmails;
		}

		public void setAppearance(Appearance appearance) {
			this.appearance = appearance;
		}

		public void setBooking(Booking booking) {
			this.booking = booking;
		}

		public void setEvent(Event event) {
			this.event = event;
		}

		public void setExpireAfter(ExpireAfter expireAfter) {
			this.expire_after = expireAfter;
		}

		public void setReminders(List<Reminders> reminders) {
			this.reminders = reminders;
		}

		public void setCalendarIds(Map<String, Calendar> calendarIds) {
			this.calendar_ids = calendarIds;
		}

		public boolean addReminders(Reminders... reminders) {
			return Collections.addAll(this.reminders, reminders);
		}

		public boolean addCalendar(String calendarId, Calendar calendar) {
			if(this.calendar_ids.containsKey(calendarId)) {
				return false;
			}
			this.calendar_ids.put(calendarId, calendar);
			return true;
		}

		@Override
		public String toString() {
			return "Config [" +
					"locale='" + locale + '\'' +
					", localeForGuests='" + locale_for_guests + '\'' +
					", timezone='" + timezone + '\'' +
					", disableEmails=" + disable_emails +
					", appearance=" + appearance +
					", booking=" + booking +
					", calendarIds=" + calendar_ids +
					", event=" + event +
					", expireAfter=" + expire_after +
					", reminders=" + reminders +
					']';
		}

		public static class Appearance {

			private String color;
			private String company_name;
			private String logo;
			private String privacy_policy_redirect;
			private String submit_text;
			private String thank_you_redirect;
			private String thank_you_text;
			private String thank_you_text_secondary;
			private Boolean show_autoschedule;
			private Boolean show_nylas_branding;
			private Boolean show_timezone_options;
			private Boolean show_week_view;

			public String getColor() {
				return color;
			}

			public String getCompanyName() {
				return company_name;
			}

			public String getLogo() {
				return logo;
			}

			public String getPrivacyPolicyRedirect() {
				return privacy_policy_redirect;
			}

			public String getSubmitText() {
				return submit_text;
			}

			public String getThankYouRedirect() {
				return thank_you_redirect;
			}

			public String getThankYouText() {
				return thank_you_text;
			}

			public String getThankYouTextSecondary() {
				return thank_you_text_secondary;
			}

			public Boolean getShowAutoschedule() {
				return show_autoschedule;
			}

			public Boolean getShowNylasBranding() {
				return show_nylas_branding;
			}

			public Boolean getShowTimezoneOptions() {
				return show_timezone_options;
			}

			public Boolean getShowWeekView() {
				return show_week_view;
			}

			public void setColor(String color) {
				this.color = color;
			}

			public void setCompanyName(String companyName) {
				this.company_name = companyName;
			}

			public void setLogo(String logo) {
				this.logo = logo;
			}

			public void setPrivacyPolicyRedirect(String privacyPolicyRedirect) {
				this.privacy_policy_redirect = privacyPolicyRedirect;
			}

			public void setSubmitText(String submitText) {
				this.submit_text = submitText;
			}

			public void setThankYouRedirect(String thankYouRedirect) {
				this.thank_you_redirect = thankYouRedirect;
			}

			public void setThankYouText(String thankYouText) {
				this.thank_you_text = thankYouText;
			}

			public void setThankYouTextSecondary(String thankYouTextSecondary) {
				this.thank_you_text_secondary = thankYouTextSecondary;
			}

			public void setShowAutoschedule(Boolean showAutoschedule) {
				this.show_autoschedule = showAutoschedule;
			}

			public void setShowNylasBranding(Boolean showNylasBranding) {
				this.show_nylas_branding = showNylasBranding;
			}

			public void setShowTimezoneOptions(Boolean showTimezoneOptions) {
				this.show_timezone_options = showTimezoneOptions;
			}

			public void setShowWeekView(Boolean showWeekView) {
				this.show_week_view = showWeekView;
			}

			@Override
			public String toString() {
				return "Appearance [" +
						"color='" + color + '\'' +
						", companyName='" + company_name + '\'' +
						", logo='" + logo + '\'' +
						", privacyPolicyRedirect='" + privacy_policy_redirect + '\'' +
						", submitText='" + submit_text + '\'' +
						", thankYouRedirect='" + thank_you_redirect + '\'' +
						", thankYouText='" + thank_you_text + '\'' +
						", thankYouTextSecondary='" + thank_you_text_secondary + '\'' +
						", showAutoschedule=" + show_autoschedule +
						", showNylasBranding=" + show_nylas_branding +
						", showTimezoneOptions=" + show_timezone_options +
						", showWeekView=" + show_week_view +
						']';
			}
		}

		public static class Booking {

			private String scheduling_method;
			private String cancellation_policy;
			private String confirmation_method;
			private Integer available_days_in_future;
			private Integer min_booking_notice;
			private Integer min_buffer;
			private Integer min_cancellation_notice;
			private Boolean calendar_invite_to_guests;
			private Boolean confirmation_emails_to_guests;
			private Boolean confirmation_emails_to_host;
			private Boolean name_field_hidden;
			private Boolean additional_guests_hidden;
			private List<AdditionalFields> additional_fields = new ArrayList<>();
			private List<OpeningHours> opening_hours = new ArrayList<>();

			public String getSchedulingMethod() {
				return scheduling_method;
			}

			public String getCancellationPolicy() {
				return cancellation_policy;
			}

			public String getConfirmationMethod() {
				return confirmation_method;
			}

			public Integer getAvailableDaysInFuture() {
				return available_days_in_future;
			}

			public Integer getMinBookingNotice() {
				return min_booking_notice;
			}

			public Integer getMinBuffer() {
				return min_buffer;
			}

			public Integer getMinCancellationNotice() {
				return min_cancellation_notice;
			}

			public Boolean getCalendarInviteToGuests() {
				return calendar_invite_to_guests;
			}

			public Boolean getConfirmationEmailsToGuests() {
				return confirmation_emails_to_guests;
			}

			public Boolean getConfirmationEmailsToHost() {
				return confirmation_emails_to_host;
			}

			public Boolean getNameFieldHidden() {
				return name_field_hidden;
			}

			public Boolean getAdditionalGuestsHidden() {
				return additional_guests_hidden;
			}

			public List<AdditionalFields> getAdditionalFields() {
				return additional_fields;
			}

			public List<OpeningHours> getOpeningHours() {
				return opening_hours;
			}

			public void setSchedulingMethod(String schedulingMethod) {
				this.scheduling_method = schedulingMethod;
			}

			public void setCancellationPolicy(String cancellationPolicy) {
				this.cancellation_policy = cancellationPolicy;
			}

			public void setConfirmationMethod(String confirmationMethod) {
				this.confirmation_method = confirmationMethod;
			}

			public void setAvailableDaysInFuture(Integer availableDaysInFuture) {
				this.available_days_in_future = availableDaysInFuture;
			}

			public void setMinBookingNotice(Integer minBookingNotice) {
				this.min_booking_notice = minBookingNotice;
			}

			public void setMinBuffer(Integer minBuffer) {
				this.min_buffer = minBuffer;
			}

			public void setMinCancellationNotice(Integer minCancellationNotice) {
				this.min_cancellation_notice = minCancellationNotice;
			}

			public void setCalendarInviteToGuests(Boolean calendarInviteToGuests) {
				this.calendar_invite_to_guests = calendarInviteToGuests;
			}

			public void setConfirmationEmailsToGuests(Boolean confirmationEmailsToGuests) {
				this.confirmation_emails_to_guests = confirmationEmailsToGuests;
			}

			public void setConfirmationEmailsToHost(Boolean confirmationEmailsToHost) {
				this.confirmation_emails_to_host = confirmationEmailsToHost;
			}

			public void setNameFieldHidden(Boolean nameFieldHidden) {
				this.name_field_hidden = nameFieldHidden;
			}

			public void setAdditionalGuestsHidden(Boolean additionalGuestsHidden) {
				this.additional_guests_hidden = additionalGuestsHidden;
			}

			public void setAdditionalFields(List<AdditionalFields> additionalFields) {
				this.additional_fields = additionalFields;
			}

			public void setOpeningHours(List<OpeningHours> openingHours) {
				this.opening_hours = openingHours;
			}

			public boolean addAdditionalFields(AdditionalFields... additionalFields) {
				return Collections.addAll(this.additional_fields, additionalFields);
			}

			public boolean addOpeningHours(OpeningHours... openingHours) {
				return Collections.addAll(this.opening_hours, openingHours);
			}

			@Override
			public String toString() {
				return "Booking [" +
						"schedulingMethod='" + scheduling_method + '\'' +
						", cancellationPolicy='" + cancellation_policy + '\'' +
						", confirmationMethod='" + confirmation_method + '\'' +
						", availableDaysInFuture=" + available_days_in_future +
						", minBookingNotice=" + min_booking_notice +
						", minBuffer=" + min_buffer +
						", minCancellationNotice=" + min_cancellation_notice +
						", calendarInviteToGuests=" + calendar_invite_to_guests +
						", confirmationEmailsToGuests=" + confirmation_emails_to_guests +
						", confirmationEmailsToHost=" + confirmation_emails_to_host +
						", nameFieldHidden=" + name_field_hidden +
						", additionalGuestsHidden=" + additional_guests_hidden +
						", additionalFields=" + additional_fields +
						", openingHours=" + opening_hours +
						']';
			}

			public static class AdditionalFields {

				private String label;
				private String name;
				private String pattern;
				private String type;
				private Integer order;
				private Boolean required;
				private List<String> dropdown_options = new ArrayList<>();
				private List<String> multi_select_options = new ArrayList<>();

				public String getLabel() {
					return label;
				}

				public String getName() {
					return name;
				}

				public String getPattern() {
					return pattern;
				}

				public String getType() {
					return type;
				}

				public Integer getOrder() {
					return order;
				}

				public Boolean getRequired() {
					return required;
				}

				public List<String> getDropdownOptions() {
					return dropdown_options;
				}

				public List<String> getMultiSelectOptions() {
					return multi_select_options;
				}

				public void setLabel(String label) {
					this.label = label;
				}

				public void setName(String name) {
					this.name = name;
				}

				public void setPattern(String pattern) {
					this.pattern = pattern;
				}

				public void setType(String type) {
					this.type = type;
				}

				public void setOrder(Integer order) {
					this.order = order;
				}

				public void setRequired(Boolean required) {
					this.required = required;
				}

				public void setDropdownOptions(List<String> dropdownOptions) {
					this.dropdown_options = dropdownOptions;
				}

				public void setMultiSelectOptions(List<String> multiSelectOptions) {
					this.multi_select_options = multiSelectOptions;
				}

				public boolean addDropdownOptions(String... dropdownOptions) {
					return Collections.addAll(this.dropdown_options, dropdownOptions);
				}

				public boolean addMultiSelectOptions(String... multiSelectOptions) {
					return Collections.addAll(this.multi_select_options, multiSelectOptions);
				}

				@Override
				public String toString() {
					return "AdditionalFields [" +
							"label='" + label + '\'' +
							", name='" + name + '\'' +
							", pattern='" + pattern + '\'' +
							", type='" + type + '\'' +
							", dropdownOptions=" + dropdown_options +
							", multiSelectOptions=" + multi_select_options +
							", order=" + order +
							", required=" + required +
							']';
				}
			}

			public static class OpeningHours {
				/**
				 * Enumeration containing the different days accepted by the API
				 */
				public enum Days {
					MONDAY('M'),
					TUESDAY('T'),
					WEDNESDAY('W'),
					THURSDAY('R'),
					FRIDAY('F'),
					SATURDAY('S'),
					SUNDAY('U'),

					;

					private final Character day;

					Days(Character day) {
						this.day = day;
					}

					public Character getDay() {
						return day;
					}
				}

				private String account_id;
				private String end;
				private String start;
				private List<Character> days = new ArrayList<>();

				public String getAccountId() {
					return account_id;
				}

				public String getEnd() {
					return end;
				}

				public String getStart() {
					return start;
				}

				public List<Character> getDays() {
					return days;
				}

				public void setAccountId(String accountId) {
					this.account_id = accountId;
				}

				public void setEnd(String end) {
					this.end = end;
				}

				public void setStart(String start) {
					this.start = start;
				}

				public void setDays(List<Character> days) {
					this.days = days;
				}

				public boolean addDays(Days... days) {
					List<Character> dayList = Arrays.stream(days).map(Days::getDay).collect(Collectors.toList());
					return this.days.addAll(dayList);
				}

				@Override
				public String toString() {
					return "OpeningHours [" +
							"accountId='" + account_id + '\'' +
							", end='" + end + '\'' +
							", start='" + start + '\'' +
							", days=" + days +
							']';
				}
			}
		}

		public static class Calendar {
			private String booking;
			private List<String> availability = new ArrayList<>();

			public String getBooking() {
				return booking;
			}

			public List<String> getAvailability() {
				return availability;
			}

			public void setBooking(String booking) {
				this.booking = booking;
			}

			public void setAvailability(List<String> availability) {
				this.availability = availability;
			}

			public boolean addAvailability(String... availability) {
				return Collections.addAll(this.availability, availability);
			}

			@Override
			public String toString() {
				return "Calendar [" +
						"booking='" + booking + '\'' +
						", availability=" + availability +
						']';
			}
		}

		public static class Event {
			private String location;
			private String title;
			private Integer duration;
			private Integer capacity;

			public String getLocation() {
				return location;
			}

			public String getTitle() {
				return title;
			}

			public Integer getDuration() {
				return duration;
			}

			public Integer getCapacity() {
				return capacity;
			}

			public void setLocation(String location) {
				this.location = location;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public void setDuration(Integer duration) {
				this.duration = duration;
			}

			public void setCapacity(Integer capacity) {
				this.capacity = capacity;
			}

			@Override
			public String toString() {
				return "Event [" +
						"location='" + location + '\'' +
						", title='" + title + '\'' +
						", duration=" + duration +
						", capacity=" + capacity +
						']';
			}
		}

		public static class ExpireAfter {
			private Integer uses;
			private Long date;

			public Integer getUses() {
				return uses;
			}

			public Long getDate() {
				return date;
			}

			public Date getDateAsDate() {
				return date != null ? new Date(date * 1000) : null;
			}

			public void setUses(Integer uses) {
				this.uses = uses;
			}

			public void setDate(Long date) {
				this.date = date;
			}

			public void setDate(Date date) {
				this.date = date.getTime() / 1000;
			}

			@Override
			public String toString() {
				return "ExpireAfter [" +
						"uses=" + uses +
						", date=" + date +
						']';
			}
		}

		public static class Reminders {
			private String delivery_method;
			private String delivery_recipient;
			private String email_subject;
			private String webhook_url;
			private Integer time_before_event;

			public String getDeliveryMethod() {
				return delivery_method;
			}

			public String getDeliveryRecipient() {
				return delivery_recipient;
			}

			public String getEmailSubject() {
				return email_subject;
			}

			public String getWebhookUrl() {
				return webhook_url;
			}

			public Integer getTimeBeforeEvent() {
				return time_before_event;
			}

			public void setDeliveryMethod(String deliveryMethod) {
				this.delivery_method = deliveryMethod;
			}

			public void setDeliveryRecipient(String deliveryRecipient) {
				this.delivery_recipient = deliveryRecipient;
			}

			public void setEmailSubject(String emailSubject) {
				this.email_subject = emailSubject;
			}

			public void setWebhookUrl(String webhookUrl) {
				this.webhook_url = webhookUrl;
			}

			public void setTimeBeforeEvent(Integer timeBeforeEvent) {
				this.time_before_event = timeBeforeEvent;
			}

			@Override
			public String toString() {
				return "Reminders [" +
						"deliveryMethod='" + delivery_method + '\'' +
						", deliveryRecipient='" + delivery_recipient + '\'' +
						", emailSubject='" + email_subject + '\'' +
						", webhookUrl='" + webhook_url + '\'' +
						", timeBeforeEvent=" + time_before_event +
						']';
			}
		}
	}
}
