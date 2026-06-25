<div align="center">
  <a href="https://www.nylas.com/">
    <img width="100%" alt="Nylas" src="https://github.com/user-attachments/assets/137517ae-244d-47a5-8ca7-b12984971fc4" />
  </a>

  <h1>Nylas Kotlin & Java SDK</h1>

  <p>
    <strong>The official Kotlin & Java SDK for Nylas — the infrastructure that powers communications</strong>
  </p>

  <p>
    <a href="https://central.sonatype.com/artifact/com.nylas.sdk/nylas"><img src="https://img.shields.io/maven-central/v/com.nylas.sdk/nylas" alt="version" /></a>
    <a href="https://codecov.io/gh/nylas/nylas-java"><img src="https://codecov.io/gh/nylas/nylas-java/graph/badge.svg?token=R94RD91GZ3" alt="code coverage" /></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="license" /></a>
  </p>

  <p>
    <a href="https://developer.nylas.com/docs/v3/sdks/kotlin-java/">📖 SDK Guide</a> ·
    <a href="https://developer.nylas.com/docs/api/v3/">📚 API Reference</a> ·
    <a href="https://dashboard-v3.nylas.com/register">🚀 Sign up</a> ·
    <a href="https://github.com/orgs/nylas-samples/repositories?q=java">💡 Samples</a> ·
    <a href="https://forums.nylas.com">💬 Forum</a>
  </p>
</div>

<br />

The official Kotlin & Java SDK for [Nylas](https://developer.nylas.com/docs/v3/) — the infrastructure that powers communications. Integrate with Gmail, Microsoft, IMAP, Zoom, and 250+ email, calendar, and meeting providers in 5 minutes. Covers [Email](https://developer.nylas.com/docs/v3/email/), [Calendar](https://developer.nylas.com/docs/v3/calendar/), [Contacts](https://developer.nylas.com/docs/v3/email/contacts/), [Scheduler](https://developer.nylas.com/docs/v3/scheduler/), [Notetaker](https://developer.nylas.com/docs/v3/notetaker/), and [Agent Accounts](https://developer.nylas.com/docs/v3/agent-accounts/).

This repository is for contributors and anyone installing the SDK from source. If you just want to use the SDK in your app, head straight to the **[Kotlin & Java SDK guide](https://developer.nylas.com/docs/v3/sdks/kotlin-java/)** on developer.nylas.com.

## Get started

1. [Sign up for a free Nylas account](https://dashboard-v3.nylas.com/register).
2. Follow the [getting started guide](https://developer.nylas.com/docs/v3/getting-started/) to create an application and provision your first API key.
3. Bootstrap a project with the Nylas CLI:

   ```bash
   brew install nylas/nylas-cli/nylas
   nylas init
   ```

## ⚙️ Install

> **Requirements:** Java 8 or later. Kotlin 1.8 or later.

### Gradle

Kotlin DSL (`build.gradle.kts`):

```kotlin
implementation("com.nylas.sdk:nylas:2.17.1")
```

Groovy (`build.gradle`):

```groovy
implementation 'com.nylas.sdk:nylas:2.17.1'
```

### Maven

```xml
<dependency>
  <groupId>com.nylas.sdk</groupId>
  <artifactId>nylas</artifactId>
  <version>2.17.1</version>
</dependency>
```

### Build from source

```bash
git clone https://github.com/nylas/nylas-java.git
cd nylas-java
./gradlew build uberJar
```

This produces `build/libs/nylas-java-sdk-<version>-uber.jar`.

## ⚡️ Usage

Initialize the client with your API key:

```java
import com.nylas.NylasClient;
import com.nylas.models.Calendar;
import com.nylas.models.ListResponse;

NylasClient nylas = new NylasClient.Builder("NYLAS_API_KEY").build();

ListResponse<Calendar> calendars = nylas.calendars().list("GRANT_ID");
```

For step-by-step walkthroughs, see the developer guides:

- [Send and receive email](https://developer.nylas.com/docs/v3/email/)
- [Read and manage calendar events](https://developer.nylas.com/docs/v3/calendar/)
- [Find available meeting times with Scheduler](https://developer.nylas.com/docs/v3/scheduler/)
- [Capture meeting notes with Notetaker](https://developer.nylas.com/docs/v3/notetaker/)
- [Manage Agent Accounts](https://developer.nylas.com/docs/v3/agent-accounts/)
- [Subscribe to webhooks and notifications](https://developer.nylas.com/docs/v3/notifications/)
- [Choose a data residency region](https://developer.nylas.com/docs/dev-guide/platform/data-residency/)

### Error handling

Nylas API errors extend `AbstractNylasApiError` (e.g. `NylasApiError`, `NylasOAuthError`). SDK-side errors extend `AbstractNylasSdkError` (e.g. `NylasSdkTimeoutError`, `NylasSdkRemoteClosedError`).

```java
import com.nylas.models.AbstractNylasApiError;
import com.nylas.models.AbstractNylasSdkError;
import com.nylas.models.NylasApiError;

try {
  nylas.calendars().list("GRANT_ID");
} catch (NylasApiError e) {
  System.err.println("API error " + e.getStatusCode() + ": " + e.getMessage());
  System.err.println("Request ID: " + e.getRequestId());
} catch (AbstractNylasSdkError e) {
  System.err.println("SDK error: " + e.getMessage());
}
```

### Logging

The SDK uses [SLF4J](http://www.slf4j.org). The HTTP client exposes three DEBUG-level loggers:

- `com.nylas.http.Summary` — one line per request/response (method, URI, status, size, duration)
- `com.nylas.http.Headers` — request/response headers (Authorization redacted by default)
- `com.nylas.http.Body` — request/response bodies (first 10 kB by default)

Enable them with your logging framework, e.g. log4j2:

```xml
<Logger name="com.nylas" level="DEBUG"/>
```

Customize redaction and body-size limits by passing your own `HttpLoggingInterceptor` to `NylasClient.Builder`.

## 💡 Examples

Runnable Java and Kotlin examples live in [`examples/`](examples) (folders, events, messages, large attachments, Notetaker). For full apps, browse [Java repos in nylas-samples](https://github.com/orgs/nylas-samples/repositories?q=java).

## 🤖 AI agents

[nylas/skills](https://github.com/nylas/skills) drops Nylas into Claude Code, Cursor, Codex, and other agents that support the skills format:

```bash
npx skills add nylas/skills
/plugin marketplace add nylas/skills   # Claude Code
```

The CLI also installs an MCP server for Claude Desktop, Claude Code, Cursor, Windsurf, or VS Code:

```bash
brew install nylas/nylas-cli/nylas
nylas mcp install
```

Walkthrough: [give AI agents email access via MCP](https://cli.nylas.com/guides/give-ai-agents-email-access-via-mcp).

## 📚 Reference

- [Kotlin & Java SDK guide](https://developer.nylas.com/docs/v3/sdks/kotlin-java/)
- [API reference](https://developer.nylas.com/docs/api/v3/)
- [Getting started](https://developer.nylas.com/docs/v3/getting-started/)
- [Javadoc / Dokka SDK reference](https://nylas-java-sdk-reference.pages.dev/)
- [Data residency](https://developer.nylas.com/docs/dev-guide/platform/data-residency/)
- [Nylas CLI](https://cli.nylas.com)
- [Dashboard](https://dashboard-v3.nylas.com)

## ✨ Upgrading

See [CHANGELOG.md](CHANGELOG.md) for release notes and [UPGRADE.md](UPGRADE.md) for migration instructions between major versions.

## 💙 Contributing

We welcome questions, bug reports, and pull requests. See [Contributing.md](Contributing.md) for how to get involved, or ask in the [Nylas forum](https://forums.nylas.com).

## 🔒 Security

Found a security issue? Please follow the [Nylas vulnerability disclosure policy](https://www.nylas.com/security/vulnerability-disclosure-policy/) instead of opening a public issue.

## 🔗 Other Nylas SDKs

- [nylas-nodejs](https://github.com/nylas/nylas-nodejs)
- [nylas-python](https://github.com/nylas/nylas-python)
- [nylas-ruby](https://github.com/nylas/nylas-ruby)

## 📝 License

This project is licensed under the terms of the [MIT license](LICENSE).
