package com.nylas.examples;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import com.nylas.Contact;
import com.nylas.ContactGroup;
import com.nylas.ContactGroups;
import com.nylas.ContactQuery;
import com.nylas.Contacts;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

import okhttp3.ResponseBody;

public class ContactsExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Contacts contacts = account.contacts();
		
		ContactQuery query = new ContactQuery()
				.source("address_book")
				.limit(10)
				;
		Contact hasProfilePicture = null;
		for (Contact c : contacts.list(query)) {
			if (c.getPictureUrl() != null) {
				hasProfilePicture = c;
			}
			System.out.println(c);
		}
		
		if (hasProfilePicture == null) {
			System.out.println("No contact has a profile pic");
		} else {
			try (ResponseBody picResponse = contacts.downloadProfilePicture(hasProfilePicture.getId())) {
				Files.copy(picResponse.byteStream(),
						Paths.get("/tmp/contact.profile." + hasProfilePicture.getId() + ".jpg"),
						StandardCopyOption.REPLACE_EXISTING);
			}
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
		
		Contact created = contacts.create(newContact);
		System.out.println("Created: " + created);
		
		created.setGivenName("Icarus (deceased)");
		Contact updated = contacts.update(created);
		System.out.println("Updated: " + updated);
		
		contacts.delete(updated.getId());
		System.out.println("deleted");
		
		ContactGroups groups = account.contactGroups();
		for (ContactGroup group : groups.list()) {
			System.out.println(group);
		}
	}
}
