# Nylas Java SDK Changelog

### [2.4.1] - Released 2024-07-26

### Changed
* Added missing `masterEventId` field in `Event` model
* Added missing `EWS` provider in `Provider` enum
* Fixed issue where the revoke OAuth token function was not working correctly

### [2.4.0] - Released 2024-06-28

### Added
* Added webhook validation support

### Changed
* Fixed issue where from field was not accessible when sending messages

### [2.3.2] - Released 2024-06-26

### Changed
* Fixed default When type
* Fixed issue where from field was not accessible when creating drafts

### [2.3.1] - Released 2024-06-10

### Changed
* Fixed code exchange response for scope value

### [2.3.0] - Released 2024-04-30

### Added
* Added missing webhook triggers
* Added provider field to token exchange response
* Added support for clean messages endpoint
* Added support for custom headers field for Drafts and Messages
* Added support for overriding various fields of outgoing requests

### Changed
* Fixed issue where attachments < 3mb were not being encoded correctly

## [2.2.1] - Released 2024-03-05

### Added
* Added support for `provider` field in code exchange response

### Changed
* Improved message sending and draft create/update performance
* Change default timeout to match API (90 seconds)

## [2.2.0] - Released 2024-02-27

### Added
* Added support for `roundTo` field in availability response
* Added support for `attributes` field in folder model
* Added support for icloud as an auth provider

### Changed
* Fixed builder for FindAttachmentQueryParams
* Fixed scopes to be optional for IMAP grants
* Fixed typo in updating grant schema
* Fixed endpoint for rotating webhook secrets
* Fixed response type for returning webhook IP addresses

### Removed
* Removed unnecessary `clientId` from detectProvider params

## [2.1.0] - Released 2024-02-12

### Added
* Add support for getting OAuth token info

### Changed
* Fix schema issues in the `Event`, `Message`, `Draft`, and `CodeExchangeResponse` models

## [2.0.0] - Released 2024-02-05

### BREAKING CHANGES

* Renamed artifact from `nylas-java-sdk` to `nylas`.
* Nylas SDK v2 supports the Nylas API v3 exclusively, dropping support for any endpoints that are not available in v3.
* Removed all REST calls from models and moved them directly into resources

### Added

* Full Kotlin support
* Created models for all API resources and endpoints, for all HTTP methods to reduce confusion on which fields are available for each endpoint
* Created error classes for the different API errors as well as SDK-specific errors

### Changed

* Leveraged Moshi annotations for JSON serialization/deserialization as opposed to manually writing JSON maps
* Removed all REST calls from models and moved them directly into resources

### Removed

* Non-builder ways for initializing `NylasClient`
* Local Webhook development support is removed due to incompatibility with the new API version

## [1.22.0] - Released 2024-01-29

### Added

* Added support for querying events using customer event ID
* Added support for overriding the Event ID when updating, for recurring events
* Added support for `reply_to_message_id` field in `Message`

## [1.21.0] - Released 2023-02-14

### Added

* Added missing `content_disposition` field in `File`
* Added toJSON() and toMap() support to account owned models
* Added scheduler support for the EU region

### Fixed

* Fixed NullPointerException sporadically occurring when calling `Message.toString()`

## [1.20.1] - Released 2023-02-09

### Changed

* Provide default implementations for `WebhookHandler` methods `onOpen`, `onClose`, and `onError`

### Fixed

* Fix `Tunnel.onMessage` not emitting individual deltas
* Change `java-websocket` dependency to `api` configuration

## [1.20.0] - Released 2023-02-06

### Added

* Added local webhook testing support
* Added new enums for `Webhook.Triggers` and `Webhook.State`

## [1.19.2] - Released 2023-01-24

### Fixed

* Fix `Event.hide_participants` not serializing
* Fix `Event.visibility` not serializing
* Fix `FreeBusy` not having a `calendar_id` field

## [1.19.1] - Released 2023-01-18

### Changed

* Added missing setters for `FreeBusy`

### Fixed

* Fixed typo in `Event.hide_participants`

## [1.19.0] - Released 2022-11-18

### Added

* Added support for calendar colors (for Microsoft calendars)
* Added support for rate limit errors

### Changed

* Set `Content-Type` and `Accept` headers on outgoing calls

### Fixed

* Fixed revoke access token always throwing an error
* Fixed participant status not serializing on Event creation


## [1.18.0] - Released 2022-10-06

### Added

* Added hide participants field for the `Event` class
* Added support for provider detection
* Added an enum for all known providers for native authentication
* Improved webhook notification support with the addition of more notification attributes and event metadata fields

### Changed

* NylasAccount.revokeAccessToken() returns a boolean value now

### Fixed

* Fix error when updating occurrence of a recurring event

## [1.17.0] - Released 2022-09-22

### Added

* Add support for getting a single expanded message and thread

### Fixed

* Fixed issue where `Event` participants could never be entirely removed once set

## [1.16.0] - Released 2022-07-29

### Added

* Add missing event query parameters
* Add support for Event reminders
* Add support for additional Event fields

## [1.15.0] - Released 2022-07-15

### Added

* Add `interval_minutes` field in Scheduler booking config
* Add `metadata` field to `JobStatus`

## [1.14.0] - Released 2022-06-14

### Added

* Add missing fields in Scheduler
* Add support for collective and group events
* Add support for calendar free-busy scope
* Add `redirect_on_error` parameter for Hosted Authentication

### Fixed

* Fixed enum value for `Scheduler.Config.Booking.OpeningHours.Days.Sunday`

## [1.13.1] - Released 2022-04-22

### Added

* Add missing `order` and `emails` fields in `Availability` and `TimeSlot`

### Fixed

* Fixed `Participant` status not being sent to the API when set

## [1.13.0] - Released 2022-03-31

### Added

- Added support for Delta
- Added support for new (beta) Integrations authentication (Integrations API, Grants API, Hosted Authentication for Integrations)
- Added `authentication_type` field to `Account`

### Changed

- Bump supported API version to v2.5

### Fixed

- Fixed incorrect property name for `Event.Notification.minutes_before_event`

## [1.12.0] - Released 2022-03-08

### Added

- Added Outbox support
- Added support for Component CRUD
- Added support for Neural Categorizer
- Added support for `calendar` field in free-busy, availability, and consecutive availability queries
- Added field for `phone_number` in `Participant`

### Changed

- Bump supported API version to v2.4

### Deprecated

- Deprecated `checkFreeBusy(Instant, Instant, List<String>)` and `checkFreeBusy(Instant, Instant, String)` in favour of `checkFreeBusy(FreeBusyQuery)`

### Fixed

- Fix null error message when `HostedAuthentication.fetchToken()` returns an API error

## [1.11.2] - Released 2022-02-01

### Removed

- Remove use of `okhttp3.internal` library

## [1.11.1] - Released 2022-01-24

### Fixed

- Fixed bug where an `IllegalArgumentException` is thrown when deserializing `Draft` with `metadata`

## [1.11.0] - Released 2022-01-20

### Added

- Added support for `Event` to ICS file generation
- Added support for calendar availability
- Added support for modifying `Folder`
- Added support for Scheduler API
- Expanded metadata support for `Calendar`, `Account`, `Message`, and `Draft`

## [1.10.3] - Released 2022-01-05

### Added

- Added support for the `forced_password` Hosted Auth setting
- Added missing `EMAIL` scope

### Fixed

- Fixed bug where saving an event without participants threw a `NullPointerException`

## [1.10.2] - Released 2021-12-23

### Changed

- Added `false` parameter when `notifyParticipants` is false

### Security

- Address major `log4j` vulnerability, updated `log4j` to v2.17.0

## [1.10.1] - Released 2021-12-13

### Security

- Address major `log4j` vulnerability, updated `log4j` to v2.15.0

## [1.10.0] - Released 2021-12-08

### Added

- Add support for automatic meeting details
- Add support for Event notifications

### Fixed

- Fix bug where updating an event resulted in an API error

## [1.9.1] - Released 2021-09-22

### Fixed

- Workaround for Send Raw MIME where server gives an error when charset is specified

## [1.9.0] - Released 2021-08-30

### Added

- Add support for Event conferencing
- Add support for Account deletion

### Deprecated

- MicrosoftExchangeProviderSettings easServerHost in favor of exchangeServerHost

## [1.8.0] - Released 2021-07-30

### Added

- Enabled support for Nylas API v2.2
- Add Event Metadata support
- Add support for new `RoomResource` fields
- Add missing getters for Event.Recurrence fields
- Add support for Neural API Sentiment Analysis, OCR, Signature Extraction, and Clean Conversations
- Add getters for Time.timezone, Timespan.start_timezone, Timespan.end_timezone

## [1.7.0] - Released 2021-05-19

### Added

- Calendar.isPrimary field is now available
- Event timezones will be checked for valid IANA zone names when created with ZonedDateTime objects

### Deprecated

- Deprecated Threads.setLabelIds method that takes an Iterable in favor for a Collection

### Fixed

- Fixed Threads.setLabelIds to convert to JSON list properly
- Fixed NullPointerException when deleting event from a virtual calendar

## [1.6.0] - Released 2021-04-24

### Changed

- `RequestFailedException` now creates a `Throwable` detail message with the details of the request failure.

### Deprecated

- Deprecated `RequestFailedException` methods `getResponseBody` and `getStatus`
  in favor of `getStatusCode`, `getErrorType` and `getErrorMessage`

## [1.5.0] - Released 2021-02-25

### Added

- Support for creating and updating Event timezones

### Deprecated

- Deprecated public use of `RestfulQuery` methods `addParameters` and `copyAtNewOffsetLimit`.
  They are for internal use only and will be removed from the public API in the future.

## [1.4.0] - Released 2020-12-11

### Added

- Calendar create, update, and delete support.
- Job status support.  Many objects returned by the server from create and update operations will now include a job
  status id which can be used to track the status of syncing those changes back to the provider.  Similarly, delete
  operations will directly return job status ids.

### Fixed

- Fix duplicate http logging when using NylasClient constructors (GH Issue #7)

## [1.3.0] - Released 2020-09-17

### Added

- Added Drafts.delete support
- NylasClient.Builder for better customization of NylasClient

### Changed

- Default NylasClient to use HTTP/1.1 to workaround OkHttp 3 bug.

### Deprecated

- Deprecated non-default NylasClient constructors

## [1.1.0] - Released 2020-07-28

Release 1.1.0 to customize client HTTP configuration

### Added

- Allow customizing configuration of the HTTP client passed to NylasClient constructor.

## [1.0.1] - Released 2020-06-17

Release 1.0.1 to address a major bug.

### Added

- Constructor for com.nylas.Event.Recurrence to allow creation of event recurrences. (GH Issue #4)

## [1.0.0] - Released 2020-06-13

Release 1.0.0 after users have been using it successfully.

### Changed

- [BREAKING] Moved AuthenticationUrlBuilder to nested class HostedAuthentication.UrlBuilder

## [0.2.0] - Released 2020-04-27

This second release aims toward API stability so that we can get to v1.0.0.

### Added

- Build artifact now includes a build.properties file and transmits a build id in X-Nylas-Commit-Hash http header
- Webhooks collection now supports pagination

### Changed

- [BREAKING] List/query methods now return objects of type com.nylas.RemoteCollection (instead of java.util.List)
  which support lazy iteration of results fetched from the server in batches of 100 (by default), or eagerly
  fetching all via fetchAll method
- [BREAKING] Updated timestamp/date apis to use standard java.time.Instant and java.time.LocalDate
- Other minor fixes

### Removed

- [BREAKING] Removed unsupported AccessToken.token_type field

## [0.1.0] - Released 2020-02-25

Initial preview release

[Unreleased]: https://github.com/nylas/nylas-java/compare/v2.4.1...HEAD
[2.4.1]: https://github.com/nylas/nylas-java/releases/tag/v2.4.1
[2.4.0]: https://github.com/nylas/nylas-java/releases/tag/v2.4.0
[2.3.2]: https://github.com/nylas/nylas-java/releases/tag/v2.3.2
[2.3.1]: https://github.com/nylas/nylas-java/releases/tag/v2.3.1
[2.3.0]: https://github.com/nylas/nylas-java/releases/tag/v2.3.0
[2.2.1]: https://github.com/nylas/nylas-java/releases/tag/v2.2.1
[2.2.0]: https://github.com/nylas/nylas-java/releases/tag/v2.2.0
[2.1.0]: https://github.com/nylas/nylas-java/releases/tag/v2.1.0
[2.0.0]: https://github.com/nylas/nylas-java/releases/tag/v2.0.0
[1.21.0]: https://github.com/nylas/nylas-java/releases/tag/v1.21.0
[1.20.1]: https://github.com/nylas/nylas-java/releases/tag/v1.20.1
[1.20.0]: https://github.com/nylas/nylas-java/releases/tag/v1.20.0
[1.19.2]: https://github.com/nylas/nylas-java/releases/tag/v1.19.2
[1.19.1]: https://github.com/nylas/nylas-java/releases/tag/v1.19.1
[1.19.0]: https://github.com/nylas/nylas-java/releases/tag/v1.19.0
[1.18.0]: https://github.com/nylas/nylas-java/releases/tag/v1.18.0
[1.17.0]: https://github.com/nylas/nylas-java/releases/tag/v1.17.0
[1.16.0]: https://github.com/nylas/nylas-java/releases/tag/v1.16.0
[1.15.0]: https://github.com/nylas/nylas-java/releases/tag/v1.15.0
[1.14.0]: https://github.com/nylas/nylas-java/releases/tag/v1.14.0
[1.13.1]: https://github.com/nylas/nylas-java/releases/tag/v1.13.1
[1.13.0]: https://github.com/nylas/nylas-java/releases/tag/v1.13.0
[1.12.0]: https://github.com/nylas/nylas-java/releases/tag/v1.12.0
[1.11.2]: https://github.com/nylas/nylas-java/releases/tag/v1.11.2
[1.11.1]: https://github.com/nylas/nylas-java/releases/tag/v1.11.1
[1.11.0]: https://github.com/nylas/nylas-java/releases/tag/v1.11.0
[1.10.3]: https://github.com/nylas/nylas-java/releases/tag/v1.10.3
[1.10.2]: https://github.com/nylas/nylas-java/releases/tag/v1.10.2
[1.10.1]: https://github.com/nylas/nylas-java/releases/tag/v1.10.1
[1.10.0]: https://github.com/nylas/nylas-java/releases/tag/v1.10.0
[1.9.1]: https://github.com/nylas/nylas-java/releases/tag/v1.9.1
[1.9.0]: https://github.com/nylas/nylas-java/releases/tag/v1.9.0
[1.8.0]: https://github.com/nylas/nylas-java/releases/tag/v1.8.0
[1.7.0]: https://github.com/nylas/nylas-java/releases/tag/v1.7.0
[1.6.0]: https://github.com/nylas/nylas-java/releases/tag/v1.6.0
[1.5.0]: https://github.com/nylas/nylas-java/releases/tag/v1.5.0
[1.4.0]: https://github.com/nylas/nylas-java/releases/tag/v1.4.0
[1.3.0]: https://github.com/nylas/nylas-java/releases/tag/v1.3.0
[1.1.0]: https://github.com/nylas/nylas-java/releases/tag/v1.1.0
[1.0.1]: https://github.com/nylas/nylas-java/releases/tag/v1.0.1
[1.0.0]: https://github.com/nylas/nylas-java/releases/tag/v1.0.0
[0.2.0]: https://github.com/nylas/nylas-java/releases/tag/v0.2.0
[0.1.0]: https://github.com/nylas/nylas-java/releases/tag/v0.1.0
