# Products Login Authentication Design

## Goal

Add a backend-only login feature to the existing `products-crud` Spring Boot API. The feature must create the required database model, authenticate users with a database-backed account, return a JWT after successful login, and protect the existing product CRUD APIs with Bearer-token authentication.

## Scope

- Add user persistence with a `users` table.
- Seed a default administrator account: `admin / Admin@123456`.
- Store passwords as BCrypt hashes only.
- Add `POST /api/auth/login`.
- Return JWT login data in the existing `ApiResponse` wrapper.
- Require `Authorization: Bearer <token>` for `/api/products/**`.
- Add service, security, controller, and integration tests.
- Update README with database setup, login, and authenticated API examples.

Out of scope:

- Frontend login page.
- Registration.
- Password reset.
- Refresh tokens.
- Role-based product permissions beyond a simple authenticated-user gate.
- Logout token blacklist.

## Current Project Context

The project is a single Maven module under `products-crud` using:

- Java 8.
- Spring Boot 2.7.18.
- Spring Web.
- Spring Data JPA.
- Spring Validation.
- MySQL at runtime.
- H2 for tests.
- Lombok.

The current application exposes `/api/products/**` without authentication. Responses use `ApiResponse<T>` with `code`, `message`, and `data`.

## Architecture

Add a small authentication module that follows the existing controller-service-repository style:

- `domain.User`: JPA entity mapped to `users`.
- `repository.UserRepository`: Spring Data repository with `findByUsername`.
- `dto.LoginRequest`: request body for login.
- `dto.LoginResponse`: token response payload.
- `dto.LoginUserResponse`: safe user fields returned after login.
- `service.AuthService`: validates credentials and builds login response.
- `service.CustomUserDetailsService`: loads active users for Spring Security.
- `security.JwtTokenProvider`: creates and validates JWTs.
- `security.JwtAuthenticationFilter`: reads Bearer tokens and sets the Spring Security context.
- `security.JwtAuthenticationEntryPoint`: returns JSON 401 responses.
- `security.SecurityConfig`: configures stateless JWT security.
- `controller.AuthController`: exposes `/api/auth/login`.

## Database Design

Add table `users` to both main and test schema SQL:

- `user_id BIGINT AUTO_INCREMENT PRIMARY KEY`
- `username VARCHAR(64) NOT NULL UNIQUE`
- `password VARCHAR(100) NOT NULL`
- `display_name VARCHAR(128)`
- `role VARCHAR(32) NOT NULL DEFAULT 'ADMIN'`
- `status INT NOT NULL DEFAULT 1`
- `created_at DATETIME`
- `updated_at DATETIME`

Seed data adds one administrator row:

- Username: `admin`
- Plain initial password for operators: `Admin@123456`
- Stored password: BCrypt hash generated for `Admin@123456`
- Display name: `Administrator`
- Role: `ADMIN`
- Status: `1`

## API Design

### Login

`POST /api/auth/login`

Request:

```json
{
  "username": "admin",
  "password": "Admin@123456"
}
```

Success response:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "token": "<jwt>",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "user": {
      "userId": 1,
      "username": "admin",
      "displayName": "Administrator",
      "role": "ADMIN"
    }
  }
}
```

Validation failure returns HTTP 400 through the existing validation handler.

Authentication failure returns HTTP 401:

```json
{
  "code": 401,
  "message": "invalid username or password",
  "data": null
}
```

Unknown username, wrong password, and disabled user all use the same public failure message.

### Product APIs

All `/api/products/**` endpoints require:

```http
Authorization: Bearer <jwt>
```

Missing, malformed, expired, or invalid tokens return HTTP 401 with:

```json
{
  "code": 401,
  "message": "unauthorized",
  "data": null
}
```

## Security Configuration

- Use Spring Security in stateless mode.
- Disable CSRF because the API is stateless and token based.
- Permit `/api/auth/login`.
- Permit CORS preflight `OPTIONS` requests.
- Authenticate all other API requests.
- Use BCrypt for password verification.
- Sign JWTs with an HMAC secret from configuration.
- Configure token expiration from application configuration.

Configuration keys:

```yaml
app:
  jwt:
    secret: change-this-secret-for-production
    expiration-seconds: 86400
```

The production README must tell operators to replace the default secret.

## Error Handling

Add an `UnauthorizedException` for explicit login failures and map it to HTTP 401 in `GlobalExceptionHandler`.

Spring Security authentication failures that occur before controller dispatch are handled by `JwtAuthenticationEntryPoint` so they still return the same `ApiResponse` JSON shape.

Existing validation, not-found, and fallback errors remain unchanged.

## Testing

Add or update tests for:

- `UserRepository` can find the seeded admin user.
- `AuthService` logs in successfully with `admin / Admin@123456`.
- `AuthService` rejects bad credentials.
- `AuthController` returns token data for valid credentials.
- `AuthController` returns HTTP 401 for invalid credentials.
- `/api/products` returns HTTP 401 without a token.
- `/api/products` succeeds with a token.
- Existing product controller tests authenticate before calling protected endpoints.
- Full application context starts with security configuration enabled.

## Documentation

Update `products-crud/README.md` with:

- The new `users` table.
- Default admin credentials.
- Login request example.
- Authenticated product request example.
- JWT configuration keys and production secret warning.

## Acceptance Criteria

- `mvn test` passes.
- `mvn -q -DskipTests compile` passes.
- Database initialization creates both `products` and `users`.
- Login returns a usable JWT.
- Product APIs reject unauthenticated requests.
- Product APIs accept requests with a valid Bearer token.
