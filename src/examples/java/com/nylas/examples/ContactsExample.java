package com.nylas.examples;

import java.util.Arrays;
import java.util.Properties;

import com.nylas.Contact;
import com.nylas.ContactQuery;
import com.nylas.Contacts;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class ContactsExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Contacts contacts = account.contacts();
		
		ContactQuery query = new ContactQuery()
				.source("address_book")
				.limit(10)
				;
		for (Contact c : contacts.list(query)) {
			System.out.println(c);
		}
		
		Contact newContact = new Contact();
		newContact.setGivenName("Icarus");
		newContact.setMiddleName("J.");
		newContact.setSurname("Daedaluson");
		newContact.setSuffix("Esq.");
		newContact.setNickname("Ick");
		newContact.setBirthday("1959-03-31");
		newContact.setCompanyName("Solar Airlines");
		newContact.setJobTitle("Aviation Engineer");
		newContact.setManagerName("Minos");
		newContact.setNotes("Sensitive to solar radiation");
		newContact.setOfficeLocation("Crete");
		newContact.setEmails(Arrays.asList(new Contact.Email("work", "icarus@example.com"),
				new Contact.Email("personal", "ickthestick@example.com")));
		newContact.setIMAddresses(Arrays.asList(new Contact.IMAddress("yahoo", "ickthestick")));
		newContact.setPhoneNumbers(Arrays.asList(new Contact.PhoneNumber("mobile", "510-555-5555"),
				new Contact.PhoneNumber("business", "415-555-5555")));
		Contact.PhysicalAddress address = new Contact.PhysicalAddress();
		address.setStreetAddress("12345 Avian Ave.");
		address.setType("other");
		address.setCity("Flyville");
		address.setState("CA");
		address.setCountry("USA");
		address.setPostalCode("00000");
		address.setFormat("structured");
		newContact.setPhysicalAddresses(Arrays.asList(address));
		
		Contact result = contacts.create(newContact);
		System.out.println(result);
		
		Contact ick = contacts.get("85zgticulonlxzbfcd38gxy6w");
		System.out.println(ick);
	}
}
