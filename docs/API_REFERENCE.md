# CashAdvance API Reference

This document describes the REST API exposed by the CashAdvance service.

Base URL: http://localhost:8080

Authentication: HTTP Basic. Use an account with the appropriate role for each endpoint (ADMIN, MANAGER, SUPERVISOR).

Headers:
- Content-Type: application/json
- Accept: application/json

---

## Admin endpoints (/api/admin)

All endpoints below require role: ADMIN

### GET /api/admin/users
- Description: List all users.
- Auth: ADMIN
- Request: none
- Response: 200 OK, JSON array of `UserResponse` objects

UserResponse shape:
```
{
  "id": 1,
  "username": "alice",
  "salary": 50000.0,
  "allowance": 2000.0,
  "monthlyLimit": 1000.0,
  "roles": ["EMPLOYEE"]
}
```

Example:
```bash
curl -u admin:admin123 http://localhost:8080/api/admin/users | jq
```

### POST /api/admin/users
- Description: Create a new user with roles.
- Auth: ADMIN
- Request body (JSON): `CreateUserRequest`

CreateUserRequest shape:
```
{
  "username": "jane",
  "password": "secret123",
  "salary": 50000.0,
  "allowance": 2000.0,
  "roles": ["EMPLOYEE"]
}
```
- Response: 200 OK, created `UserResponse` object

Example:
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -d '{"username":"jane","password":"secret","salary":50000,"allowance":2000,"roles":["EMPLOYEE"]}' | jq
```

### PUT /api/admin/users/{userId}/monthly-limit?limit={value}
- Description: Set monthly limit for a user.
- Auth: ADMIN
- Path param: `userId` (long)
- Query param: `limit` (double)
- Response: 200 OK with a plain text success message

Example:
```bash
curl -u admin:admin123 -X PUT "http://localhost:8080/api/admin/users/5/monthly-limit?limit=1500"
```

### PUT /api/admin/transactions/{id}/cancel
- Description: Cancel a transaction.
- Auth: ADMIN
- Path param: `id` (transaction id)
- Response: 200 OK with a plain text message

Example:
```bash
curl -u admin:admin123 -X PUT http://localhost:8080/api/admin/transactions/10/cancel
```

### PUT /api/admin/transactions/{id}/override
- Description: Override a transaction.
- Auth: ADMIN
- Path param: `id` (transaction id)
- Response: 200 OK with a plain text message

Example:
```bash
curl -u admin:admin123 -X PUT http://localhost:8080/api/admin/transactions/10/override
```

---

## Advance endpoints (/api/advances)

Requires role: SUPERVISOR or ADMIN for both endpoints below.

### POST /api/advances
- Description: Give an advance to a user.
- Auth: SUPERVISOR or ADMIN
- Query param: `supervisorId` (long) - the supervisor performing the action
- Request body: `AdvanceRequest`

AdvanceRequest shape:
```
{
  "userId": 12,
  "amount": 250.0
}
```
- Response: 200 OK, `AdvanceResponse`:
```
{
  "transactionId": 42,
  "amount": 250.0,
  "remainingLimit": 750.0
}
```

Example:
```bash
curl -u supervisor:pass -X POST "http://localhost:8080/api/advances?supervisorId=3" \
  -H "Content-Type: application/json" \
  -d '{"userId":12,"amount":250}' | jq
```

### POST /api/advances/returns
- Description: Return cash / record a return for a user.
- Auth: SUPERVISOR or ADMIN
- Query param: `supervisorId` (long)
- Request body: `AdvanceRequest` (same as above)
- Response: 200 OK, `AdvanceResponse`

Example:
```bash
curl -u supervisor:pass -X POST "http://localhost:8080/api/advances/returns?supervisorId=3" \
  -H "Content-Type: application/json" \
  -d '{"userId":12,"amount":100}' | jq
```

---

## Transactions endpoint (/api/transactions)

### GET /api/transactions
- Description: List all advance transactions. Returns model objects representing transactions.
- Auth: MANAGER or ADMIN or SUPERVISOR
- Request: none
- Response: 200 OK, JSON array of `AdvanceTransaction` model objects.

Example:
```bash
curl -u manager:manager123 http://localhost:8080/api/transactions | jq
```

Note: The `AdvanceTransaction` model includes fields such as id, user, amount, timestamp, status. Inspect the model class if you need exact field names.

---

## Supervisor endpoints (/api/supervisors)

### POST /api/supervisors
- Description: Create a supervisor user.
- Auth: MANAGER or ADMIN
- Request body: `User` model JSON (the controller accepts a `User` model instance)
- Response: 201 CREATED with the created `User` object

Example:
```bash
curl -u manager:manager123 -X POST http://localhost:8080/api/supervisors \
  -H "Content-Type: application/json" \
  -d '{"username":"sup1","password":"secret","salary":40000,"allowance":1000}' | jq
```

---

## Notes and tips
- Authentication: the app uses HTTP Basic for API access. Use `-u username:password` with `curl`.
- Validation: Many endpoints use `@Valid` for request bodies and will return 400 Bad Request with a JSON error body when validation fails.
- Swagger/OpenAPI: The project includes `springdoc-openapi-starter-webmvc-ui` — the API docs are available at `/swagger-ui.html` and the OpenAPI spec at `/v3/api-docs`.
- DB: For local development, run with the `local` profile (H2 in-memory) if present. Example:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments='--spring.profiles.active=local'
```

- For more precise request/response field names (e.g., `AdvanceTransaction`), inspect the model classes under `src/main/java/com/example/cashadvance/model`.

---

If you want, I can:
- Expand the reference with exact model field lists for `AdvanceTransaction`, `User`, and other model classes.
- Auto-generate example Postman collection or OpenAPI snippets for these endpoints.
- Add sample responses for error cases (400, 403, 500) based on `GlobalExceptionHandler` behavior.

