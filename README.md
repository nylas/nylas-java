<a href="https://www.nylas.com/">
    <img src="https://brand.nylas.com/assets/downloads/logo_horizontal_png/Nylas-Logo-Horizontal-Blue_.png" alt="Aimeos logo" title="Aimeos" align="right" height="60" />
</a>

# Nylas SDK for Kotlin & Java
This is the GitHub repository for the Nylas SDK for Kotlin & Java and this repo is primarily for anyone who wants to make contributions to the SDK or install it from source. If you are looking to use this SDK to access the Nylas Email, Calendar, or Contacts API you should refer to our official [Java SDK Quickstart Guide](https://developer.nylas.com/docs/sdks/java/).

The Nylas Communications Platform provides REST APIs for [Email](https://developer.nylas.com/docs/email/), [Calendar](https://developer.nylas.com/docs/calendar/), and [Contacts](https://developer.nylas.com/docs/contacts/), and the Nylas SDK is the quickest way to build your integration using Kotlin or Java.

Here are some resources to help you get started:

- [Sign up for your free Nylas account](https://dashboard.nylas.com/register)
- [Nylas API v3 Quickstart Guide](https://developer.nylas.com/docs/v3-beta/v3-quickstart/)
- [Nylas SDK Reference](https://nylas-java-sdk-reference.pages.dev/)
- [Nylas API Reference](https://docs.nylas.com/reference)
- [Nylas Samples repo for code samples and example applications](https://github.com/orgs/nylas-samples/repositories?q=&type=all&language=java)

If you have a question about the Nylas Communications Platform, please reach out to support@nylas.com to get help.

## ⚙️ Install
**Note:** The Nylas SDK for Kotlin & Java requires JRE 8 or above.

**Setup via Gradle**: If you're using Gradle, add the following to your dependencies section of build.gradle:

```groovy
implementation("com.nylas.sdk:nylas:2.0.0-beta.1")
```
    
**Build from source**: To build from source, clone this repo and build the project with Gradle.

```shell
git clone https://github.com/nylas/nylas-java.git && cd nylas-java
./gradlew build uberJar
```

This will create a new jar file in `build/libs/nylas-java-sdk-2.0.0-beta.1-uber.jar`.

See Gradle documentation on [Building Libraries](https://guides.gradle.org/building-java-libraries/)
or the [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html) for more information.

## ⚡️Usage

To use this SDK, you first need to [sign up for a free Nylas account](https://dashboard.nylas.com/register).

Then, follow our guide to [setup your first app and get your API access keys](https://developer.nylas.com/docs/v3-beta/v3-quickstart/#update-or-create-test-provider-applications).

For code examples that demonstrate how to use this SDK, take a look at our [Nylas Samples repo](https://github.com/orgs/nylas-samples/repositories?q=&type=all&language=java).

### 🚀 Making Your First Request

Your `NylasClient` object is what you use to make requests to the Nylas API. The SDK is organized into different resources, each of which has methods to make requests to the API. Each resource is available through the `NylasClient` object configured with you API key.

For example, to get a list of calendars, you can use the following code:

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();
ListResponse<Calendars> calendars = nylas.calendars().list("GRANT_ID");
```

## 📚 Documentation
We have provided an SDK reference guide to help you get familiar with the available methods and classes. You can find the latest documentation here: [Nylas SDK Reference](https://nylas-java-sdk-reference.pages.dev/).

## ✨ Upgrading from 1.x

Please refer to [UPGRADING.md](UPGRADING.md) for information about how to upgrade from 1.x to 2.x. Please note that 2.x is not compatible with the Nylas API < 3.0.

## 🪵 Logging

The SDK uses [SLF4J](http://www.slf4j.org) for logging.  Applications using the SDK can
[choose what logging framework to use with it](http://www.slf4j.org/manual.html#projectDep).
Common choices are log4j, logback, java.util.logging. If the application doesn't specify any logging framework,
then SLF4J will emit one warning and then be completely silent.

By default, the HTTP client is configured with the `com.nylas.HttpLoggingInterceptor`
which provides 3 loggers for HTTP requests that only log at DEBUG level.
- `com.nylas.http.Summary` logs one line for each request, containing method, URI, and content size
and one line for each response containing status code, message, content size and duration.
- `com.nylas.http.Headers` logs the request and response HTTP headers (except Authorization value by default).
- `com.nylas.http.Body` logs request and response bodies (first 10kB by default).

Enabling or disabling those loggers is done via the logging framework being used.
For example, if using log4j2 and with an xml configuration file, include this line to enable all three:
`<Logger name="com.nylas" level="DEBUG"/>`

Configuring the logging of the HTTP Authorization header values and the body size limit can be done by using a 
`NylasClient.Builder` with a customized `HttpLoggingInterceptor`

## 💙 Contributing

Please refer to [Contributing](Contributing.md) for information about how to make contributions to this project. We welcome questions, bug reports, and pull requests.

## 📝 License

This project is licensed under the terms of the MIT license. Please refer to [LICENSE](LICENSE) for the full terms. 
