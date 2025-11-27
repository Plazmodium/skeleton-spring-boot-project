# Development Tasks Checklist

This checklist is derived from the Implementation Plan (`docs/plan.md`) and Requirements (`docs/requirements.md`).
Mark items with [x] when done. Keep links to Plan and Requirement IDs intact.

## Phase 1 — Project Setup & Foundations
1. [x] Initialize Spring Boot project structure; set Java 21 toolchain; configure group/artifact; ensure layered packages (controller, service, repository, domain, config). (Plan: PLAN-1.1; Reqs: 1, 3)
2. [x] Add Gradle dependencies: Spring Web, Spring Data JPA, H2 (dev/test), validation, Lombok (optional), testing stack (JUnit 5, Mockito), Actuator (future), Springdoc OpenAPI. (Plan: PLAN-1.1, PLAN-4.1, PLAN-6.1; Reqs: 1, 3, 5, 9)
3. [x] Configure Spring profiles `dev`, `test`, `prod`; externalize common properties and per‑profile overrides. (Plan: PLAN-1.3; Reqs: 8)
4. [x] Set up basic application bootstrap with `Main` class and banner/logging defaults. (Plan: PLAN-1.1; Reqs: 9)
5. [x] Establish test infrastructure: JUnit platform config, Mockito, test resources, base `@SpringBootTest` setup. (Plan: PLAN-1.2; Reqs: 6)
6. [x] Define and document the Java/Gradle/Spring Boot compatibility matrix and troubleshooting (README); include IDE Gradle JVM and CLI `JAVA_HOME` instructions. (Plan: PLAN-1.4; Reqs: 13)
7. [x] Verify Gradle runs on a supported JVM and project sync/build succeeds in CLI and IDE; record results. (Plan: PLAN-1.4; Reqs: 13)

## Phase 2 — Persistence & Migrations
8. [x] Create `SampleEntity` JPA entity with fields: id (auto), name, description, createdAt. (Plan: PLAN-2.1; Reqs: 1, 3)
9. [x] Define `SampleEntityRepository` extending Spring Data repository; enable pagination. (Plan: PLAN-2.1; Reqs: 1, 3)
10. [x] Integrate Flyway or Liquibase; add baseline migration creating `sample_entity` table with indexes; ensure auto‑run on startup. (Plan: PLAN-2.2; Reqs: 4)
11. [x] Configure H2 for `dev`/`test` and placeholders for PostgreSQL/MySQL for `prod`. (Plan: PLAN-2.1; Reqs: 3, 8)
12. [x] Add startup checks/logging for DB connectivity and fail‑fast on migration conflicts with actionable messages. (Plan: PLAN-2.3; Reqs: 12)

## Phase 3 — API & Validation
13. [x] Implement service layer for `SampleEntity` with CRUD operations and transactional boundaries. (Plan: PLAN-3.1; Reqs: 1, 3)
14. [x] Implement controller at `/api/v1/sample-entities` supporting: Create (POST), Read by id (GET /{id}), List with pagination (GET), Update (PUT/PATCH), Delete (DELETE). (Plan: PLAN-3.1; Reqs: 1)
15. [x] Add DTOs and mappers between entity and API models; prevent exposing persistence internals. (Plan: PLAN-3.1; Reqs: 1)
16. [x] Add Bean Validation annotations (e.g., `@NotBlank`, length limits) and a global `@ControllerAdvice` for standardized error responses. (Plan: PLAN-3.2; Reqs: 2)
17. [x] Define a consistent error model (code, message, details) and map common exceptions (MethodArgumentNotValid, ConstraintViolation, HttpMessageNotReadable). (Plan: PLAN-3.2; Reqs: 2)

## Phase 4 — API Documentation
18. [x] Add Springdoc OpenAPI; generate OpenAPI spec and expose Swagger UI; annotate controllers/DTOs; include error schemas. (Plan: PLAN-4.1; Reqs: 5)
19. [x] Add basic unit tests for Swagger generation and validation. (Plan: PLAN-4.1; Reqs: 5)
20. [x] Make sure that swagger-ui.html is accessible at `/swagger-ui.html`. (Plan: PLAN-4.1; Reqs: 5)
21. [x] Made sure that swagger-ui.html is accessible at `/api/v1/swagger-ui.html`. (Plan: PLAN-4.1; Reqs: 5)
22. [x] Add integration tests for Swagger UI. (Plan: PLAN-4.1; Reqs: 5)
23. [x] Add basic integration tests for API endpoints. (Plan: PLAN-4.1; Reqs: 5)
24. [x] Add basic integration tests for error responses. (Plan: PLAN-4.1; Reqs: 5)
25. [x] Add basic integration tests for OpenAPI spec. (Plan: PLAN-4.1; Reqs: 5)

## Phase 5 — Security (JWT‑ready)
19. [x] Add Spring Security dependency and minimal configuration: permit GETs by default; require auth for POST/PUT/DELETE on `/api/v1/**`. (Plan: PLAN-5.1; Reqs: 7)
20. [x] Provide JWT authentication filter/configuration stubs and properties placeholders; allow easy enable/disable per profile. (Plan: PLAN-5.1; Reqs: 7, 8)
21. [x] Add JWT token generation and validation logic; protect API endpoints with JWT auth. (Plan: PLAN-5.2; Reqs: 7)
22. [x] Dependencies chosen should not have any security vulnerabilities. (Plan: PLAN-5.1; Reqs: 7)

## Phase 6 — Observability & Health
21. [x] Enable Actuator endpoints (`/actuator/health`, basic metrics); configure exposure in `dev` vs `prod`. (Plan: PLAN-6.1; Reqs: 9)
22. [x] Configure structured logging pattern and log levels; ensure key events (migrations, errors) are logged. (Plan: PLAN-6.1; Reqs: 9, 12)

## Phase 7 — CI/CD Readiness
23. [x] Add example GitHub Actions workflow (build, test, package). (Plan: PLAN-7.1; Reqs: 10)

## Phase 8 — Internationalization Hooks
24. [x] Add `messages.properties` (en) and wire `MessageSource`; use codes in error responses with locale awareness. (Plan: PLAN-8.1; Reqs: 11)

## Phase 9 — Testing & Hardening
25. [x] Unit tests: service layer (happy paths, edge cases) using Mockito. (Plan: PLAN-9.1; Reqs: 6)
26. [x] Controller tests: MockMvc tests for CRUD, validation errors, and error model structure. (Plan: PLAN-9.1; Reqs: 1, 2, 6)
27. [x] Repository tests: JPA/H2 tests verifying persistence and pagination. (Plan: PLAN-9.1; Reqs: 3, 6)
28. [x] Integration tests: end‑to‑end CRUD flow against H2 or Testcontainers; verify migrations apply. (Plan: PLAN-9.1; Reqs: 1, 3, 4, 6)
29. [x] Failure scenario tests: DB unavailability, migration conflicts, malformed JSON, invalid payloads, unauthorized requests (if security enabled). (Plan: PLAN-10.1; Reqs: 2, 7, 12)

## Phase 10 — Release & Documentation
30. [x] Update README with the chosen architecture underlying the project, run instructions, profiles, basic explanation of what each major architectural feature does, Swagger URL, and DB configuration examples. (Plan: PLAN-4.1, PLAN-1.3; Reqs: 5, 8)
31. [x] Add CHANGELOG and versioning strategy for the skeleton. (Plan: PLAN-7.1; Reqs: 10)
32. [x] Update all package-info.java files according to their folder context (Plan: PLAN-4.1; Reqs: 5).
33. [x] Provide sample `.env` or `application-*.properties` templates for quick starts. (Plan: PLAN-1.3; Reqs: 8)
