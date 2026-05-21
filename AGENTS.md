# AGENTS.md

## Build order

`axon-event-commons` must be installed first — it is a local JAR dependency for the other three services:

```bash
./mvnw clean install --projects axon-event-commons
```

Then run any service with `spring-boot:run`:

```bash
./mvnw clean spring-boot:run --projects customer-service -Dspring-boot.run.jvmArguments="-Dserver.port=9080"
```

## Formatting

Spotless (Google Java Format + Prettier) is enforced during `./mvnw verify`. Fix before commit:

```bash
./mvnw spotless:apply
```

Wildcard imports are **forbidden**. Unused imports are removed automatically.

## Testing

- Tests **disable Axon Server** (`axon.axonserver.enabled=false`) — no Axon Server needed.
- **Testcontainers** are used for real DBs (MySQL, PostgreSQL, MongoDB). Docker is required.
- Per-service test command: `./mvnw clean test --projects <module>`
- All tests: `./mvnw clean test`
- Aggregate unit tests use `axon-test` (e.g., `CustomerAggregateTest`, `RestaurantAggregateTest`, `OrderAggregateTest`).
- JPA tests set `spring.jpa.hibernate.ddl-auto=create-drop`.
- Testcontainers helper interfaces: `MySQLTestcontainers`, `PostgreSQLTestcontainers`, `MongoDBTestcontainers`.

## Key configuration quirks

- **`spring.main.allow-circular-references=true`** in every service's `application.properties`.
- **Jackson 2 compat**: Spring Boot 4.x defaults to Jackson 3, but Hibernate's JSON mapping still needs Jackson 2. Each service pulls in `com.fasterxml.jackson.core:jackson-databind:runtime`.
- **XStream workaround**: `AxonConfig` in each service registers an XStream bean with `allowTypesByWildcard` to avoid `ForbiddenClassException`.
- Axon Server connection: `axon.axonserver.servers=${AXON_SERVER_HOST:localhost}:${AXON_SERVER_PORT:8124}`.

## Docker operations

No Docker Compose — plain bash scripts:

| Script | Purpose |
|---|---|
| `init-environment.sh` | Start Axon Server + MySQL + PostgreSQL + MongoDB |
| `shutdown-environment.sh` | Remove containers and network |
| `build-docker-images.sh` | Build images via `spring-boot:build-image` (skips tests, prefix `ivanfranchin`) |
| `start-apps.sh` | Run all 3 service containers |
| `stop-apps.sh` | Stop service containers |

Port mapping: 8080 (container) → 9080/9081/9082 (host).

## Architecture / package conventions

- `command/` — Axon command objects
- `aggregate/` — Axon event-sourced aggregates (`@Aggregate`)
- `query/` — Axon query objects
- `repository/` — JPA/Mongo repositories + event-sourcing projectors
- `rest/` — REST controllers and DTOs
- `websocket/` — WebSocket handlers and config
- `config/` — Spring config (`AxonConfig`, `ErrorAttributesConfig`, `MongoConfig`)
- `interceptor/` — Axon dispatch interceptors (logging)

Shared events live in `axon-event-commons` under `com.ivanfranchin.axoneventcommons.*`.

## Lombok

Used throughout — `@NoArgsConstructor`, `@Data`, `@RequiredArgsConstructor`, `@Slf4j`, etc. Annotation processor is configured per-service POM.

## Environment variables

| Service | DB vars | Axon vars |
|---|---|---|
| customer-service | `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_USERNAME`, `MYSQL_PASSWORD` | `AXON_SERVER_HOST`, `AXON_SERVER_PORT` |
| restaurant-service | `POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_USERNAME`, `POSTGRES_PASSWORD` | `AXON_SERVER_HOST`, `AXON_SERVER_PORT` |
| food-ordering-service | `MONGODB_HOST`, `MONGODB_PORT`, `MONGODB_DATABASE` | `AXON_SERVER_HOST`, `AXON_SERVER_PORT` |

All have sensible localhost defaults.

## Java version

**Java 25** required. Set via `<java.version>25</java.version>` in root POM.
