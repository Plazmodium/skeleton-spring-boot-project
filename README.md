SpringRestSkeleton
==================

A clean Spring Boot skeleton for REST services with layered architecture, JPA/Hibernate, OpenAPI, testing, profiles, and production‑ready defaults.

Architecture Overview
---------------------
The project follows a classic layered architecture with clear responsibilities and minimal coupling:

- Controller layer (`rest.skeleton.spring.boot.controller`)
  - Exposes REST endpoints (HTTP → DTOs)
  - Validates inputs and maps to service calls
  - Returns DTOs and standardized error models
- Service layer (`rest.skeleton.spring.boot.service`)
  - Encapsulates business logic and transactions
  - Coordinates repositories and mapping
  - Defines domain‑centric operations (create/get/list/update/delete)
- Repository layer (`rest.skeleton.spring.boot.repository`)
  - Spring Data JPA repositories (CRUD + pagination)
  - Talks to the database using JPA entities
- Domain model (`rest.skeleton.spring.boot.domain`)
  - JPA entities and domain primitives (e.g., `SampleEntity`)
- Configuration (`rest.skeleton.spring.boot.config`)
  - Spring Boot configuration for security, OpenAPI, DB startup verification, etc.

Major Features
--------------
- CRUD API example for `SampleEntity` at `/api/v1/sample-entities`
- Validation + global error handling (`ApiError` model)
- JPA/Hibernate with H2 (dev/test) and Flyway migrations
- OpenAPI 3 + Swagger UI
- Security (toggleable) with JWT support (token mint endpoint for dev/test)
- Actuator health endpoint and structured logging
- Profiles: `dev` (default), `test`, `prod`

Quick Start
-----------
- JDK: 21 or 25 (see compatibility matrix below)
- Build: `./gradlew clean build`
- Run: `./gradlew bootRun`
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- H2 Console (dev): http://localhost:8080/h2-console

Profiles
--------
- dev (default): H2 in‑memory DB, SQL formatting, H2 console enabled, Flyway enabled
- test: H2, Flyway enabled, logs tuned for debugging
- prod: Placeholders for PostgreSQL (or your RDBMS), JPA `validate`, Flyway enabled

Run instructions
----------------
1) Build and run with dev profile (default):
   - `./gradlew clean build`
   - `./gradlew bootRun`
2) Run with a specific profile:
   - `SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun`
   - or `java -jar build/libs/rest-skeleton-1.0-SNAPSHOT.jar --spring.profiles.active=prod`
3) Security toggles (off by default):
   - Enable security: `--app.security.enabled=true`
   - Enable JWT validation: `--app.security.jwt.enabled=true`
   - Dev stub (accept any token): `--app.security.jwt.acceptAnyToken=true`
4) Mint a test JWT (when JWT enabled):
   - `POST /api/v1/auth/token` with body `{ "subject": "tester", "roles": ["ROLE_USER"] }`
   - Use the returned token as `Authorization: Bearer <token>` on modifying endpoints.

Compatibility Matrix and Guidance
---------------------------------
This project supports two safe combinations. Ensure your Gradle process runs on a supported Java version for the selected Gradle wrapper.

| JVM to run Gradle | Gradle Wrapper | Spring Boot Plugin |
|-------------------|----------------|--------------------|
| Java 21           | 8.10.x         | 3.3.x             |
| Java 25           | 9.2            | 3.4.x             |

Current repository configuration:
- Gradle Wrapper: 9.2
- Spring Boot: 3.4.2
- Java Toolchain (for compilation): 21
- Verified JVMs to run Gradle: 21 and 25 (preferred 25 in modern IDEs)

IDE Setup (IntelliJ IDEA)
-------------------------
1) Settings → Build, Execution, Deployment → Build Tools → Gradle
2) Use Gradle from: “Wrapper task in Gradle build script”
3) Gradle JVM: Prefer JDK 25 (with Gradle 9.2) or JDK 21
4) Reload All Gradle Projects

CLI Setup
---------
- macOS/Linux temporary per shell:
  - For Java 25: `export JAVA_HOME=/path/to/jdk-25 && ./gradlew --version`
  - For Java 21: `export JAVA_HOME=/path/to/jdk-21 && ./gradlew --version`
- Windows PowerShell:
  - `$env:JAVA_HOME = "C:\\Program Files\\Java\\jdk-25"`

Troubleshooting
---------------
- Problem: “Your build is configured to use incompatible Java 25.0.x and Gradle 8.10.x.”
  - Fix Option 1: Run Gradle on JDK 21.
  - Fix Option 2 (chosen in this repo): Upgrade wrapper to Gradle 9.2 and Boot to 3.4.x so Gradle can run on Java 25.

Testing
-------
- Unit/Integration base is configured with JUnit 5 and Mockito.
- Example test: `ApplicationSmokeTest` verifies Spring context boot.
- Run: `./gradlew test`

Database Migrations — How to Run
--------------------------------
Flyway is used for schema migrations. SQL files live under `src/main/resources/db/migration` and are applied automatically on startup when Flyway is enabled in the active profile (already true for `dev`, `test`, and `prod`).

Where migrations live
- Folder: `src/main/resources/db/migration`
- Naming: `V{version}__{description}.sql` (e.g., `V2__add_status_to_sample_entity.sql`)
- Baseline in this repo: `V1__create_sample_entity.sql`

Run migrations (common paths)
1) Auto‑apply on app start (recommended)
   - Dev/Test (H2): simply run the app — Flyway applies pending migrations on startup.
     - `./gradlew bootRun`
     - or `java -jar build/libs/rest-skeleton-1.0-SNAPSHOT.jar`
   - Prod: ensure database URL/credentials/driver are configured (see examples below) and start the app; Flyway runs before the web layer is ready.

2) Run migrations without serving HTTP (headless startup)
   - Useful for one‑off migration runs in CI/CD or maintenance windows.
   - Example (use any profile and DB settings you need):
     - `java -jar build/libs/rest-skeleton-1.0-SNAPSHOT.jar --spring.main.web-application-type=none --spring.profiles.active=prod`
   - This starts Spring, runs Flyway, and exits after the context shuts down (no HTTP server).

3) Run migrations via Flyway Docker CLI (no app start)
   - If you prefer not to start the Spring app, you can use the official Flyway Docker image:
     - Postgres example:
       - `docker run --rm -v "$PWD/src/main/resources/db/migration:/flyway/sql" flyway/flyway:10 -url=jdbc:postgresql://host:5432/db -user=postgres -password=secret migrate`
   - Adjust JDBC URL/credentials for your environment. For multi‑schema setups, add `-schemas=public` (or your schema).

Verifying migrations
- Check app logs on startup for Flyway output and the current/pending versions.
- Inspect the `flyway_schema_history` table in your database to see applied migrations.

Creating a new migration
- Add a new file under `src/main/resources/db/migration`, following the `V{N}__{description}.sql` convention.
- Keep scripts idempotent where feasible and avoid vendor‑specific SQL unless required (the skeleton uses H2‑ and Postgres‑friendly syntax in the baseline example).

Notes
- Profiles already set `spring.flyway.enabled=true` and `spring.jpa.hibernate.ddl-auto=validate` so the schema is validated against entities at startup.
- For production databases, include the appropriate JDBC driver in dependencies (e.g., `org.postgresql:postgresql`) if not already present in your runtime.

Database Configuration Examples
-------------------------------
H2 (dev/test) — already configured in `application-dev.yml` and `application-test.yml`:

```
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:rest_skeleton;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
```

PostgreSQL (prod) — set via env or `application-prod.yml`:

```
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/rest_skeleton
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
```

Verification (documented)
-------------------------
- `./gradlew --version` executed successfully on Java 25 using Gradle 9.2.
- `./gradlew clean build` completed successfully.
- Base `@SpringBootTest` compiled and executed within the build lifecycle.

Tech Stack
----------
- Spring Boot 3.4.x, Java 21 toolchain, Gradle 9.2
- Spring Web, Spring Data JPA, Validation, Actuator, Springdoc OpenAPI
- H2 (dev/test); PostgreSQL placeholders for prod
- JUnit 5, Mockito
