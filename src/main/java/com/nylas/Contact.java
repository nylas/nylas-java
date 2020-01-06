package com.nylas;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contact extends AccountOwnedModel implements JsonObject {

	private String given_name;
	private String middle_name;
	private String surname;
	private String suffix;
	private String nickname;
	private String birthday;
	private String company_name;
	private String job_title;
	private String manager_name;
	private String office_location;
	private String notes;
	private String picture_url;
	private List<Email> emails;
	private List<IMAddress> im_addresses;
	private List<PhysicalAddress> physical_addresses;
	private List<PhoneNumber> phone_numbers;
	private List<WebPage> web_pages;
	private List<ContactGroup> groups;
	private String source;
	
	@Override
	public String getObjectType() {
		return "contact";
	}
	
	public String getGivenName() {
		return given_name;
	}

	public String getMiddleName() {
		return middle_name;
	}

	public String getSurname() {
		return surname;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getNickname() {
		return nickname;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getCompanyName() {
		return company_name;
	}

	public String getJobTitle() {
		return job_title;
	}

	public String getManagerName() {
		return manager_name;
	}

	public String getOfficeLocation() {
		return office_location;
	}

	public String getNotes() {
		return notes;
	}

	public String getPictureUrl() {
		return picture_url;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public List<IMAddress> getIMAddresses() {
		return im_addresses;
	}

	public List<PhysicalAddress> getPhysicalAddresses() {
		return physical_addresses;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phone_numbers;
	}

	public List<WebPage> getWebPages() {
		return web_pages;
	}

	public List<ContactGroup> getGroups() {
		return groups;
	}

	public String getSource() {
		return source;
	}

	public void setGivenName(String givenName) {
		this.given_name = givenName;
	}

	public void setMiddleName(String middleName) {
		this.middle_name = middleName;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setCompanyName(String companyName) {
		this.company_name = companyName;
	}

	public void setJobTitle(String jobTitle) {
		this.job_title = jobTitle;
	}

	public void setManagerName(String managerName) {
		this.manager_name = managerName;
	}

	public void setOfficeLocation(String officeLocation) {
		this.office_location = officeLocation;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public void setIMAddresses(List<IMAddress> imAddresses) {
		this.im_addresses = imAddresses;
	}

	public void setPhysicalAddresses(List<PhysicalAddress> physicalAddresses) {
		this.physical_addresses = physicalAddresses;
	}

	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phone_numbers = phoneNumbers;
	}

	public void setWebPages(List<WebPage> webPages) {
		this.web_pages = webPages;
	}

	public void setGroup(ContactGroup group) {
		this.groups = Arrays.asList(group);
	}

	@Override
	Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		if (creation) {
			if (groups != null && groups.size() > 0) {
				Maps.putIfNotNull(params, "group", groups.get(0).getId());
			}
		}
		Maps.putIfNotNull(params, "given_name", getGivenName());
		Maps.putIfNotNull(params, "middle_name", getMiddleName());
		Maps.putIfNotNull(params, "surname", getSurname());
		Maps.putIfNotNull(params, "birthday", getBirthday());
		Maps.putIfNotNull(params, "suffix", getSuffix());
		Maps.putIfNotNull(params, "nickname", getNickname());
		Maps.putIfNotNull(params, "company_name", getCompanyName());
		Maps.putIfNotNull(params, "job_title", getJobTitle());
		Maps.putIfNotNull(params, "manager_name", getManagerName());
		Maps.putIfNotNull(params, "office_location", getOfficeLocation());
		Maps.putIfNotNull(params, "notes", getNotes());
		Maps.putIfNotNull(params, "emails", getEmails());
		Maps.putIfNotNull(params, "im_addresses", getIMAddresses());
		Maps.putIfNotNull(params, "physical_addresses", getPhysicalAddresses());
		Maps.putIfNotNull(params, "phone_numbers", getPhoneNumbers());
		Maps.putIfNotNull(params, "web_pages", getWebPages());
		return params;
	}
	
	@Override
	public String toString() {
		return "Contact [given_name=" + given_name + ", middle_name=" + middle_name + ", surname=" + surname
				+ ", suffix=" + suffix + ", nickname=" + nickname + ", birthday=" + birthday + ", company_name="
				+ company_name + ", job_title=" + job_title + ", manager_name=" + manager_name + ", office_location="
				+ office_location + ", notes=" + notes + ", picture_url=" + picture_url + ", emails=" + emails
				+ ", im_addresses=" + im_addresses + ", physical_addresses=" + physical_addresses + ", phone_numbers="
				+ phone_numbers + ", web_pages=" + web_pages + ", groups=" + groups + ", source=" + source
				+ ", accountId=" + getAccountId() + ", id=" + getId() + "]";
	}

	public static class Email {
		private String type;
		private String email;
		
		public Email() {}
		
		public Email(String type, String email) {
			this.type = type;
			this.email = email;
		}

		public String getType() {
			return type;
		}
		
		public String getEmail() {
			return email;
		}
		
		@Override
		public String toString() {
			return "Email [type=" + type + ", email=" + email + "]";
		}
	}
	
	public static class IMAddress {
		private String type;
		private String im_address;
		
		public IMAddress() {}
		
		public IMAddress(String type, String im_address) {
			this.type = type;
			this.im_address = im_address;
		}

		public String getType() {
			return type;
		}
		
		public String getIMAddress() {
			return im_address;
		}

		@Override
		public String toString() {
			return "IMAddress [type=" + type + ", im_address=" + im_address + "]";
		}
	}
	
	public static class PhysicalAddress {
		private String format;
		private String type;
		private String street_address;
		private String city;
		private String postal_code;
		private String state;
		private String country;

		public String getFormat() {
			return format;
		}

		public String getType() {
			return type;
		}

		public String getStreetAddress() {
			return street_address;
		}

		public String getCity() {
			return city;
		}

		public String getPostalCode() {
			return postal_code;
		}

		public String getState() {
			return state;
		}

		public String getCountry() {
			return country;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setStreetAddress(String streetAddress) {
			this.street_address = streetAddress;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public void setPostalCode(String postalCode) {
			this.postal_code = postalCode;
		}

		public void setState(String state) {
			this.state = state;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		@Override
		public String toString() {
			return "PhysicalAddress [format=" + format + ", type=" + type + ", street_address=" + street_address
					+ ", city=" + city + ", postal_code=" + postal_code + ", state=" + state + ", country=" + country
					+ "]";
		}
	}
	
	public static class PhoneNumber {
		private String type;
		private String number;

		public PhoneNumber() {}

		public PhoneNumber(String type, String number) {
			this.type = type;
			this.number = number;
		}

		public String getType() {
			return type;
		}

		public String getNumber() {
			return number;
		}

		@Override
		public String toString() {
			return "PhoneNumber [type=" + type + ", number=" + number + "]";
		}
	}
	
	public static class WebPage {
		private String type;
		private String url;

		public WebPage() {}

		public WebPage(String type, String url) {
			this.type = type;
			this.url = url;
		}

		public String getType() {
			return type;
		}

		public String getUrl() {
			return url;
		}

		@Override
		public String toString() {
			return "WebPage [type=" + type + ", url=" + url + "]";
		}
	}

}
