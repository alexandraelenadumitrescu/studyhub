# StudyHub

StudyHub is a small backend service that helps university students keep track of their
courses and the assignments tied to each course. It lets students register the courses
they are enrolled in and then manage the assignments for those courses, including due
dates, priority, and progress status.

## What it does

* Manage courses (name, code, professor, semester)
* Manage assignments (title, description, due date, status, priority)
* Link every assignment to a course
* Track assignment progress through `TODO`, `IN_PROGRESS`, and `COMPLETED`
* Prioritize assignments as `LOW`, `MEDIUM`, or `HIGH`
* Filter assignments by status, priority, or course
* Mark an assignment as completed with a single request
* Automatically record when an assignment was created and last updated

## Technologies

* Java 21
* Spring Boot 3
* Spring Web
* Spring Data JPA
* H2 (in-memory database)
* Bean Validation (Jakarta Validation)
* JUnit 5 / Spring Boot Test
* Maven

## Architecture

The application follows a standard layered architecture:

```text
controller/   REST endpoints, request/response mapping
service/      Business logic and transaction boundaries
repository/   Spring Data JPA repositories
entity/       JPA entities
dto/          Request and response objects
exception/    Custom exceptions and centralized error handling
```

Requests flow from the controller layer into the service layer, which coordinates
repository access and enforces business rules. DTOs keep the persistence model separate
from what is exposed over HTTP, and a global exception handler translates domain errors
into consistent HTTP responses.

## Running the application

Requirements: JDK 21+ and Maven (or the bundled wrapper).

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`. It uses an in-memory H2 database, so
data resets every time the application restarts. The H2 console is available at
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:studyhub`).

## Running the tests

```bash
./mvnw test
```

To build a runnable jar:

```bash
./mvnw package
```

The generated artifact will be at `target/studyhub.jar`.

## Main endpoints

### Courses

| Method | Path                | Description                |
|--------|---------------------|----------------------------|
| POST   | `/api/courses`      | Create a course            |
| GET    | `/api/courses`      | List all courses           |
| GET    | `/api/courses/{id}` | Get a course by id         |
| PUT    | `/api/courses/{id}` | Update a course            |
| DELETE | `/api/courses/{id}` | Delete a course            |

### Assignments

| Method | Path                              | Description                                   |
|--------|------------------------------------|------------------------------------------------|
| POST   | `/api/assignments`                 | Create an assignment for a course              |
| GET    | `/api/assignments`                 | List all assignments                           |
| GET    | `/api/assignments?status=TODO`     | Filter assignments by status                   |
| GET    | `/api/assignments?priority=HIGH`   | Filter assignments by priority                 |
| GET    | `/api/assignments?courseId={id}`   | Filter assignments by course                   |
| GET    | `/api/assignments/{id}`            | Get an assignment by id                        |
| PUT    | `/api/assignments/{id}`            | Update an assignment                           |
| PATCH  | `/api/assignments/{id}/complete`   | Mark an assignment as completed                |
| DELETE | `/api/assignments/{id}`            | Delete an assignment                           |

## Example requests

Create a course:

```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Data Structures",
        "code": "CS201",
        "professor": "Dr. Ionescu",
        "semester": "2026-1"
      }'
```

Create an assignment for that course:

```bash
curl -X POST http://localhost:8080/api/assignments \
  -H "Content-Type: application/json" \
  -d '{
        "title": "Binary Search Tree implementation",
        "description": "Implement insert, delete, and traversal operations",
        "dueDate": "2026-09-01",
        "priority": "HIGH",
        "courseId": 1
      }'
```

Mark an assignment as completed:

```bash
curl -X PATCH http://localhost:8080/api/assignments/1/complete
```

Filter assignments that are still pending:

```bash
curl http://localhost:8080/api/assignments?status=TODO
```

## Project structure

```text
studyhub/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/studyhub/
    │   │   ├── StudyhubApplication.java
    │   │   ├── controller/
    │   │   ├── service/
    │   │   ├── repository/
    │   │   ├── entity/
    │   │   ├── dto/
    │   │   └── exception/
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/studyhub/
            ├── service/
            └── controller/
```
