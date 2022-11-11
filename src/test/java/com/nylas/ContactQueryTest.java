package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContactQueryTest {
    private ContactQuery contactQuery;
    private HttpUrl.Builder url;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        contactQuery = new ContactQuery();
        url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nylas.com")
                .addPathSegment("contacts");
    }


    @Test
    public void testGetters() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.email("jdoe@gmail.com");

        contactQuery.addParameters(url);
        String urlWithEmail = url.build().toString();
        assertEquals(urlWithEmail, "https://api.nylas.com/contacts?email=jdoe%40gmail.com");

        String email = (String) FieldReflectionUtils.getField("email", contactQuery);
        assertEquals("jdoe@gmail.com", email);
    }

    @Test
    public void testPhoneNumber() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.phoneNumber("+1 205 0293 03489");

        contactQuery.addParameters(url);
        String urlWithPhoneNumber = url.build().toString();
        assertEquals(urlWithPhoneNumber, "https://api.nylas.com/contacts?phone_number=%2B1%20205%200293%2003489");

        String phoneNumber = (String) FieldReflectionUtils.getField("phoneNumber", contactQuery);
        assertEquals(phoneNumber, "+1 205 0293 03489");
    }

    @Test
    public void testStreetAddress() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.streetAddress("315 Market Street");

        contactQuery.addParameters(url);
        String urlWithStreetAddress = url.build().toString();
        assertEquals(urlWithStreetAddress, "https://api.nylas.com/contacts?street_address=315%20Market%20Street");

        String streetAddress = (String) FieldReflectionUtils.getField("streetAddress", contactQuery);
        assertEquals(streetAddress, "315 Market Street");
    }

    @Test
    public void testPostalCode() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.postalCode("11111");

        contactQuery.addParameters(url);
        String urlWithPostalCode = url.build().toString();
        assertEquals(urlWithPostalCode, "https://api.nylas.com/contacts?postal_code=11111");

        String postalCode = (String) FieldReflectionUtils.getField("postalCode", contactQuery);
        assertEquals(postalCode,"11111");
    }

    @Test
    public void testState() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.state("CA");

        contactQuery.addParameters(url);
        String urlWithPostalCode = url.build().toString();
        assertEquals(urlWithPostalCode, "https://api.nylas.com/contacts?state=CA");

        String state = (String) FieldReflectionUtils.getField("state", contactQuery);
        assertEquals(state, "CA");
    }

    @Test
    public void testCountry() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.country("US");

        contactQuery.addParameters(url);
        String urlWithPostalCode = url.build().toString();
        assertEquals(urlWithPostalCode, "https://api.nylas.com/contacts?country=US");

        String country = (String) FieldReflectionUtils.getField("country", contactQuery);
        assertEquals(country, "US");
    }

    @Test
    public void testSource() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.source("address_book");

        contactQuery.addParameters(url);
        String urlWithPostalCode = url.build().toString();
        assertEquals(urlWithPostalCode, "https://api.nylas.com/contacts?source=address_book");

        String source = (String) FieldReflectionUtils.getField("source", contactQuery);
        assertEquals(source, "address_book");
    }

    @Test
    public void testGroupId() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.groupId("ladsckjn908ui3");

        contactQuery.addParameters(url);
        String urlWithPostalCode = url.build().toString();
        assertEquals(urlWithPostalCode, "https://api.nylas.com/contacts?group=ladsckjn908ui3");

        String groupId = (String) FieldReflectionUtils.getField("groupId", contactQuery);
        assertEquals("ladsckjn908ui3", groupId);
    }

    @Test
    public void testRecurse() throws NoSuchFieldException, IllegalAccessException {
        contactQuery.recurse(true);

        contactQuery.addParameters(url);
        String urlWithPostalCode = url.build().toString();
        assertEquals(urlWithPostalCode, "https://api.nylas.com/contacts?recurse=true");

        Boolean recurse = (Boolean) FieldReflectionUtils.getField("recurse", contactQuery);
        assertTrue(recurse);
    }
}
