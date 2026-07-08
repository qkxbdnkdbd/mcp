# Products Login Authentication Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add database-backed login, JWT issuance, and Bearer-token protection for product APIs in `products-crud`.

**Architecture:** Extend the existing Spring Boot controller-service-repository structure with a focused auth module. Use Spring Security for stateless request authentication, BCrypt for password verification, and a small JDK/Jackson-based HS256 JWT provider to avoid an extra JWT library dependency.

**Tech Stack:** Java 8, Spring Boot 2.7.18, Maven, Spring Web, Spring Data JPA, Spring Security, Bean Validation, MySQL, H2, Lombok, JUnit 5, MockMvc.

## Global Constraints

- Work in `products-crud`.
- Base package remains `com.qww.products`.
- Existing `ApiResponse<T>` JSON shape remains `code`, `message`, `data`.
- Default admin credentials are `admin / Admin@123456`.
- Store only BCrypt password hashes in database seed data.
- `POST /api/auth/login` is public.
- `/api/products/**` requires `Authorization: Bearer <token>`.
- Token type string is `Bearer`.
- Default token expiration is `86400` seconds.
- Product CRUD behavior remains unchanged except authentication requirement.
- No frontend, registration, password reset, refresh tokens, logout blacklist, or role-based product authorization.

---

## File Structure

- Modify `products-crud/pom.xml`: add Spring Security starter.
- Modify `products-crud/src/main/resources/schema.sql`: create `users`.
- Modify `products-crud/src/main/resources/data.sql`: seed admin user.
- Modify `products-crud/src/test/resources/test-schema.sql`: create test `users`.
- Modify `products-crud/src/test/resources/test-data.sql`: seed test admin user.
- Modify `products-crud/src/main/resources/application.yml`: add JWT config.
- Modify `products-crud/src/test/resources/application-test.yml`: add deterministic test JWT config.
- Create `products-crud/src/main/java/com/qww/products/domain/User.java`: JPA user entity.
- Create `products-crud/src/main/java/com/qww/products/repository/UserRepository.java`: user lookup.
- Create `products-crud/src/main/java/com/qww/products/dto/LoginRequest.java`: login payload.
- Create `products-crud/src/main/java/com/qww/products/dto/LoginUserResponse.java`: safe user response.
- Create `products-crud/src/main/java/com/qww/products/dto/LoginResponse.java`: token response.
- Create `products-crud/src/main/java/com/qww/products/common/UnauthorizedException.java`: explicit 401 exception.
- Modify `products-crud/src/main/java/com/qww/products/common/GlobalExceptionHandler.java`: map `UnauthorizedException`.
- Create `products-crud/src/main/java/com/qww/products/service/CustomUserDetailsService.java`: Spring Security user loader.
- Create `products-crud/src/main/java/com/qww/products/service/AuthService.java`: login use case.
- Create `products-crud/src/main/java/com/qww/products/security/JwtTokenProvider.java`: HS256 JWT create/validate.
- Create `products-crud/src/main/java/com/qww/products/security/JwtAuthenticationFilter.java`: Bearer-token filter.
- Create `products-crud/src/main/java/com/qww/products/security/JwtAuthenticationEntryPoint.java`: JSON 401 for pre-controller failures.
- Create `products-crud/src/main/java/com/qww/products/security/SecurityConfig.java`: stateless Spring Security config.
- Create `products-crud/src/test/java/com/qww/products/repository/UserRepositoryTest.java`: seeded admin persistence test.
- Create `products-crud/src/test/java/com/qww/products/service/AuthServiceTest.java`: login service tests.
- Create `products-crud/src/test/java/com/qww/products/controller/AuthControllerTest.java`: auth API tests.
- Modify `products-crud/src/test/java/com/qww/products/controller/ProductControllerTest.java`: authenticate existing product API tests and add unauthenticated assertion.
- Modify `products-crud/README.md`: document login and authenticated requests.

### Task 1: User Persistence And Security Dependency

**Files:**
- Modify: `products-crud/pom.xml`
- Modify: `products-crud/src/main/resources/schema.sql`
- Modify: `products-crud/src/main/resources/data.sql`
- Modify: `products-crud/src/test/resources/test-schema.sql`
- Modify: `products-crud/src/test/resources/test-data.sql`
- Create: `products-crud/src/main/java/com/qww/products/domain/User.java`
- Create: `products-crud/src/main/java/com/qww/products/repository/UserRepository.java`
- Create: `products-crud/src/test/java/com/qww/products/repository/UserRepositoryTest.java`

**Interfaces:**
- Produces: `UserRepository.findByUsername(String username): Optional<User>`.
- Produces: `User.isEnabled(): boolean`, returning `status != null && status == 1`.

- [ ] **Step 1: Add failing repository test**

Create `UserRepositoryTest` with two assertions: the seeded `admin` user exists and the password hash starts with `$2a$` or `$2b$`.

Run: `mvn -Dtest=UserRepositoryTest test`

Expected: compile failure because `UserRepository` does not exist.

- [ ] **Step 2: Add Spring Security dependency**

Add `spring-boot-starter-security` to `pom.xml`.

- [ ] **Step 3: Add user schema and seed data**

Add `users` table to main and test schema. Seed `admin` in main and test data using a BCrypt hash for `Admin@123456`.

- [ ] **Step 4: Implement user entity and repository**

Implement `User` with fields `userId`, `username`, `password`, `displayName`, `role`, `status`, `createdAt`, and `updatedAt`.

Implement `UserRepository extends JpaRepository<User, Long>` with `Optional<User> findByUsername(String username)`.

- [ ] **Step 5: Run repository test**

Run: `mvn -Dtest=UserRepositoryTest test`

Expected: test passes.

### Task 2: Login Service And JWT Provider

**Files:**
- Modify: `products-crud/src/main/resources/application.yml`
- Modify: `products-crud/src/test/resources/application-test.yml`
- Create: `products-crud/src/main/java/com/qww/products/dto/LoginRequest.java`
- Create: `products-crud/src/main/java/com/qww/products/dto/LoginUserResponse.java`
- Create: `products-crud/src/main/java/com/qww/products/dto/LoginResponse.java`
- Create: `products-crud/src/main/java/com/qww/products/common/UnauthorizedException.java`
- Modify: `products-crud/src/main/java/com/qww/products/common/GlobalExceptionHandler.java`
- Create: `products-crud/src/main/java/com/qww/products/security/JwtTokenProvider.java`
- Create: `products-crud/src/main/java/com/qww/products/service/AuthService.java`
- Create: `products-crud/src/test/java/com/qww/products/service/AuthServiceTest.java`

**Interfaces:**
- Consumes: `UserRepository.findByUsername(String username)`.
- Produces: `AuthService.login(LoginRequest request): LoginResponse`.
- Produces: `JwtTokenProvider.createToken(User user): String`.
- Produces: `JwtTokenProvider.getUsername(String token): String`.
- Produces: `JwtTokenProvider.validateToken(String token): boolean`.

- [ ] **Step 1: Add failing service tests**

Test that `admin / Admin@123456` returns a `Bearer` response with `expiresIn = 86400`, a non-empty token, and safe user data. Test that a bad password throws `UnauthorizedException`.

Run: `mvn -Dtest=AuthServiceTest test`

Expected: compile failure because auth service classes do not exist.

- [ ] **Step 2: Add JWT configuration**

Add `app.jwt.secret` and `app.jwt.expiration-seconds` to main and test YAML.

- [ ] **Step 3: Implement login DTOs and unauthorized exception**

`LoginRequest` has `@NotBlank username` and `@NotBlank password`.

`LoginUserResponse` contains `userId`, `username`, `displayName`, and `role`.

`LoginResponse` contains `token`, `tokenType`, `expiresIn`, and `LoginUserResponse user`.

- [ ] **Step 4: Implement JWT provider**

Use Java 8 `Base64.getUrlEncoder().withoutPadding()`, `Mac` with `HmacSHA256`, and Jackson `ObjectMapper`. JWT header is `{"alg":"HS256","typ":"JWT"}`. Payload includes `sub`, `uid`, `role`, `iat`, and `exp`.

- [ ] **Step 5: Implement auth service**

Look up user by username, verify `user.isEnabled()`, verify BCrypt password, and throw `UnauthorizedException("invalid username or password")` for all login failures.

- [ ] **Step 6: Run service tests**

Run: `mvn -Dtest=AuthServiceTest test`

Expected: tests pass.

### Task 3: Security Filter, Auth Controller, And Protected Product APIs

**Files:**
- Create: `products-crud/src/main/java/com/qww/products/service/CustomUserDetailsService.java`
- Create: `products-crud/src/main/java/com/qww/products/security/JwtAuthenticationFilter.java`
- Create: `products-crud/src/main/java/com/qww/products/security/JwtAuthenticationEntryPoint.java`
- Create: `products-crud/src/main/java/com/qww/products/security/SecurityConfig.java`
- Create: `products-crud/src/main/java/com/qww/products/controller/AuthController.java`
- Create: `products-crud/src/test/java/com/qww/products/controller/AuthControllerTest.java`
- Modify: `products-crud/src/test/java/com/qww/products/controller/ProductControllerTest.java`

**Interfaces:**
- Consumes: `AuthService.login(LoginRequest request): LoginResponse`.
- Consumes: `JwtTokenProvider.getUsername(String token)` and `validateToken(String token)`.
- Produces: `POST /api/auth/login`.
- Produces: security rule that permits `/api/auth/login` and requires authentication for `/api/products/**`.

- [ ] **Step 1: Add failing controller and protected API tests**

Add `AuthControllerTest` for successful login and invalid login.

Add one product controller test asserting `GET /api/products` without token returns HTTP 401.

Update existing product controller tests to obtain a token by calling `/api/auth/login` and send `Authorization: Bearer <token>`.

Run: `mvn -Dtest=AuthControllerTest,ProductControllerTest test`

Expected: compile failure because controller and security classes do not exist.

- [ ] **Step 2: Implement user details service**

Load active user by username and return Spring Security `UserDetails` with username, hashed password, and authority `ROLE_<role>`.

- [ ] **Step 3: Implement JSON authentication entry point**

Return HTTP 401, content type `application/json;charset=UTF-8`, and serialized `ApiResponse.failure(401, "unauthorized")`.

- [ ] **Step 4: Implement JWT authentication filter**

Read `Authorization`, require `Bearer ` prefix, validate token, load user details, and set `SecurityContextHolder` with `UsernamePasswordAuthenticationToken`.

- [ ] **Step 5: Implement security config**

Declare `PasswordEncoder` as `new BCryptPasswordEncoder()`. Disable CSRF, use stateless sessions, permit `/api/auth/login` and `OPTIONS /**`, require authentication for any other request, register the JWT filter before `UsernamePasswordAuthenticationFilter`, and use the JSON entry point.

- [ ] **Step 6: Implement auth controller**

Expose `POST /api/auth/login`, validate `LoginRequest`, and return `ApiResponse.success(authService.login(request))`.

- [ ] **Step 7: Run controller tests**

Run: `mvn -Dtest=AuthControllerTest,ProductControllerTest test`

Expected: tests pass.

### Task 4: Documentation And Full Verification

**Files:**
- Modify: `products-crud/README.md`

**Interfaces:**
- Consumes: completed login API and security behavior.
- Produces: runnable documentation for database setup, login, and authenticated product calls.

- [ ] **Step 1: Update README**

Add sections for `users` table, default admin credentials, JWT config, login example, and authenticated product API example.

- [ ] **Step 2: Run full test suite**

Run: `mvn clean test`

Expected: all tests pass.

- [ ] **Step 3: Run compile verification**

Run: `mvn -q -DskipTests compile`

Expected: command exits successfully.

## Self-Review

- Spec coverage: database, seeded admin, BCrypt, JWT, login endpoint, product protection, errors, tests, and README are covered by Tasks 1-4.
- Completeness scan: no unfinished markers remain.
- Type consistency: `User`, `UserRepository`, `AuthService`, `JwtTokenProvider`, `LoginRequest`, `LoginResponse`, and `LoginUserResponse` names are consistent across tasks.
