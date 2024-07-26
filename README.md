<a href="https://www.nylas.com/">
    <img src="https://brand.nylas.com/assets/downloads/logo_horizontal_png/Nylas-Logo-Horizontal-Blue_.png" alt="Aimeos logo" title="Aimeos" align="right" height="60" />
</a>

# Nylas SDK for Kotlin & Java

![Maven Central Version](https://img.shields.io/maven-central/v/com.nylas.sdk/nylas)
[![codecov](https://codecov.io/gh/nylas/nylas-java/graph/badge.svg?token=R94RD91GZ3)](https://codecov.io/gh/nylas/nylas-java)

This is the GitHub repository for the Nylas SDK for Kotlin and Java. This repo is primarily for anyone who wants to make contributions to the SDK or install it from source. For documentation on how to use this SDK to access the Nylas Email, Calendar, or Contacts APIs, see the official [Java SDK Quickstart Guide](https://developer.nylas.com/docs/sdks/java/).

The Nylas Communications Platform provides REST APIs for [Email](https://developer.nylas.com/docs/email/), [Calendar](https://developer.nylas.com/docs/calendar/), and [Contacts](https://developer.nylas.com/docs/contacts/), and the Nylas SDK is the quickest way to build your integration using Kotlin or Java.

Here are some resources to help you get started:

- [Sign up for your free Nylas account](https://dashboard.nylas.com/register)
- [Nylas API v3 Quickstart Guide](https://developer.nylas.com/docs/v3-beta/v3-quickstart/)
- [Nylas SDK Reference](https://nylas-java-sdk-reference.pages.dev/)
- [Nylas API Reference](https://developer.nylas.com/docs/api/)
- [Nylas Samples repo for code samples and example applications](https://github.com/orgs/nylas-samples/repositories?q=&type=all&language=java)

If you have a question about the Nylas Communications Platform, [contact Nylas Support](https://support.nylas.com/) for help.

## ⚙️ Install

**Note:** The Nylas SDK for Kotlin & Java requires JRE 8 or later.

### Set up using Gradle

If you're using Gradle, add the following to the dependencies section of `build.gradle`:

```groovy
implementation("com.nylas.sdk:nylas:2.4.1")
```

### Build from source

To build from source, clone this repo and build the project with Gradle.

```shell
git clone https://github.com/nylas/nylas-java.git && cd nylas-java
./gradlew build uberJar
```

This creates a new jar file in `build/libs/nylas-java-sdk-2.4.1-uber.jar`.

See the Gradle documentation on [Building Libraries](https://guides.gradle.org/building-java-libraries/)
or the [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html) for more information.

## ⚡️Usage

To use this SDK, you must first [get a free Nylas account](https://dashboard.nylas.com/register).

Then, follow the Quickstart guide to [set up your first app and get your API keys](https://developer.nylas.com/docs/v3-beta/v3-quickstart/).

For code examples that demonstrate how to use this SDK, take a look at our [Java repos in the Nylas Samples collection](https://github.com/orgs/nylas-samples/repositories?q=&type=all&language=java).

### 🚀 Making Your First Request

You use the `NylasClient` object to make requests to the Nylas API. The SDK is organized into different resources, each of which has methods to make requests to the API. Each resource is available through the `NylasClient` object configured with your API key.

For example, to get a list of calendars, you can use the following code:

```java
NylasClient nylas = new NylasClient.Builder("API_KEY").build();
ListResponse<Calendars> calendars = nylas.calendars().list("GRANT_ID");
```

## 📚 Documentation

Nylas maintains a [reference guide for the Kotlin and Java SDK](https://nylas-java-sdk-reference.pages.dev/) to help you get familiar with the available methods and classes.

## ✨ Upgrading from 1.x

See [UPGRADE.md](UPGRADING.md) for instructions on upgrading from 1.x to 2.x.

**Note**: The Kotlin/Java SDK 2.x is not compatible with the Nylas API earlier than v3-beta.

## 🪵 Logging

The SDK uses [SLF4J](http://www.slf4j.org) for logging. If you are using the SDK you can [choose what logging framework to use with it](http://www.slf4j.org/manual.html#projectDep) for your app.

Common choices are log4j, logback, java.util.logging. If the application doesn't specify any logging framework,
SLF4J emits one single warning and is then completely silent.

By default, the HTTP client is configured with the `com.nylas.HttpLoggingInterceptor`
which provides three DEBUG level loggers for HTTP requests.

- `com.nylas.http.Summary` logs one line for each request, containing the method, URI, and content size,
and one line for each response containing the status code, message, content size and duration.
- `com.nylas.http.Headers` logs the request and response HTTP headers (except Authorization value by default).
- `com.nylas.http.Body` logs request and response bodies (first 10kB by default).

You can enable or disable these loggers using whatever logging framework you use.

For example, if you're using log4j2 and with an xml configuration file, include this line to enable all three:
`<Logger name="com.nylas" level="DEBUG"/>`

You can configure how you log HTTP Authorization header values and the body size limit using a
`NylasClient.Builder` with a customized `HttpLoggingInterceptor`

## 💙 Contributing

We value and appreciate contributors' time! We welcome questions, bug reports, and pull requests.

See the [Contributing](Contributing.md) for information about how to make contributions to this project.

## 📝 License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) for the full terms.
