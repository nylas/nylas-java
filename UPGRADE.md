# Upgrading to the Nylas SDK v2.0 for Java

## Introduction

The Nylas Java SDK has been rewritten for the upcoming release of the Nylas API v3 to be more idiomatic and easier to use. This guide helps you upgrade your code to use the new SDK. The new SDK also includes [documentation for the SDK's methods and models](https://nylas-java-sdk-reference.pages.dev/) so you can easily find the implementation details you need.

⚠️ **Note:** The Nylas Java SDK v2.0 is not compatible with Nylas APIs earlier than 3.0 beta. If you are still using an earlier version of the API (such as Nylas v2.7), keep using the Nylas Java SDK v1.x until you can upgrade.

## Initial Set up

To upgrade to the new SDK, you update your dependencies to use the new version.

**Note:** The artifact name has changed from `nylas-java-sdk` to `nylas`.
This makes it consistent with the other Nylas SDK offerings, and reflects that this SDK is no longer Java-only. Despite the name change, versioning will remain consistent with the previous SDK version, so the first version of the new SDK is `2.0.0`.

To upgrade to the new SDK, change the dependency in your `build.gradle` file to the following:

```groovy
implementation ('com.nylas.sdk:nylas:2.0.0')
```

The first step to use the Nylas Java SDK is to create a new `NylasClient` object. This object is used to make all API requests.

All previous deprecated methods of initializing the `NylasClient` have been removed, so you must now create it  using the builder, as in the example below. The SDK uses builder patterns in many places to make your code more readable and easier to create objects with many optional parameters.

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();
```

From here, you can use the client to make API requests by accessing the different resources configured with your API Key.

## Building Objects

As mentioned earlier, the SDK now uses builder patterns extensively to create objects. This makes it easier to create objects with many optional parameters. All models that contain optional parameters now have a builder class that you can use to create the object.

The SDK now has different models for creating, updating, and retrieving objects, to better reflect the underlying methods in the Nylas API. Create objects are prefixed with `Create` and have a suffix of `Request`, update objects are prefixed with `Update` and have a suffix of `Request`, and retrieve objects are just the name of the object.

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

You use the `NylasClient` object to make requests to the Nylas API. The SDK is organized into different resources, each of which has methods to make requests to the API. Each resource is available using the `NylasClient` object configured with your API key.

For example, to get a list of calendars, you can use the following code:

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();
ListResponse<Calendars> calendars = nylas.calendars().list("GRANT_ID");
```

You might notice in the code above that there are some new concepts in the new SDK when making requests. These concepts are explained in more detail below.

### Resource Parameters

Each resource takes different parameters. All resources take an "identifier", which is the ID of the account you want to make the request for. This is usually the Grant ID or the email address of the account. Some resources also take "query parameters" which are mainly used to filter data or pass in additional information.

### Response Objects

The new Nylas API v3 now has standard response objects for all requests (excluding OAuth endpoints). There are generally two main types of response objects: `Response` and `ListResponse`.

The `Response` object is used for requests that return a single object, such as retrieving a single calendar. This returns a parameterized object of the type you are requesting (for example `Calendar`), and a string that represents the request ID.

The `ListResponse` object is used for requests that return a list of objects, such as retrieving a _list_ of calendars. This returns a list of parameterized objects of the type you are requesting (for example, `Calendar`), a string representing the request ID, and another string representing the token of the next page for paginating a request. <!-- Do we need to talk about changes to the default pagination behavior here? -->

### Error Objects

Like the response objects, Nylas v3 now has standard error objects for all requests (excluding OAuth endpoints). There are two superclass error classes, `AbstractNylasApiError`, used for errors returned by the API, and `AbstractNylasSdkError`, used for errors returned by the SDK.

The `AbstractNylasApiError` includes two subclasses: `NylasOAuthError`, used for API errors that are returned from the OAuth endpoints, and `NylasApiError`, used for any other Nylas API errors.

The error details are extracted from the response and stored in the error object along with the request ID and the HTTP status code.

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

The available authentication methods reflect the new Nylas API v3. While you can manage your application's integrations in the dashboard, you can manage almost everything else directly from the SDK. This includes managing grants, redirect URIs, OAuth tokens, and authenticating your users.

There are two main methods to focus on when authenticating users to your application. The first is the `Auth#urlForOAuth2` method, which returns the URL that you should redirect your users to in order to authenticate them using Nylas' OAuth 2.0 implementation.

The second is the `Auth#exchangeCodeForToken` method, which you use to exchange the code returned from the authentication redirect for an access token. You actually don't need to use the data from the response as you can use the authenticated email address directly as the identifier for the account. However, if you prefer to use the grant ID as the account identifier, you can extract the grant ID from the `CodeExchangeResponse` object and use that instead.

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

// Write code here to redirect the user to the url and parse the code
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
