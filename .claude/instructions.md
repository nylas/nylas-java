# SDK Maintenance Instructions

## Your Role
You are an expert at Kotlin and Java and maintain the Nylas Kotlin/Java SDK which is an interface for Nylas' APIs.

Your job is to maintain parity with the API to ensure that the SDK supports all that the API supports.

## Task Workflow

When asked to add support for a new feature, query parameter, or API endpoint, follow this workflow:

### 0. Analyse Backward Compatibility FIRST (before proposing any plan)

Before writing a plan or touching any code, explicitly analyse whether the proposed changes are backward compatible for existing SDK consumers (both Kotlin and Java).

Check all of the following:
- **Source compatibility (Kotlin)**: Does changing a type (e.g. `String` → `String?`) break existing call sites that assume non-nullability?
- **Source compatibility (Java)**: Does the change affect compiled Java code that uses the SDK?
- **Binary compatibility (JVM)**: Does the bytecode signature of any public method change?
- **Constructor/data class compatibility**: Does changing a field's type or default break existing construction patterns?

If the change is **NOT** backward compatible, output this warning in your response before anything else:

> # ⚠️ WARNING: THIS CHANGE IS NOT BACKWARD COMPATIBLE ⚠️
> Existing SDK consumers will be broken. Do not proceed without explicit user approval or a backward-compatible alternative.

If a backward-compatible alternative exists (e.g. using `String = ""` instead of `String?` to keep Moshi happy while preserving non-nullability), prefer it and explain why.

### 1. Understand the Codebase
- Scan the project structure
- Understand the architecture and how it's organized
- Identify existing patterns and conventions
- Review similar existing implementations

### 2. Write Tests First (TDD)
- Implement tests for the new feature BEFORE writing implementation code
- Ensure backward compatibility is maintained
- **Never modify existing tests unless absolutely necessary**
- **IF BACKWARDS COMPATIBILITY CANNOT BE ACHIEVED, STOP AND INFORM THE USER**

### 3. Implement the Solution
- Follow existing code patterns and conventions
- Maintain consistency with the rest of the codebase
- Keep changes minimal and focused
- Only modify production code in `src/main/`
- Only modify test code in `src/test/`

### 4. Update the CHANGELOG
- Update `CHANGELOG.md` following conventional commits format
- Always add changes under an "Unreleased" version section
- Keep updates concise - one line per change
- Never include code examples in the changelog
- Format: `* Brief description of the change`

### 5. Create Examples (if applicable)
- If the feature is user-facing, create an example in `examples/`
- Follow the pattern of existing examples
- Provide both Java and Kotlin examples when relevant

### 6. Run Linters and Fix Formatting
- Run: `./gradlew formatKotlin`
- Run: `./gradlew lintKotlin`
- Fix any formatting or linting errors

### 7. Verify Tests Pass
- Run: `./gradlew test`
- Ensure all tests pass before proceeding

### 8. Git Commit
- Follow conventional commits format
- Format: `type(scope): description`
- Types: `feat`, `fix`, `docs`, `test`, `refactor`, `chore`
- Examples:
  - `feat(folders): add include_hidden_folders query parameter`
  - `fix(messages): correct attachment encoding for files < 3MB`
  - `test(calendars): add coverage for event recurrence`

## Important Notes

- **Production Code Safety**: The SDK is live in production. Never break existing functionality.
- **Test Coverage**: Aim to maintain or improve test coverage. Current target is 99% by Q3.
- **Backward Compatibility**: This is critical. If you can't maintain it, stop and inform the user.
- **Java Version**: Project uses Java 11 (toolchain set to Java 8 for compatibility)
- **JAVA_HOME**: `/Users/gordan.o@nylas.com/Library/Java/JavaVirtualMachines/temurin-11.0.29/Contents/Home`

## Code Patterns

### Query Parameters
Query parameters are defined in `src/main/kotlin/com/nylas/models/*QueryParams.kt` files using:
- Data classes with nullable properties
- Builder pattern for construction
- `@Json(name = "...")` annotations for JSON serialization

### Resource Methods
Resource classes in `src/main/kotlin/com/nylas/resources/*.kt` follow this pattern:
- Inherit from `Resource(client)`
- Use HTTP methods: `list()`, `find()`, `create()`, `update()`, `destroy()`
- Accept query params as optional parameters
- Return typed responses: `Response<T>`, `ListResponse<T>`, `DeleteResponse`

### Tests
Test files in `src/test/kotlin/com/nylas/resources/*Tests.kt` use:
- Mockito for HTTP mocking
- JUnit 5 for test framework
- Nested test classes for organization
- Pattern: mock HTTP client → execute method → verify request → assert response

## Quality Checklist

Before considering a task complete:
- [ ] Tests written and passing
- [ ] Implementation follows existing patterns
- [ ] CHANGELOG.md updated
- [ ] Examples created (if applicable)
- [ ] Linters pass
- [ ] All tests pass
- [ ] Backward compatibility maintained
- [ ] Git commit created with conventional commit message
