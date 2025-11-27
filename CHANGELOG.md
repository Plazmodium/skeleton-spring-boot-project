Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog and this project adheres to Semantic Versioning (SemVer): MAJOR.MINOR.PATCH.

Unreleased
----------
- Placeholder for upcoming changes.

0.1.0 — 2025-11-27
-------------------
Added
- Initial SpringRestSkeleton release with layered architecture (controller, service, repository, domain, config).
- SampleEntity CRUD API with validation and standardized error handling.
- JPA/Hibernate persistence with H2 for dev/test and Flyway baseline migration.
- OpenAPI 3 documentation with Swagger UI and annotations on controllers/DTOs.
- Toggleable Spring Security with JWT support and token mint endpoint for dev/test.
- Actuator health endpoint and structured logging configuration.
- Profiles for dev/test/prod with sensible defaults.
- Example GitHub Actions CI workflow (build, test, package).
- Internationalization hooks for error messages.
- Comprehensive unit, slice, and integration test examples.
- Documentation: Requirements, Plan, Tasks, README with compatibility matrix and run instructions.

Changed
- N/A (first tagged version).

Removed
- N/A.
