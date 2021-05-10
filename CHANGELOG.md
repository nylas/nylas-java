# Nylas Java SDK Changelog

## [Unreleased]

This section contains changes that have been committed but not yet released.

### Added

- Calendar.isPrimary field is now available

### Changed

### Deprecated

- Deprecated Threads.setLabelIds method that takes an Iterable in favor for a Collection 

### Fixed

- Fixed Threads.setLabelIds to convert to JSON list properly 

### Removed

### Security

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

[Unreleased]: https://github.com/nylas/nylas-java/compare/v1.6.0...HEAD
[1.6.0]: https://github.com/nylas/nylas-java/releases/tag/v1.6.0
[1.5.0]: https://github.com/nylas/nylas-java/releases/tag/v1.5.0
[1.4.0]: https://github.com/nylas/nylas-java/releases/tag/v1.4.0
[1.3.0]: https://github.com/nylas/nylas-java/releases/tag/v1.3.0
[1.1.0]: https://github.com/nylas/nylas-java/releases/tag/v1.1.0
[1.0.1]: https://github.com/nylas/nylas-java/releases/tag/v1.0.1
[1.0.0]: https://github.com/nylas/nylas-java/releases/tag/v1.0.0
[0.2.0]: https://github.com/nylas/nylas-java/releases/tag/v0.2.0
[0.1.0]: https://github.com/nylas/nylas-java/releases/tag/v0.1.0
