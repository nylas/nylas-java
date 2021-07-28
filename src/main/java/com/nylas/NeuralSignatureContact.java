package com.nylas;

import java.util.ArrayList;
import java.util.List;

public class NeuralSignatureContact {
	private List<String> job_titles;
	private List<Links> links;
	private List<Names> names;
	private List<String> phone_numbers;
	private List<String> emails;

	public List<String> getJobTitles() {
		return job_titles;
	}

	public List<Links> getLinks() {
		return links;
	}

	public List<Names> getNames() {
		return names;
	}

	public List<String> getPhoneNumbers() {
		return phone_numbers;
	}

	public List<String> getEmails() {
		return emails;
	}

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
			for(Links link : links) {
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
		return String.format("NeuralSignatureContact [job_titles=%s, links=%s, names=%s, phone_numbers=%s, emails=%s]",
				job_titles, links, names, phone_numbers, emails);
	}
}

class Names {
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
		return String.format("Names [first_name=%s, last_name=%s]",
				first_name, last_name);
	}
}

class Links {
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
		return String.format("Links [description=%s, url=%s]",
				description, url);
	}
}