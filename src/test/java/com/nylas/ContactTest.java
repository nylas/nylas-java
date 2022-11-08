package com.nylas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {
    private Contact contact;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        contact = new Contact();

        contact.setGivenName("Linus");
        contact.setMiddleName("Benedict");
        contact.setSurname("Torvalds");
        contact.setSuffix("mr");
        contact.setNickname("Linux");
        contact.setBirthday("December 28, 1969");
        contact.setCompanyName("Linux Foundation");
        contact.setJobTitle("Founder");
        contact.setManagerName("N/A");
        contact.setOfficeLocation("Portland, Oregon");
        contact.setNotes("Known for git and linux");
        setField("picture_url", "https://media.newyorker.com/photos/5ba177da9eb2f7420aadeb98/master/w_1920,c_limit/Cohen-Linus-Torvalds.jpg", contact);

        List<Contact.Email> emails = new LinkedList<>();
        Contact.Email email = new Contact.Email("imap", "torvalds@linux-foundation.org");
        emails.add(email);
        contact.setEmails(emails);

        List<Contact.IMAddress> im_addresses = new LinkedList<>();
        Contact.IMAddress imAddress = new Contact.IMAddress("im", "torvalds-im@linux-foundation.org");
        im_addresses.add(imAddress);
        contact.setIMAddresses(im_addresses);

        List<Contact.PhysicalAddress> physical_addresses = new LinkedList<>();
        Contact.PhysicalAddress physicalAddress = new Contact.PhysicalAddress();
        physicalAddress.setFormat("standard");
        physicalAddress.setStreetAddress("1234");
        physicalAddress.setCity("Portland");
        physicalAddress.setCountry("U.S");
        physicalAddress.setState("Oregon");
        physicalAddress.setType("physical");
        physicalAddress.setPostalCode("0101");
        physical_addresses.add(physicalAddress);
        contact.setPhysicalAddresses(physical_addresses);

        List<Contact.PhoneNumber> phone_numbers = new LinkedList<>();
        Contact.PhoneNumber phoneNumber = new Contact.PhoneNumber("cellphone", "091283");
        phone_numbers.add(phoneNumber);
        contact.setPhoneNumbers(phone_numbers);


        List<Contact.WebPage> web_pages = new LinkedList<>();
        Contact.WebPage webPage = new Contact.WebPage("page", "linux-foundation.org");
        web_pages.add(webPage);
        contact.setWebPages(web_pages);

        ContactGroup contactGroup = new ContactGroup();
        Field idField = contactGroup.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(contactGroup, "asdolkv909d7v4r");
        setField("name", "org", contactGroup);
        setField("path", "it", contactGroup);
        contact.setGroup(contactGroup);

        setField("source", "address_book", contact);
    }

    @Test
    public void testGetters() {
        assertEquals(contact.getObjectType(), "contact");
        assertEquals(contact.getGivenName(), "Linus");
        assertEquals(contact.getMiddleName(), "Benedict");
        assertEquals(contact.getSurname(), "Torvalds");
        assertEquals(contact.getSuffix(), "mr");
        assertEquals(contact.getNickname(), "Linux");
        assertEquals(contact.getBirthday(), "December 28, 1969");
        assertEquals(contact.getCompanyName(), "Linux Foundation");
        assertEquals(contact.getJobTitle(), "Founder");
        assertEquals(contact.getManagerName(), "N/A");
        assertEquals(contact.getOfficeLocation(), "Portland, Oregon");
        assertEquals(contact.getNotes(), "Known for git and linux");
        assertEquals(contact.getPictureUrl(), "https://media.newyorker.com/photos/5ba177da9eb2f7420aadeb98/master/w_1920,c_limit/Cohen-Linus-Torvalds.jpg");
        assertEquals(contact.getEmails().size(), 1);
        assertEquals(contact.getIMAddresses().size(), 1);
        assertEquals(contact.getPhysicalAddresses().size(), 1);
        assertEquals(contact.getPhoneNumbers().size(), 1);
        assertEquals(contact.getWebPages().size(), 1);
        assertEquals(contact.getGroups().size(),1);
        assertEquals(contact.getSource(), "address_book");
    }

    @Test
    public void testGetWriteableFields_creation() {
        Map<String, Object> writeableFields = contact.getWritableFields(true);

        assertEquals(writeableFields.size(), 17);
        assertEquals(writeableFields.get("given_name"), "Linus");
        assertEquals(writeableFields.get("middle_name"), "Benedict");
        assertEquals(writeableFields.get("surname"), "Torvalds");
        assertEquals(writeableFields.get("birthday"), "December 28, 1969");
        assertEquals(writeableFields.get("suffix"), "mr");
        assertEquals(writeableFields.get("nickname"), "Linux");
        assertEquals(writeableFields.get("company_name"), "Linux Foundation");
        assertEquals(writeableFields.get("job_title"), "Founder");
        assertEquals(writeableFields.get("manager_name"), "N/A");
        assertEquals(writeableFields.get("office_location"), "Portland, Oregon");
        assertEquals(writeableFields.get("notes"), "Known for git and linux");

        List<Contact.Email> emails = (List<Contact.Email>) writeableFields.get("emails");
        assertEquals(emails.get(0).getEmail(), "torvalds@linux-foundation.org");

        List<Contact.IMAddress> imAddresses = (List<Contact.IMAddress>) writeableFields.get("im_addresses");
        assertEquals(imAddresses.get(0).getIMAddress(), "torvalds-im@linux-foundation.org");

        List<Contact.PhoneNumber> phoneNumbers = (List<Contact.PhoneNumber>) writeableFields.get("phone_numbers");
        assertEquals(phoneNumbers.get(0).getNumber(), "091283");

        List<Contact.PhysicalAddress> physicalAddresses = (List<Contact.PhysicalAddress>) writeableFields.get("physical_addresses");
        assertEquals(physicalAddresses.get(0).getStreetAddress(), "1234");

        List<Contact.WebPage> webPages = (List<Contact.WebPage>) writeableFields.get("web_pages");
        assertEquals(webPages.get(0).getUrl(), "linux-foundation.org");

        String group = (String)writeableFields.get("group"); // gets the id of the group and not the actual group
        assertEquals(group, "asdolkv909d7v4r");
    }

    @Test
    public void testGetWriteableFields_NoCreation() {
        Map<String, Object> writeableFields = contact.getWritableFields(true);

        assertEquals(writeableFields.size(), 16);
        assertEquals(writeableFields.get("given_name"), "Linus");
        assertEquals(writeableFields.get("middle_name"), "Benedict");
        assertEquals(writeableFields.get("surname"), "Torvalds");
        assertEquals(writeableFields.get("birthday"), "December 28, 1969");
        assertEquals(writeableFields.get("suffix"), "mr");
        assertEquals(writeableFields.get("nickname"), "Linux");
        assertEquals(writeableFields.get("company_name"), "Linux Foundation");
        assertEquals(writeableFields.get("job_title"), "Founder");
        assertEquals(writeableFields.get("manager_name"), "N/A");
        assertEquals(writeableFields.get("office_location"), "Portland, Oregon");
        assertEquals(writeableFields.get("notes"), "Known for git and linux");

        List<Contact.Email> emails = (List<Contact.Email>) writeableFields.get("emails");
        assertEquals(emails.get(0).getEmail(), "torvalds@linux-foundation.org");

        List<Contact.IMAddress> imAddresses = (List<Contact.IMAddress>) writeableFields.get("im_addresses");
        assertEquals(imAddresses.get(0).getIMAddress(), "torvalds-im@linux-foundation.org");

        List<Contact.PhoneNumber> phoneNumbers = (List<Contact.PhoneNumber>) writeableFields.get("phone_numbers");
        assertEquals(phoneNumbers.get(0).getNumber(), "091283");

        List<Contact.PhysicalAddress> physicalAddresses = (List<Contact.PhysicalAddress>) writeableFields.get("physical_addresses");
        assertEquals(physicalAddresses.get(0).getStreetAddress(), "1234");

        List<Contact.WebPage> webPages = (List<Contact.WebPage>) writeableFields.get("web_pages");
        assertEquals(webPages.get(0).getUrl(), "linux-foundation.org");
    }

    @Test
    public void testToString() {
        assertEquals(contact.toString(), "Contact [given_name=Linus, middle_name=Benedict, surname=Torvalds, suffix=mr, nickname=Linux, birthday=December 28, 1969, company_name=Linux Foundation, job_title=Founder, manager_name=N/A, office_location=Portland, Oregon, notes=Known for git and linux, picture_url=https://media.newyorker.com/photos/5ba177da9eb2f7420aadeb98/master/w_1920,c_limit/Cohen-Linus-Torvalds.jpg, emails=[Email [type=imap, email=torvalds@linux-foundation.org]], im_addresses=[IMAddress [type=im, im_address=torvalds-im@linux-foundation.org]], physical_addresses=[PhysicalAddress [format=standard, type=physical, street_address=1234, city=Portland, postal_code=0101, state=Oregon, country=U.S]], phone_numbers=[PhoneNumber [type=cellphone, number=091283]], web_pages=[WebPage [type=page, url=linux-foundation.org]], groups=[ContactGroup [name=org, path=it, accountId=null, id=null]], source=address_book, accountId=null, id=null]");
    }

    @Test
    public void testEmail() {
        Contact.Email email = new Contact.Email(); // Testing deserialization constructor
        assertNotNull(email);

        assertEquals(contact.getEmails().get(0).getEmail(), "torvalds@linux-foundation.org");
        assertEquals(contact.getEmails().get(0).getType(), "imap");
    }

    @Test
    public void testIMAddress() {
        Contact.IMAddress imAddress = new Contact.IMAddress(); // Testing deserialization constructor
        assertNotNull(imAddress);

        assertEquals(contact.getIMAddresses().get(0).getIMAddress(), "torvalds-im@linux-foundation.org");
        assertEquals(contact.getIMAddresses().get(0).getType(), "im");
    }

    @Test
    public void testPhysicalAddress() {
        assertEquals(contact.getPhysicalAddresses().get(0).getFormat(), "standard");
        assertEquals(contact.getPhysicalAddresses().get(0).getType(), "physical");
        assertEquals(contact.getPhysicalAddresses().get(0).getStreetAddress(), "1234");
        assertEquals(contact.getPhysicalAddresses().get(0).getCity(), "Portland");
        assertEquals(contact.getPhysicalAddresses().get(0).getPostalCode(), "0101");
        assertEquals(contact.getPhysicalAddresses().get(0).getState(), "Oregon");
        assertEquals(contact.getPhysicalAddresses().get(0).getCountry(), "U.S");
    }

    @Test
    public void testPhoneNumber() {
        Contact.PhoneNumber phoneNumber = new Contact.PhoneNumber(); // testing deserialization constructor
        assertNotNull(phoneNumber);

        assertEquals(contact.getPhoneNumbers().get(0).getNumber(), "091283");
        assertEquals(contact.getPhoneNumbers().get(0).getType(), "cellphone");
    }

    @Test
    public void testWebPage() {
        Contact.WebPage webPage = new Contact.WebPage(); // testing deserialization constructor
        assertNotNull(webPage);

        assertEquals(contact.getWebPages().get(0).getUrl(), "linux-foundation.org");
        assertEquals(contact.getWebPages().get(0).getType(), "page");
    }

    private void setField(String fieldName, Object fieldValue, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field codeField = o.getClass().getDeclaredField(fieldName);
        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }
}
