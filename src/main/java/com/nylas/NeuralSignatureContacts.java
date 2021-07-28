package com.nylas;

import java.util.ArrayList;
import java.util.List;

public class NeuralSignatureContacts {
	private List<String> job_titles;
	private List<Link> links;
	private List<Name> names;
	private List<String> phone_numbers;
	private List<String> emails;

	public List<String> getJobTitles() {
		return job_titles;
	}

	public List<Link> getLinks() {
		return links;
	}

	public List<Name> getNames() {
		return names;
	}

	public List<String> getPhoneNumbers() {
		return phone_numbers;
	}

	public List<String> getEmails() {
		return emails;
	}

	/**
	 * Creates a Nylas contact object compatible with the contact endpoints.
	 * Please note if multiple names were parsed only the first set are used.
	 */
	public Contact toContactObject() {
		Contact contact = new Contact();

		if(names != null) {
			contact.setGivenName(names.get(0).getFirstName());
			contact.setSurname(names.get(0).getLastName());
		}
		if(job_titles != null) {
			contact.setJobTitle(job_titles.get(0));
		}
		if(emails != null) {
			List<Contact.Email> emailList = new ArrayList<>();
			for(String email : emails) {
				emailList.add(new Contact.Email("personal", email));
			}
			contact.setEmails(emailList);
		}
		if(phone_numbers != null) {
			List<Contact.PhoneNumber> phoneList = new ArrayList<>();
			for(String number : phone_numbers) {
				phoneList.add(new Contact.PhoneNumber("mobile", number));
			}
			contact.setPhoneNumbers(phoneList);
		}
		if(links != null) {
			List<Contact.WebPage> webPageList = new ArrayList<>();
			for(Link link : links) {
				String description = link.getDescription() != null && !link.getDescription().isEmpty() ?
						link.getDescription() : "homepage";
				webPageList.add(new Contact.WebPage(description, link.getUrl()));
			}
			contact.setWebPages(webPageList);
		}

		return contact;
	}

	@Override
	public String toString() {
		return String.format("NeuralSignatureContacts [job_titles=%s, links=%s, names=%s, phone_numbers=%s, emails=%s]",
				job_titles, links, names, phone_numbers, emails);
	}
}

class Name {
	private String first_name;
	private String last_name;

	public String getFirstName() {
		return first_name;
	}

	public String getLastName() {
		return last_name;
	}

	@Override
	public String toString() {
		return String.format("Name [first_name=%s, last_name=%s]",
				first_name, last_name);
	}
}

class Link {
	private String description;
	private String url;

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return String.format("Link [description=%s, url=%s]",
				description, url);
	}
}