# Course Catalogue Service (Kotlin + Spring Boot)

This repository contains the code produced by following the tutorial: "Build RESTFUL APIs using Kotlin and Spring Boot".

It showcases how to build a small REST API in Kotlin with Spring Boot, implement basic CRUD for domain resources, validate requests, handle errors globally, and write both unit and integration tests (including Testcontainers for PostgreSQL).

## Tech Stack
- Kotlin, Spring Boot (Web, Validation, Data JPA)
- PostgreSQL (local via Docker Compose for runtime)
- Testcontainers (PostgreSQL) for integration tests
- Gradle (Kotlin DSL)

## Getting Started

### Prerequisites
- JDK 17+
- Docker Desktop (or any Docker engine)
- Git

### 1) Start PostgreSQL
The application is configured to connect to a local PostgreSQL at `localhost:5438` (see `src/main/resources/application.yml`). Start the DB with Docker Compose from the project root:

```
docker-compose up -d
```

This starts a Postgres instance with:
- user: postgres
- password: postgres
- database: courses
- port mapping: 5438 -> 5432

### 2) Run the Application
From the project root:

```
./gradlew bootRun
```

On Windows PowerShell:

```
./gradlew.bat bootRun
```

The service will start on `http://localhost:8080`.

### 3) Execute Requests
Sample curl commands are provided in:
- `src/main/resources/curl-commands.txt`

Key endpoints:
- POST `/v1/instructors` – create an instructor
- POST `/v1/courses` – create a course (requires a valid `instructorId`)
- GET `/v1/courses` – list all courses; filter by `?course_name=...`
- PUT `/v1/courses/{course_id}` – update name/category
- DELETE `/v1/courses/{course_id}` – delete a course

Validation and error handling:
- Bean validation errors are returned as HTTP 400 with messages.
- An invalid instructorId when creating a course returns HTTP 400.

## Testing

Run all tests (unit + integration):

```
./gradlew test
```

Integration tests use Testcontainers to spin up an ephemeral PostgreSQL automatically, so you do not need to start Docker Compose for tests. See:
- `src/test/integration/.../util/PostgreSQLContainerInitializer.kt`
- `src/main/resources/application-test.yml`

## Project Structure (high level)
- `controller/` REST controllers for Courses and Instructors
- `service/` Business logic
- `repository/` Spring Data JPA repositories
- `entity/` JPA entities
- `exception/` Custom exceptions
- `exceptionhandler/` Centralized error handling
- `src/test/unit` Unit tests (controllers)
- `src/test/integration` Integration tests (controllers, repositories, Testcontainers)

## Notes from the Tutorial
- The code intentionally focuses on clarity and the core building blocks of a REST API.
- DTOs are used at the API boundary; entities map to the database.
- Global error handling demonstrates how to translate exceptions into consistent HTTP responses.

## Troubleshooting
- If the app fails to start due to DB connectivity, ensure Docker Compose is running and port 5438 is free.
- To reset the DB locally, stop the app and run `docker-compose down -v` then `docker-compose up -d`.

## License
This code was created as the result of following a tutorial and is intended for educational purposes.
