# Upgrading to the Nylas Java SDK v2.0

## Introduction
With the upcoming release of the Nylas API v3, the Nylas Java SDK has been rewritten to be more idiomatic and easier to use. This guide will help you upgrade your code to use the new SDK. We have also documented the methods and models of the SDK so that you can easily find the information you need in our [SDK reference](https://nylas-java-sdk-reference.pages.dev/).

## Initial Setup
The first step to using the Nylas Java SDK is to create a new `NylasClient` object. This object will be used to make all API requests. All methods to initialize the `NylasClient` that were deprecated are now removed, so the only way to create it now is through the builder. The SDK employs the use of builder patterns in many places, to make it more readable and easier to create objects with many optional parameters. 

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();
```

From here, you can use the client to make API requests by accessing all the different resources configured with your API Key.


## Building Objects
As mentioned earlier, the SDK now uses builder patterns extensively to create objects. This makes it easier to create objects with many optional parameters. All models that contain optional parameters now have a builder class that can be used to create the object.

Furthermore, we now have different models for creating, updating, and retrieving objects. This was done to better reflect the API, which itself has different models for each different endpoint and operation. Create objects are prefixed with "Create" and has a suffix of "Request", update objects are prefixed with "Update" and has a suffix of "Request", and retrieve objects are just the name of the object.

If we take Calendar objects as an example, `Calendar` is the retrieve object, `CreateCalendarRequest` is the create object, and `UpdateCalendarRequest` is the update object.

To illustrate this, the following code shows how to construct the request to create a new calendar:

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();

CreateCalendarRequest createCalendarRequest = new CreateCalendarRequest.Builder("My Calendar") // Calendar name is required
    .description("My calendar description") // Calendar description is optional
    .location("My calendar location") // Calendar location is optional
    .timezone("America/New_York") // Calendar timezone is optional
    .build();
```

## Making Requests to the Nylas API
Your `NylasClient` object is what you use to make requests to the Nylas API. The SDK is organized into different resources, each of which has methods to make requests to the API. Each resource is available through the `NylasClient` object configured with you API key. 

For example, to get a list of calendars, you can use the following code:

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();
ListResponse<Calendars> calendars = nylas.calendars().list("GRANT_ID");
```

As illustrated by the code above, there are some new concepts that are introduced into the SDK when making requests. These concepts are explained in more detail below.

### Resource Parameters
Depending on the resource it takes different parameters. All resources take an "identifier", which is the ID of the account you want to make the request for. This is usually the Grant ID or the email address of the account. Some resources also take "query parameters" which is mainly used for filtering data or passing in additional information.

### Response Objects
With the new Nylas API v3, we now have standard response objects for all requests (excluding OAuth endpoints). There's generally two main types of response objects: `Response` and `ListResponse`.

The `Response` object is used for requests that return a single object, such as retrieving a single calendar. This will return a parameterized object of the type you are requesting (e.g. `Calendar`) and a string representing the request ID.

The `ListResponse` object is used for requests that return a list of objects, such as retrieving a list of calendars.This will return a list of parameterized objects of the type you are requesting (e.g. `Calendar`), a string representing the request ID, and another string representing the token of the next page for paginating a request.

### Error Objects
Like the response objects, we now have standard error objects for all requests (excluding OAuth endpoints). There are two superclass error classes, `AbstractNylasApiError` and `AbstractNylasSdkError`.

`AbstractNylasApiError` is used for errors returned by the API. Currently, there are only two subclasses of this error class: `NylasOAuthError` and `NylasApiError`. `NylasOAuthError` is used for errors that are returned from the OAuth endpoints. While `NylasApiError` is used for errors all the other Nylas API errors. The error details are extracted from the response and stored in the error object along with the request ID as well as the HTTP status code.

`AbstractNylasSdkError` is used for errors returned by the SDK. Right now there's only one type of error we return, and that's a `NylasSdkTimeoutError` which is thrown when a request times out.

Putting it all together, the following code shows how to make a request to create a new Event and handle any errors that may occur:

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();

// Build the create event request
CreateEventRequest createEventRequest = new CreateEventRequest.Builder(
    new CreateEventRequest.When
        .Timespan
        .Builder(1686765600, 1686769200)
        .build()
    ) // A "When" type is required
    .title("My Event") // Title is optional
    .description("My event description") // Description is optional
    .location("My event location") // Location is optional
    .build();

// Build the query parameters, as it is required for event creation
CreateEventQueryParams createEventQueryParams = new CreateEventQueryParams.Builder(calendarId).build();

try {
    // Make the request
    Response<Event> createdEventResponse = nylas.events().create(
        "GRANT_ID",
        createEventRequest,
        createEventQueryParams
    );
} catch (NylasApiError e) {
    // Handle error
}
```

## Authentication
The authentication methods have changed to reflect the new Nylas API v3. While you can manage your application's integrations in the dashboard, you can manage nearly everything else directly from the SDK. That includes managing grants, redirect URIs, OAuth tokens, and authenticating your users.

To authenticate users to your application there are two main methods to focus on. The first is the `Auth#urlForOAuth2` method, which returns the URL that you should redirect your users to in order to authenticate them using Nylas' OAuth 2.0 implementation. 

The second is the `Auth#exchangeCodeForToken` method, which is used to exchange the code returned from the authentication redirect for an access token. You actually don't need to use the data from the response as you can use the authenticated email address directly as the identifier for the account. However, if you prefer to use the grant ID as the account identifier, you can extract the grant ID from the `CodeExchangeResponse` object and use that instead.

The following code shows how to authenticate a user into a Nylas application:

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();

String clientId = "NYLAS_CLIENT_ID";
String clientSecret = "NYLAS_CLIENT_SECRET";
String clientUri = "https://redirect-uri.com/path";

// Build the URL for authentication
String authURL = nylasClient.auth().urlForOAuth2(
    new UrlForAuthenticationConfig.Builder(clientId, clientUri)
      .loginHint("example@email.com")
      .build());

// Insert code here to redirect the user to the url and parse the code
    ...

// Exchange the code for an access token
CodeExchangeRequest codeExchangeRequest = new CodeExchangeRequest.Builder(
    clientUri,
    "CODE",
    clientId,
    clientSecret
).build();

CodeExchangeResponse codeExchangeResponse = nylasClient.auth().exchangeCodeForToken(codeExchangeRequest);

// Now you can either use the email address that was authenticated or the grant ID in the response as the identifier

// Using the email address
ListResponse<Calendars> calendars = nylas.calendars().list("example@email.com");

// Using the grant ID
ListResponse<Calendars> calendars = nylas.calendars().list(codeExchangeResponse.getGrantId());
```
