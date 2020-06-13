# Nylas Java SDK Changelog

## [Unreleased]

This section contains changes that have been committed but not yet released.

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

[Unreleased]: https://github.com/nylas/nylas-java/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/nylas/nylas-java/releases/tag/v1.0.0
[0.2.0]: https://github.com/nylas/nylas-java/releases/tag/v0.2.0
[0.1.0]: https://github.com/nylas/nylas-java/releases/tag/v0.1.0
