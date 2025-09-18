# CashAdvance

This project implements a simple cash advance system.

## Running

Start the app:

```bash
./mvnw spring-boot:run
```

App runs on http://localhost:8080 by default.

Run with a local in-memory H2 DB (disables Flyway) for testing the API:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments='--spring.profiles.active=local'
```

The application exposes REST endpoints (see "Endpoints" below). Use HTTP Basic to authenticate API requests (e.g., `-u admin:admin123`).

## Swagger UI

API documentation is available at:

- http://localhost:8080/swagger-ui.html
- OpenAPI spec: http://localhost:8080/v3/api-docs

## Endpoints (summary)

Admin endpoints (require ADMIN role):

- GET  /api/admin/users — list users (returns user DTO without password)
- POST /api/admin/users — create user (body: username, password, salary, allowance, roles)
- PUT  /api/admin/users/{userId}/monthly-limit?limit={value} — set monthly limit
- PUT  /api/admin/transactions/{id}/cancel — cancel transaction
- PUT  /api/admin/transactions/{id}/override — override transaction

Supervisor endpoints (require SUPERVISOR or ADMIN):

- POST /api/advances?supervisorId={id} — give advance (body: userId, amount)
- POST /api/advances/returns?supervisorId={id} — return cash (body: userId, amount)

## Postman

Import the Postman collection in `docs/postman/CashAdvance.postman_collection.json`.

Variables to configure:
- baseUrl: http://localhost:8080
- adminUser/adminPass: seeded admin credentials (example admin/admin123)

Configuration:
Use the `application-local.yml` profile for an in-memory H2 database during development. Authentication is HTTP Basic; configure users in the database or via your test data SQL scripts.

## Example curl

Create user (no CSRF):

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -d '{"username":"jane","password":"plain","salary":50000,"allowance":2000,"roles":["EMPLOYEE"]}'
```

List users:

```bash
curl -u admin:admin123 http://localhost:8080/api/admin/users | jq
```

## Notes

- For production you should add a DB migration tool (Flyway/Liquibase) for schema changes.
- Add request validation and ControllerAdvice to produce consistent error messages.
